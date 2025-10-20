import { Routes } from '@angular/router';
import { HomeComponent } from './home/home';
import { LoginComponent } from './components/log-in/log-in';
import { RegisterComponent } from './components/register/register';
import { ResetPasswordComponent } from './components/reset-password/reset-password';
import { AuthGuard } from './services/auth-guard';
import { HistoryPageComponent } from './history/history';
import { SettingsComponent } from './settings/settings';

export const routes: Routes = [
    { path: '', redirectTo: '/login', pathMatch: 'full'}, 
    { path: 'login', component: LoginComponent},
    { path: 'register', component: RegisterComponent },
    { path: 'home', component: HomeComponent, canActivate: [AuthGuard]}, //canActivate: [AuthGaurd]
    { path: 'reset-password', component: ResetPasswordComponent},
    { path: 'history', component: HistoryPageComponent, canActivate: [AuthGuard]},
    { path: 'settings', component: SettingsComponent, canActivate: [AuthGuard]}
];
