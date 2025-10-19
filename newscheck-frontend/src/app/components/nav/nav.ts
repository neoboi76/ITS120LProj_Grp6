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

  goHistory() {
    this.route.navigate(['/history'], { fragment: 'history' });
  }
  goSettings() {
    this.route.navigate(['/settings'], { fragment: 'settings' });
  }
  goHome() {
    this.route.navigate(['/home'], { fragment: 'home' });
  }


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
