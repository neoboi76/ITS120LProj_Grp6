import { Injectable } from '@angular/core';
import { LoginModel } from '../models/login-model';
import { Observable } from 'rxjs';

const TOKEN_KEY = 'auth-token';
const USER_KEY = 'auth-user';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {


  public saveToken(token: string): void {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY, token);
  }

  public getToken(): string | null {
    return window.sessionStorage.getItem(TOKEN_KEY);
  }

  public saveUser(user: LoginModel): void {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
  }
  
  public getUser(): Observable<LoginModel> {
    const user = JSON.parse(sessionStorage.getItem(USER_KEY) || '{}');
    return user;
  }

  public getUsrId() {
    const user = JSON.parse(sessionStorage.getItem(USER_KEY) || '{}');
    return user.id;
  }

  logout(): void {
    window.sessionStorage.clear();
    sessionStorage.clear();
  }

}
