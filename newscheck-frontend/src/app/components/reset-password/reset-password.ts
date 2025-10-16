import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-reset-password',
  imports: [FormsModule, RouterLink],
  templateUrl: './reset-password.html',
  styleUrl: './reset-password.css'
})
export class ResetPasswordComponent {
form: any;
onSubmit() {
throw new Error('Method not implemented.');
}

}
