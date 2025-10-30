import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginModel } from '../models/login-model';
import { RegisterModel } from '../models/register-model';
import { TokenStorageService } from './token-storage-service';
import { LogoutModel } from '../models/logout-model';
import { first, Observable, tap } from 'rxjs';
import { ResetPasswordModel } from '../models/reset-model';
import { UserModel } from '../models/user-model';

/* 
Developed by Group 6:
      Ken Aliling
      Anicia Kaela Bonayao
      Carl Norbi Felonia
      Cedrick Miguel Kaneko
      Dino Alfred T. Timbol (Group Leader)
 */

//Service class authentication operations

@Injectable({
  providedIn: 'root'
})

export class AuthService  {
  
  isLoggedIn: boolean = false;
  apiUrl: string = "http://localhost:8080"; //Url for backend

  constructor(
    private http: HttpClient,
    private tokenStorageService: TokenStorageService
  ) {}

  //Inserts jwt token in the header for each request
  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': `Bearer ${this.tokenStorageService.getToken()}`,
      'Content-Type': 'application/json'
    });
  }

  //Facilitates user login
  login(email: string, password: string) {
    return this.http.post<LoginModel>(
      `${this.apiUrl}/login`,
      { email, password }
    )
  }

  //Facilitates user sign up
  register(firstName: string, lastName: string, email: string, password: string) {
    return this.http.post<RegisterModel>(
      `${this.apiUrl}/register`,
      { firstName, lastName, email, password }
    )
  }

  //Facilitates user logout
  logout(): Observable<any> {
    
    return this.http.post(
      `${this.apiUrl}/logout`,
       null, { headers: this.getHeaders() }).pipe(
      tap(() => {
        this.tokenStorageService.logout(); 
        this.isLoggedIn = false;
      })
    );
  }

  //Facilitates user password reset
  resetPassword(email: string, oldPassword: string, newPassword: string, token: string) {
    return this.http.put(
      `${this.apiUrl}/reset-password`, 
      { email, oldPassword, newPassword, token}
    )
  }

  //Same as the above code
  forgotPassword(email: string, newPassword: string, token: string) {
    return this.http.put(
      `${this.apiUrl}/forgot-password`, 
      { email, newPassword, token}
    )
  }

  //Request password reset from the login page
  requestForgotPassword(email: string) {
    
    return this.http.post(
      `${this.apiUrl}/request-forgot`, 
      { email }
    )
  }

  //Request password reset from the settings page
  requestPasswordReset(payload: { email: string }): Observable<any> {

    return this.http.post(
      `${this.apiUrl}/request-reset`,
       payload);
    
  }


  //Modifies user profile through the settings form
  settingsForm(firstName: string, lastName: string, id: number, gender: string, country: string, language: string) {

    return this.http.put(
      `${this.apiUrl}/update-user`,
      {firstName, lastName, id, gender, country, language},
      { headers: this.getHeaders() }
    );
  }

  //Returns a particular user
  getUser(id: number): Observable<UserModel> {

    return this.http.get<UserModel>(
      `${this.apiUrl}/get-user/${id}`, 
      { headers: this.getHeaders() }
    )
  }
  
}
