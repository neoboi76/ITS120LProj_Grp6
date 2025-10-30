import { Injectable } from '@angular/core';
import { AuthService } from './auth-service';
import { TokenStorageService } from './token-storage-service';
import { Router } from '@angular/router';

/* 
Developed by Group 6:
      Ken Aliling
      Anicia Kaela Bonayao
      Carl Norbi Felonia
      Cedrick Miguel Kaneko
      Dino Alfred T. Timbol (Group Leader)
 */

//Service class restricting access to non-authenticated users

@Injectable({
  providedIn: 'root'
})
export class AuthGuard {

  constructor(
    private authService: AuthService,
    private router: Router,
    private tokenStorageService: TokenStorageService
  ) {}

  //If no JWT token is present, redirect user to login page
  canActivate(): boolean {
    if (this.tokenStorageService.getToken()) {
      return true;
    }
    else {
      this.router.navigate(['/login']);
      return false;
    }
  }

}
