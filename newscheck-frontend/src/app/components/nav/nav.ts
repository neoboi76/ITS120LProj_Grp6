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
    this.authService.logout().subscribe({
      next: (res) => console.log('Logout successful:', res),
      error: (err) => console.error('Logout failed:', err)
    });
    setTimeout(() => this.route.navigate(['/login']), 1000);
  }
}
