import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { TokenStorageService } from './token-storage-service';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard {

  constructor(
    private router: Router,
    private tokenStorageService: TokenStorageService
  ) {}

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