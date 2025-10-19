import { Routes } from '@angular/router';
import { HomeComponent } from './home/home';
import { LoginComponent } from './components/log-in/log-in';
import { RegisterComponent } from './components/register/register';
import { ResetPasswordComponent } from './components/reset-password/reset-password';
import { AuthGaurd } from './services/auth-gaurd';
import { HistoryPageComponent } from './history/history';
import { SettingsComponent } from './settings/settings';

export const routes: Routes = [
    { path: '', component: LoginComponent}, 
    { path: 'login', component: LoginComponent},
    { path: 'register', component: RegisterComponent },
    { path: 'home', component: HomeComponent, }, //canActivate: [AuthGaurd]
    { path: 'reset-password', component: ResetPasswordComponent },
    { path: 'history', component: HistoryPageComponent },
    { path: 'settings', component: SettingsComponent}
];
