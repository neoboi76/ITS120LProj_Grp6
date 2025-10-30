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


/* Developed by Group 6:
      Ken Aliling
      Anicia Kaela Bonayao
      Carl Norbi Felonia
      Cedrick Miguel Kaneko
      Dino Alfred T. Timbol (Group Leader) */

export const routes: Routes = [

    //AuthGaurd = if not logged in (i.e., no JWT token stored), user will be redirected to login page
    //AdminGaurd = if not admin, user will be redirected to home

    { path: '', redirectTo: '/login', pathMatch: 'full'},  //Route to login page
    { path: 'login', component: LoginComponent},// Route to login page
    { path: 'register', component: RegisterComponent }, //Route to sign up page
    { path: 'home', component: HomeComponent, canActivate: [AuthGuard]}, //Route to home page
    { path: 'reset-password', component: ResetPasswordComponent}, //Route to reset password page
    { path: 'forgot-password', component: ForgotPasswordComponent},//Route to forget password page
    { path: 'history', component: HistoryPageComponent, canActivate: [AuthGuard]},//Route to history page
    { path: 'settings', component: SettingsComponent, canActivate: [AuthGuard]},//Route to settings page
    { path: 'entry-details/:id', component: EntryDetailsComponent, canActivate: [AuthGuard]},//Route to entry-details page
    { path: 'admin/dashboard', component: AdminDashboardComponent, canActivate: [AdminGuard]},//Route to admin-dashboard page
    { path: 'admin/users', component: AdminUsersComponent, canActivate: [AdminGuard]},//Route to admin-users page
    { path: 'admin/verifications', component: AdminVerificationsComponent, canActivate: [AdminGuard]},//Route to admin-verifications page
    { path: 'admin/audit-logs', component: AdminAuditLogsComponent, canActivate: [AdminGuard]}//Route to admin-audit-logs page

];