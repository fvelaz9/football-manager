import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { PlayerDashboardComponent } from './players/player-dashboard.component';
import { FormationCreatorComponent } from './formations/formation-creator.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'players', component: PlayerDashboardComponent },
  { path: 'formations', component: FormationCreatorComponent },
];

