import { Component } from '@angular/core';
import { TokenStorageService } from '../../services/token-storage-service';
import { AuthService } from '../../services/auth-service';
import { Router, RouterLink } from '@angular/router';

/* 
Developed by Group 6:
      Ken Aliling
      Anicia Kaela Bonayao
      Carl Norbi Felonia
      Cedrick Miguel Kaneko
      Dino Alfred T. Timbol (Group Leader)
 */

//Class that handles navigation bar operations

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

  //Controls the collapsable menu when screen size is reduced
  isMenuOpen = false;

  toggleMenu() {
    this.isMenuOpen = !this.isMenuOpen;
  }

  //Checks if logged user is admin or not. If yes,
  //the admin page link is enabled
  isAdmin(): boolean {
    return this.tokenStorageService.isAdmin();
  }

  //Logout method
  logout() {
    this.authService.logout().subscribe({
      next: (res) => console.log('Logout successful:', res),
      error: (err) => console.error('Logout failed:', err)
    });
    setTimeout(() => this.route.navigate(['/login']), 1000);
  }

  showPopup = false;

  //Opens popup when user clicks on log out link
  openPopup(event: Event) {
    event.preventDefault();
    this.showPopup = true;
  }

  //Closes abovementioned popup
  onClosePopup(confirm: boolean) {
    this.showPopup = false;
    if (confirm) {
      this.logout();
    }
  }
}