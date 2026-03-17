import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PlayerDto } from '../models/player.model';

@Injectable({
  providedIn: 'root',
})
export class PlayerService {
  private readonly baseUrl = 'http://localhost:8080/api/players';

  constructor(private http: HttpClient) {}

  getAll(): Observable<PlayerDto[]> {
    return this.http.get<PlayerDto[]>(this.baseUrl);
  }

  getById(id: number): Observable<PlayerDto> {
    return this.http.get<PlayerDto>(`${this.baseUrl}/${id}`);
  }

  create(player: PlayerDto): Observable<PlayerDto> {
    return this.http.post<PlayerDto>(this.baseUrl, player);
  }

  update(id: number, player: PlayerDto): Observable<PlayerDto> {
    return this.http.put<PlayerDto>(`${this.baseUrl}/${id}`, player);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}

