import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-log-in',
  imports: [FormsModule, RouterLink],
  templateUrl: './log-in.html',
  styleUrl: './log-in.css'
})
export class LogIn {
form: any;
onSubmit() {
throw new Error('Method not implemented.');
}

}
