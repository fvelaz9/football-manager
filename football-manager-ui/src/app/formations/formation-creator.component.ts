import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PlayerService } from '../services/player.service';
import { FormationService } from '../services/formation.service';
import { PlayerDto } from '../models/player.model';
import { FormationRequest, FormationResponse } from '../models/formation.model';

@Component({
  selector: 'app-formation-creator',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './formation-creator.component.html',
})
export class FormationCreatorComponent implements OnInit {
  // Data
  formations: FormationResponse[] = [];
  allPlayers: PlayerDto[] = [];

  // Selected formation to view
  selectedFormationId: number | null = null;
  selectedFormation: FormationResponse | null = null;

  // Create panel state
  showCreatePanel = false;
  newFormationName = '';
  newSelectedPlayerIds: Set<number> = new Set();

  // UI state
  error: string | null = null;
  success: string | null = null;
  removingPlayerId: number | null = null;

  constructor(
    private playerService: PlayerService,
    private formationService: FormationService
  ) {}

  ngOnInit(): void {
    this.loadFormations();
    this.playerService.getAll().subscribe({
      next: (players) => (this.allPlayers = players),
      error: () => (this.error = 'Error cargando jugadores'),
    });
  }

  loadFormations(): void {
    this.formationService.getAll().subscribe({
      next: (formations) => {
        this.formations = formations;
        // Refresh selected formation data
        if (this.selectedFormationId) {
          this.selectedFormation =
            formations.find((f) => f.id === this.selectedFormationId) ?? null;
        }
      },
      error: () => {},
    });
  }

  onSelectFormation(id: number | null): void {
    this.selectedFormationId = id ? Number(id) : null;
    this.selectedFormation = id
      ? (this.formations.find((f) => f.id === Number(id)) ?? null)
      : null;
    this.error = null;
    this.success = null;
  }

  // --- Create panel ---
  openCreatePanel(): void {
    this.showCreatePanel = true;
    this.newFormationName = '';
    this.newSelectedPlayerIds = new Set();
    this.error = null;
    this.success = null;
  }

  closeCreatePanel(): void {
    this.showCreatePanel = false;
  }

  toggleNewPlayer(player: PlayerDto): void {
    if (this.newSelectedPlayerIds.has(player.id!)) {
      this.newSelectedPlayerIds.delete(player.id!);
    } else {
      this.newSelectedPlayerIds.add(player.id!);
    }
    this.error = null;
  }

  isNewPlayerSelected(player: PlayerDto): boolean {
    return this.newSelectedPlayerIds.has(player.id!);
  }

  get newSelectionCount(): number {
    return this.newSelectedPlayerIds.size;
  }

  canCreate(): boolean {
    return this.newFormationName.trim().length > 0;
  }

  isSubmitting = false;

  createFormation(): void {
    if (this.isSubmitting) return;

    this.error = null;
    this.success = null;

    if (!this.canCreate()) {
      this.error = 'Necesitás un nombre para la formación.';
      return;
    }

    this.isSubmitting = true;

    const req: FormationRequest = {
      name: this.newFormationName.trim(),
      playerIds: Array.from(this.newSelectedPlayerIds),
    };

    this.formationService.create(req).subscribe({
      next: (created) => {
        this.isSubmitting = false;
        this.success = `Formación "${created.name}" creada correctamente.`;
        this.showCreatePanel = false;
        this.selectedFormationId = created.id;
        this.selectedFormation = created;
        this.loadFormations();
      },
      error: (err) => {
        this.isSubmitting = false;
        this.error = err?.error?.message || 'Error creando la formación.';
      },
    });
  }

  // --- Remove player from formation ---
  removePlayer(player: PlayerDto): void {
    if (!this.selectedFormation || !player.id) return;
    this.removingPlayerId = player.id;
    this.error = null;

    this.formationService
      .removePlayer(this.selectedFormation.id, player.id)
      .subscribe({
        next: (updated) => {
          this.selectedFormation = updated;
          const idx = this.formations.findIndex((f) => f.id === updated.id);
          if (idx !== -1) this.formations[idx] = updated;
          this.removingPlayerId = null;
        },
        error: () => {
          this.error = 'Error eliminando el jugador de la formación.';
          this.removingPlayerId = null;
        },
      });
  }
}

