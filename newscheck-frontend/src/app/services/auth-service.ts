import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginModel } from '../models/login-model';
import { RegisterModel } from '../models/register-model';
import { TokenStorageService } from './token-storage-service';
import { LogoutModel } from '../models/logout-model';
import { first, Observable, tap } from 'rxjs';
import { ResetPasswordModel } from '../models/reset-model';
import { UserModel } from '../models/user-model';

@Injectable({
  providedIn: 'root'
})

export class AuthService  {
  
  isLoggedIn: boolean = false;
  apiUrl: string = "http://localhost:8080";

  constructor(
    private http: HttpClient,
    private tokenStorageService: TokenStorageService
  ) {}

  login(email: string, password: string) {
    return this.http.post<LoginModel>(
      this.apiUrl + "/login",
      { email, password }
    )
  }

  register(firstName: string, lastName: string, email: string, password: string) {
    return this.http.post<RegisterModel>(
      this.apiUrl + "/register",
      { firstName, lastName, email, password }
    )
  }


  logout(): Observable<any> {
    
    const token = this.tokenStorageService.getToken(); 
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.post(this.apiUrl + "/logout", null, { headers }).pipe(
      tap(() => {
        this.tokenStorageService.logout(); 
        this.isLoggedIn = false;
      })
    );
  }

  resetPassword(email: string, oldPassword: string, newPassword: string, token: string) {
    return this.http.put(this.apiUrl + "/reset-password", 
      { email, oldPassword, newPassword, token}
    )
  }

  requestPasswordReset(payload: { email: string }): Observable<any> {

    return this.http.post(this.apiUrl + '/request-reset', payload);
    
  }


  settingsForm(firstName: string, lastName: string, id: number, gender: string, country: string, language: string) {

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.tokenStorageService.getToken()}`,
      'Content-Type': 'application/json'
    });

    return this.http.put(this.apiUrl + "/update-user",
      {firstName, lastName, id, gender, country, language},
      { headers }
    );
  }

  getUser(id: number): Observable<UserModel> {

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${this.tokenStorageService.getToken()}`,
      'Content-Type': 'application/json'
    });

    return this.http.get<UserModel>(this.apiUrl + "/get-user/" + id, 
      { headers }
    )
  }
  
}
