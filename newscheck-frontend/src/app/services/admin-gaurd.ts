import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { TokenStorageService } from './token-storage-service';

/* 
Developed by Group 6:
      Ken Aliling
      Anicia Kaela Bonayao
      Carl Norbi Felonia
      Cedrick Miguel Kaneko
      Dino Alfred T. Timbol (Group Leader)
 */

//Service class for restricting access to admin pages for non-admin users

@Injectable({
  providedIn: 'root'
})
export class AdminGuard {

  constructor(
    private router: Router,
    private tokenStorageService: TokenStorageService
  ) {}

  //Checks if user is admin. If not, goes back to home
  canActivate(): boolean {
    const user = this.tokenStorageService.getUser();
    
    if (user && user.role === 'ADMIN') {
      return true;
    } else {
      this.router.navigate(['/home']);
      return false;
    }
  }
}