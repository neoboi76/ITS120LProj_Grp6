import { Component } from '@angular/core';
import { Router, RouterLink } from "@angular/router";
import { TokenStorageService } from '../../services/token-storage-service';

/* 
Developed by Group 6:
      Ken Aliling
      Anicia Kaela Bonayao
      Carl Norbi Felonia
      Cedrick Miguel Kaneko
      Dino Alfred T. Timbol (Group Leader)
 */

//Class that deals with footer operations
@Component({
  selector: 'app-footer',
  imports: [RouterLink],
  templateUrl: './footer.html',
  styleUrl: './footer.css'
})
export class FooterComponent {

  constructor(
    private route: Router,
    private tokenStorageService: TokenStorageService
  ) {}

  scrollToTop() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  //Verifies if logged user is admin or not. If yes, enables link to admin page
  isAdmin(): boolean {
    return this.tokenStorageService.isAdmin();
  }

}
