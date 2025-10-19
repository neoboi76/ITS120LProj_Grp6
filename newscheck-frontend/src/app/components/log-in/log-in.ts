import { CommonModule, NgClass } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { TokenStorageService } from '../../services/token-storage-service';
import { AuthService } from '../../services/auth-service';
import { LoginModel } from '../../models/login-model';

@Component({
  selector: 'app-log-in',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, RouterLink, NgClass],
  templateUrl: './log-in.html',
  styleUrl: './log-in.css'
})
export class LoginComponent implements OnInit{

  loginForm!: FormGroup;
  submitted = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private tokenStorageService: TokenStorageService,
    private authService: AuthService,
    private router: Router,
    private fb: FormBuilder
  ) {}

  showPassword: boolean = false;

  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  ngOnInit(): void {
    if (this.tokenStorageService.getToken()) {
      this.authService.isLoggedIn = true;
      this.router.navigate(['/home']);
    }

    this.loginForm = this.fb.group({
      email: ['', [Validators.required,
        Validators.minLength(6)]],
      password: ['', [Validators.required,
        Validators.minLength(6)]],
    })
  }

  get f() {
    return this.loginForm.controls;
  }

  onSubmit(): void {
    this.submitted = true;
    this.errorMessage = '';
    this.successMessage = '';

    if (this.loginForm.invalid) return;

    const {email, password} = this.loginForm.value;

    this.authService.login(email, password).subscribe({
      next: (data: LoginModel) => {
        this.tokenStorageService.saveToken(data.token);
        this.tokenStorageService.saveUser(data);
        this.successMessage = 'Login was a success!';
        setTimeout(() => this.router.navigate(['/home']), 1000);
      },
      error: err => {
        console.log(err);
        this.errorMessage = 'Invalid username or password.';
      }
    });

  }

}
