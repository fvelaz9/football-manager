import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, shareReplay, tap } from 'rxjs';
import { FormationRequest, FormationResponse } from '../models/formation.model';

@Injectable({
  providedIn: 'root',
})
export class FormationService {
  private readonly baseUrl = 'http://localhost:8080/api/formations';
  private formationsCache$: Observable<FormationResponse[]> | null = null;

  constructor(private http: HttpClient) {}

  getAll(): Observable<FormationResponse[]> {
    if (!this.formationsCache$) {
      this.formationsCache$ = this.http.get<FormationResponse[]>(this.baseUrl).pipe(
        shareReplay(1)
      );
    }
    return this.formationsCache$;
  }

  getById(id: number): Observable<FormationResponse> {
    return this.http.get<FormationResponse>(`${this.baseUrl}/${id}`);
  }

  create(req: FormationRequest): Observable<FormationResponse> {
    return this.http.post<FormationResponse>(this.baseUrl, req).pipe(
      tap(() => this.clearCache())
    );
  }

  update(id: number, req: FormationRequest): Observable<FormationResponse> {
    return this.http.put<FormationResponse>(`${this.baseUrl}/${id}`, req).pipe(
      tap(() => this.clearCache())
    );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`).pipe(
      tap(() => this.clearCache())
    );
  }

  removePlayer(formationId: number, playerId: number): Observable<FormationResponse> {
    return this.http.delete<FormationResponse>(
      `${this.baseUrl}/${formationId}/players/${playerId}`
    ).pipe(
      tap(() => this.clearCache())
    );
  }

  clearCache(): void {
    this.formationsCache$ = null;
  }
}


