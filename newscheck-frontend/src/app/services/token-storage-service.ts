import { Injectable } from '@angular/core';
import { LoginModel } from '../models/login-model';
import { Observable } from 'rxjs';

/* 
Developed by Group 6:
      Ken Aliling
      Anicia Kaela Bonayao
      Carl Norbi Felonia
      Cedrick Miguel Kaneko
      Dino Alfred T. Timbol (Group Leader)
 */

//Service class handling user and jwt token storage and retrieval
//as well as user information storage and retrieval

const TOKEN_KEY = 'auth-token';
const USER_KEY = 'auth-user';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {

  //Saves JWT token in session storage
  public saveToken(token: string): void {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY, token);
  }

  //Retrieves logged user's JWT token from session storage
  public getToken(): string | null {
    return window.sessionStorage.getItem(TOKEN_KEY);
  }

  //Saves logged user to session storage
  public saveUser(user: LoginModel): void {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
  }
  
  //Retrieves logged user from session storage
  public getUser(): any {
    const user = sessionStorage.getItem(USER_KEY);
    return user ? JSON.parse(user) : null;
  }

  //Retrieves logged user's id from sessions storage
  public getUsrId(): number {
    const user = this.getUser();
    return user ? user.id : 0;
  }

  //Retrieves logged user's email from sessions storage
  public getUserEmail(): string {
    const user = this.getUser();
    return user ? user.email : "";
  }

  //Check's if a logged user is admin or not
  public isAdmin(): boolean {
    const user = this.getUser();
    return user && user.role === 'ADMIN';
  }

  //Logout a logged user by clearing session storage the JWT token and user information
  logout(): void {
    window.sessionStorage.clear();
    sessionStorage.clear();
  }
}