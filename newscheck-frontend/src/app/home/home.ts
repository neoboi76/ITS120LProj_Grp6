import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NavComponent } from "../components/nav/nav";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink, NavComponent],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class HomeComponent {


}
