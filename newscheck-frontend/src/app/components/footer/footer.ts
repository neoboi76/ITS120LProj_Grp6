import { Component } from '@angular/core';
import { Router, RouterLink } from "@angular/router";

@Component({
  selector: 'app-footer',
  imports: [RouterLink],
  templateUrl: './footer.html',
  styleUrl: './footer.css'
})
export class FooterComponent {

  constructor(
    private route: Router
  ) {}

  scrollToTop() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

}
