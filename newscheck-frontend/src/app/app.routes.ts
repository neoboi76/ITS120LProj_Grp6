import { Routes } from '@angular/router';
import { HomeComponent } from './home/home';
import { LoginComponent } from './components/log-in/log-in';
import { RegisterComponent } from './components/register/register';
import { ResetPasswordComponent } from './components/reset-password/reset-password';
import { AuthGuard } from './services/auth-guard';
import { HistoryPageComponent } from './history/history';
import { SettingsComponent } from './settings/settings';
import { EntryDetailsComponent } from './components/entry-details/entry-details';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard';
import { AdminGuard } from './services/admin-gaurd';
import { AdminUsersComponent } from './components/admin-users/admin-users';
import { AdminVerificationsComponent } from './components/admin-verifications/admin-verifications';
import { AdminAuditLogsComponent } from './components/admin-audit-logs/admin-audit-logs';
import { ForgotPasswordComponent } from './components/forgot-password/forgot-password';

export const routes: Routes = [
    { path: '', redirectTo: '/login', pathMatch: 'full'}, 
    { path: 'login', component: LoginComponent},
    { path: 'register', component: RegisterComponent },
    { path: 'home', component: HomeComponent, canActivate: [AuthGuard]}, 
    { path: 'reset-password', component: ResetPasswordComponent},
    { path: 'forgot-password', component: ForgotPasswordComponent},
    { path: 'history', component: HistoryPageComponent, canActivate: [AuthGuard]},
    { path: 'settings', component: SettingsComponent, canActivate: [AuthGuard]},
    { path: 'entry-details/:id', component: EntryDetailsComponent, canActivate: [AuthGuard]},
    { path: 'admin/dashboard', component: AdminDashboardComponent, canActivate: [AdminGuard]},
    { path: 'admin/users', component: AdminUsersComponent, canActivate: [AdminGuard]},
    { path: 'admin/verifications', component: AdminVerificationsComponent, canActivate: [AdminGuard]},
    { path: 'admin/audit-logs', component: AdminAuditLogsComponent, canActivate: [AdminGuard]}

];