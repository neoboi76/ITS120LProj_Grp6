import { Component } from '@angular/core';
import { TokenStorageService } from '../../services/token-storage-service';
import { AuthService } from '../../services/auth-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-nav',
  imports: [],
  templateUrl: './nav.html',
  styleUrl: './nav.css'
})
export class NavComponent {


  constructor(
    private route: Router,
    private authService: AuthService
  ) {}

  isMenuOpen = false;

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  logout() {
    setTimeout(() => this.route.navigate(['/login']), 1000);
    this.authService.logout();
  }
}
