import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { AuthService } from '../../services/auth-service';
import { NgClass } from '@angular/common';

/* 
Developed by Group 6:
      Ken Aliling
      Anicia Kaela Bonayao
      Carl Norbi Felonia
      Cedrick Miguel Kaneko
      Dino Alfred T. Timbol (Group Leader)
 */

//Service class that deals with resetting password operations (from the settings page)

@Component({
  selector: 'app-reset-password',
  imports: [FormsModule, RouterLink, ReactiveFormsModule, NgClass],
  templateUrl: './reset-password.html',
  styleUrl: './reset-password.css'
})
export class ResetPasswordComponent implements OnInit{

  resetForm!: FormGroup;
  submitted = false;
  errorMessage = '';
  successMessage = '';
  token: any;

  constructor(
    private authService: AuthService,
    private router: Router,
    private fb: FormBuilder,
    private route: ActivatedRoute
  ) {}


  //Toggles password view visible and not
  showPassword1: boolean = false;
  showPassword2: boolean = false;

  togglePassword1() {
    this.showPassword1 = !this.showPassword1;
  }

  togglePassword2() {
    this.showPassword2 = !this.showPassword2;
  }


  //Retrieves token from url parameter 
  ngOnInit(): void {

    this.token = this.route.snapshot.queryParamMap.get('token');
    
    //Validates form
    this.resetForm = this.fb.group({ 
      email: ['', [Validators.required, Validators.minLength(6)]],
      oldPassword: ['', [Validators.required,Validators.minLength(6)]],
      newPassword: ['', [Validators.required, Validators.minLength(6)]]
    })
  }

  get f() {
    return this.resetForm.controls;
  }

  //Submits new password and updates it in the backend
  onSubmit(): void {
    this.submitted = true;
    this.errorMessage = '';
    this.successMessage = '';

    if (this.resetForm.invalid) return;

    const {email, oldPassword, newPassword} = this.resetForm.value;

      this.authService.resetPassword(email, oldPassword, newPassword, this.token).subscribe({
        next: (res)  => {
          console.log(res);
          this.successMessage = 'Reset was a success!';
          setTimeout(() => this.router.navigate(['/login']), 1000);

        },
        error: err => {
          console.log(err);
          this.errorMessage = 'Invalid Reset.';
        }
      });

  }

}
