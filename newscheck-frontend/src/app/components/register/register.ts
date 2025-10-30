import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth-service';
import { RegisterModel } from '../../models/register-model';
import { TokenStorageService } from '../../services/token-storage-service';
import { NgClass } from '@angular/common';

/* 
Developed by Group 6:
      Ken Aliling
      Anicia Kaela Bonayao
      Carl Norbi Felonia
      Cedrick Miguel Kaneko
      Dino Alfred T. Timbol (Group Leader)
 */

//Class that deals with register operations
@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, RouterLink, NgClass],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class RegisterComponent implements OnInit{

  registerForm!: FormGroup;
  submitted = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private authService: AuthService,
    private tokenStorageService: TokenStorageService
  ) {}


  //Validates form
  ngOnInit(): void {
    this.registerForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(3)]],
      lastName: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.minLength(6)]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    })
  }

  get f() {
    return this.registerForm.controls;
  }

  //Toggles password view visible and not
  showPassword: boolean = false;

  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  //Submit user register request to the backend
  onSubmit(): void {
    this.submitted = true;
    this.errorMessage = '';
    this.successMessage = '';

    if (this.registerForm.invalid) true;

    const {firstName, lastName, email, password} = this.registerForm.value;

    this.authService.register(firstName, lastName, email, password).subscribe({
      next: (data: RegisterModel) => {
        this.successMessage = 'Account was created successfully!';
        setTimeout(() => this.router.navigate(['/login']), 1000);
      },
      error: err => {
        console.log(err);
        this.errorMessage = 'Invalid sign up details.';
      }
    });

  }

}

