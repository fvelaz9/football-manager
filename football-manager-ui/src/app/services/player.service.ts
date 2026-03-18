import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, shareReplay, tap } from 'rxjs';
import { PlayerDto } from '../models/player.model';

@Injectable({
  providedIn: 'root',
})
export class PlayerService {
  private readonly baseUrl = 'http://localhost:8080/api/players';
  private playersCache$: Observable<PlayerDto[]> | null = null;

  constructor(private http: HttpClient) {}

  getAll(): Observable<PlayerDto[]> {
    if (!this.playersCache$) {
      this.playersCache$ = this.http.get<PlayerDto[]>(this.baseUrl).pipe(
        shareReplay(1)
      );
    }
    return this.playersCache$;
  }

  getById(id: number): Observable<PlayerDto> {
    return this.http.get<PlayerDto>(`${this.baseUrl}/${id}`);
  }

  create(player: PlayerDto): Observable<PlayerDto> {
    return this.http.post<PlayerDto>(this.baseUrl, player).pipe(
      tap(() => this.clearCache())
    );
  }

  update(id: number, player: PlayerDto): Observable<PlayerDto> {
    return this.http.put<PlayerDto>(`${this.baseUrl}/${id}`, player).pipe(
      tap(() => this.clearCache())
    );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`).pipe(
      tap(() => this.clearCache())
    );
  }

  clearCache(): void {
    this.playersCache$ = null;
  }
}

