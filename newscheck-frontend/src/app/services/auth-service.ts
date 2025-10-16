import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginModel } from '../models/login-model';
import { RegisterModel } from '../models/register-model';
import { TokenStorageService } from './token-storage-service';
import { LogoutModel } from '../models/logout-model';

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



  logout() {

    const token = this.tokenStorageService.getToken(); 
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    this.http.post<LogoutModel>("http://localhost:8080/logout", null, { headers });

    this.tokenStorageService.logout();

    this.isLoggedIn = false;
  }
  
}
