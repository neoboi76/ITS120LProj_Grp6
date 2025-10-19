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

  goHistory() {
    this.route.navigate(['/history'], { fragment: 'history' });
  }
  goSettings() {
    this.route.navigate(['/settings'], { fragment: 'settings' });
  }
  goHome() {
    this.route.navigate(['/home'], { fragment: 'home' });
  }


}
