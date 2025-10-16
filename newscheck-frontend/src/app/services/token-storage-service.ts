import { Injectable } from '@angular/core';

const TOKEN_KEY = 'auth-token';
const USER_KEY = 'auth-user';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {

  //signout()

  public saveToken(token: string): void {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY, token);
  }

  public getToken(): string | null {
    console.log(TOKEN_KEY);
    return window.sessionStorage.getItem(TOKEN_KEY);
  }

  public saveUser(id: number): void {
    let strId = id.toString();
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, strId);
  }
  
  public getUser(): number | null {
    const user = window.sessionStorage.getItem(USER_KEY);
    return user ? parseInt(user, 10): null;
  }

  logout(): void {
    window.sessionStorage.clear();
    sessionStorage.clear();
  }

}
