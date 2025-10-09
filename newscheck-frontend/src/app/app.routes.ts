import { Routes } from '@angular/router';
import { Home } from './home/home';
import { LogIn } from './components/log-in/log-in';
import { Register } from './components/register/register';

export const routes: Routes = [
    { path: '', component: LogIn},
    { path: 'register', component: Register },
    { path: 'home', component: Home }
];
