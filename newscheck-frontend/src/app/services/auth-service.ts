import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginModel } from '../models/login-model';
import { RegisterModel } from '../models/register-model';
import { TokenStorageService } from './token-storage-service';
import { LogoutModel } from '../models/logout-model';
import { Observable, tap } from 'rxjs';
import { ResetPasswordModel } from '../models/reset-model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  
  isLoggedIn: boolean = false;
  
  constructor(
    private http: HttpClient,
    private tokenStorageService: TokenStorageService
  ) {}

  login(email: string, password: string) {
    return this.http.post<LoginModel>(
      "http://localhost:8080/login",
      { email, password }
    )
  }

  register(firstName: string, lastName: string, email: string, password: string) {
    return this.http.post<RegisterModel>(
      "http://localhost:8080/register",
      { firstName, lastName, email, password }
    )
  }


  logout(): Observable<any> {
    const token = this.tokenStorageService.getToken(); 
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.post("http://localhost:8080/logout", null, { headers }).pipe(
      tap(() => {
        this.tokenStorageService.logout(); 
        this.isLoggedIn = false;
      })
    );
  }

  resetPassword(email: string, oldPassword: string, newPassword: string) {
    return this.http.put("http://localhost:8080/reset-password", 
      { email, oldPassword, newPassword }
    )
  }

  
}
