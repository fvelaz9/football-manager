import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PlayerService } from '../services/player.service';
import { FormationService } from '../services/formation.service';
import { PlayerDto } from '../models/player.model';
import { FormationResponse } from '../models/formation.model';

@Component({
  selector: 'app-player-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './player-dashboard.component.html',
})
export class PlayerDashboardComponent implements OnInit {
  players: PlayerDto[] = [];
  formations: FormationResponse[] = [];
  selected: PlayerDto | null = null;
  form: PlayerDto = { name: '', position: '', number: 0 };
  loading = false;
  isSubmitting = false;
  error: string | null = null;

  // Track which player's formation panel is open
  expandedPlayerId: number | null = null;

  constructor(
    private playerService: PlayerService,
    private formationService: FormationService
  ) {}

  ngOnInit(): void {
    this.load();
    this.loadFormations();
  }

  load(): void {
    this.loading = true;
    this.error = null;
    this.playerService.getAll().subscribe({
      next: (players) => {
        this.players = players;
        this.loading = false;
      },
      error: () => {
        this.error = 'Error cargando jugadores';
        this.loading = false;
      },
    });
  }

  loadFormations(): void {
    this.formationService.getAll().subscribe({
      next: (formations) => (this.formations = formations),
      error: () => {},
    });
  }

  isInFormation(player: PlayerDto, formation: FormationResponse): boolean {
    return formation.players.some((p) => p.id === player.id);
  }

  toggleFormation(player: PlayerDto, formation: FormationResponse): void {
    const alreadyIn = this.isInFormation(player, formation);
    let newPlayerIds: number[];

    if (alreadyIn) {
      newPlayerIds = formation.players
        .filter((p) => p.id !== player.id)
        .map((p) => p.id!);
    } else {
      newPlayerIds = [...formation.players.map((p) => p.id!), player.id!];
    }

    if (alreadyIn) {
      this.formationService.removePlayer(formation.id, player.id!).subscribe({
        next: (updated) => {
          const idx = this.formations.findIndex((f) => f.id === formation.id);
          if (idx !== -1) this.formations[idx] = updated;
        },
        error: () => (this.error = 'Error quitando jugador de la formación'),
      });
    } else {
      this.formationService
        .update(formation.id, { name: formation.name, playerIds: newPlayerIds })
        .subscribe({
          next: (updated) => {
            const idx = this.formations.findIndex((f) => f.id === formation.id);
            if (idx !== -1) this.formations[idx] = updated;
          },
          error: (err) =>
            (this.error = err?.error?.message || 'Error agregando jugador a la formación'),
        });
    }
  }

  toggleExpand(player: PlayerDto): void {
    this.expandedPlayerId = this.expandedPlayerId === player.id ? null : player.id!;
  }

  edit(player: PlayerDto): void {
    this.selected = player;
    this.form = { ...player };
  }

  resetForm(): void {
    this.selected = null;
    this.form = { name: '', position: '', number: 0 };
    this.isSubmitting = false;
  }

  save(): void {
    if (this.isSubmitting) return;
    this.isSubmitting = true;
    this.error = null;
    const action = this.selected?.id
      ? this.playerService.update(this.selected.id!, this.form)
      : this.playerService.create(this.form);

    action.subscribe({
      next: () => {
        this.isSubmitting = false;
        this.resetForm();
        this.load();
      },
      error: () => {
        this.isSubmitting = false;
        this.error = 'Error guardando jugador';
      },
    });
  }

  delete(player: PlayerDto): void {
    if (!player.id) return;
    this.playerService.delete(player.id).subscribe({
      next: () => this.load(),
      error: () => {
        this.error = 'Error eliminando jugador';
      },
    });
  }
}



