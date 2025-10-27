import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { AuthService } from '../../services/auth-service';
import { NgClass } from '@angular/common';

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

  showPassword1: boolean = false;
  showPassword2: boolean = false;

  togglePassword1() {
    this.showPassword1 = !this.showPassword1;
  }

  togglePassword2() {
    this.showPassword2 = !this.showPassword2;
  }


  ngOnInit(): void {

    this.token = this.route.snapshot.queryParamMap.get('token');
    
    this.resetForm = this.fb.group({ 
      email: ['', [Validators.required, Validators.minLength(6)]],
      oldPassword: ['', [Validators.required,Validators.minLength(6)]],
      newPassword: ['', [Validators.required, Validators.minLength(6)]]
    })
  }

  get f() {
    return this.resetForm.controls;
  }

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
