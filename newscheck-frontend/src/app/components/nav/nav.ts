import { Component } from '@angular/core';
import { TokenStorageService } from '../../services/token-storage-service';
import { AuthService } from '../../services/auth-service';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-nav',
  imports: [RouterLink],
  templateUrl: './nav.html',
  styleUrl: './nav.css'
})
export class NavComponent {

  scrollToTop() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  constructor(
    private route: Router,
    private authService: AuthService,
    private tokenStorageService: TokenStorageService
  ) {}

  isMenuOpen = false;

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  isAdmin(): boolean {
    return this.tokenStorageService.isAdmin();
  }

  logout() {
    this.authService.logout().subscribe({
      next: (res) => console.log('Logout successful:', res),
      error: (err) => console.error('Logout failed:', err)
    });
    setTimeout(() => this.route.navigate(['/login']), 1000);
  }

  showPopup = false;

  openPopup(event: Event) {
    event.preventDefault();
    this.showPopup = true;
  }

  onClosePopup(confirm: boolean) {
    this.showPopup = false;
    if (confirm) {
      this.logout();
    }
  }
}