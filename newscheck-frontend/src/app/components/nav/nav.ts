import { Component } from '@angular/core';
import { TokenStorageService } from '../../services/token-storage-service';
import { AuthService } from '../../services/auth-service';
import { Router } from '@angular/router';
import { LogoutPopupComponent } from "../logout/logout";

@Component({
  selector: 'app-nav',
  imports: [LogoutPopupComponent],
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
