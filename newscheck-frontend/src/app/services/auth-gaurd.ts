import { Injectable } from '@angular/core';
import { AuthService } from './auth-service';
import { TokenStorageService } from './token-storage-service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGaurd {

  constructor(
    private authService: AuthService,
    private router: Router,
    private tokenStorageService: TokenStorageService
  ) {}

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
