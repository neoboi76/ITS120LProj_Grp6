import { Component, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NavComponent } from "../components/nav/nav";
import { FooterComponent } from "../components/footer/footer";
import { HttpClient } from '@angular/common/http';
import { RouterLink } from '@angular/router';
import { LoginModel } from '../models/login-model';
import { TokenStorageService } from '../services/token-storage-service';
import { AuthService } from '../services/auth-service';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-settings',
  imports: [ReactiveFormsModule, NavComponent, FooterComponent, RouterLink, NgClass],
  templateUrl: './settings.html',
  styleUrl: './settings.css'
})
export class SettingsComponent {

  user: any;
  successMessage = '';
  errorMessage = '';
  submitted = false;
  isDisabled = false;
  
  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private tokenStorageService: TokenStorageService,
    private authService: AuthService

  ) {
    this.userForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(3)]],
      lastName: ['', [Validators.required, Validators.minLength(3)]],
      gender: ['', [Validators.required]],
      country: ['', [Validators.required, Validators.minLength(4)]],
      language: ['', [Validators.required, Validators.minLength(4)]]
    });

  }

  ngOnInit() {
      this.user = this.tokenStorageService.getUser();
      console.log(this.user.firstName + "lol");
      this.userForm.patchValue(this.user);
      this.userForm.disable();
  }

  userForm: FormGroup;
  formSubmitted = false;

  startEdit() {

    if (!this.userForm) return;

     this.isDisabled = !this.isDisabled;

     if (!this.isDisabled) {
      this.userForm.disable();
     }
     else {
      this.userForm.enable();
     }
     
  }


  onSubmit() {
    this.submitted = true;
    this.errorMessage = '';
    this.errorMessage = '';

    if (this.userForm.invalid) return;

    const {firstName, lastName, gender, country, language} = this.userForm.value;

    const token = this.tokenStorageService.getToken();
    const userId = this.user.id;

    if (!token) {
      this.errorMessage = "You must be logged in to update settings.";
      return;
    }

    this.authService.settingsForm(token, firstName, lastName, gender, country, language, userId).subscribe({
      next: (res) => {
        this.successMessage = "User information updated successfully.";
      },
      error: (err) => {
        this.errorMessage = "Error! Cannot update user information at this time.";
      }
    });

  }


}
