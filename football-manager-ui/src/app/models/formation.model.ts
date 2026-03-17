import { PlayerDto } from './player.model';

export interface FormationRequest {
  name: string;
  playerIds: number[];
}

export interface FormationResponse {
  id: number;
  name: string;
  players: PlayerDto[];
}

