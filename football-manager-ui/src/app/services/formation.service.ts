import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FormationRequest, FormationResponse } from '../models/formation.model';

@Injectable({
  providedIn: 'root',
})
export class FormationService {
  private readonly baseUrl = 'http://localhost:8080/api/formations';

  constructor(private http: HttpClient) {}

  getAll(): Observable<FormationResponse[]> {
    return this.http.get<FormationResponse[]>(this.baseUrl);
  }

  getById(id: number): Observable<FormationResponse> {
    return this.http.get<FormationResponse>(`${this.baseUrl}/${id}`);
  }

  create(req: FormationRequest): Observable<FormationResponse> {
    return this.http.post<FormationResponse>(this.baseUrl, req);
  }

  update(id: number, req: FormationRequest): Observable<FormationResponse> {
    return this.http.put<FormationResponse>(`${this.baseUrl}/${id}`, req);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  removePlayer(formationId: number, playerId: number): Observable<FormationResponse> {
    return this.http.delete<FormationResponse>(
      `${this.baseUrl}/${formationId}/players/${playerId}`
    );
  }
}


