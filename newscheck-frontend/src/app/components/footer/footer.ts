import { Component } from '@angular/core';
import { Router, RouterLink } from "@angular/router";
import { TokenStorageService } from '../../services/token-storage-service';

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

  isAdmin(): boolean {
    return this.tokenStorageService.isAdmin();
  }

}
