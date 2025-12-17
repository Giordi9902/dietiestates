import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ProfileService } from '../../services/services';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile implements OnInit {
  constructor(private profileService: ProfileService, private router: Router) {}

  activeTab: 'details' | 'password' = 'details';

  detailsModel: any = {
    firstname: '',
    lastname: '',
    email: '',
    birthdate: '',
  };

  passwordModel = {
    currentPassword: '',
    newPassword: '',
    confirmPassword: '',
  };

  loading: boolean = false;
  successMessage: string | null = null;
  errorMessage: string | null = null;

  ngOnInit() {
    this.loadProfileDetails();
  }

  loadProfileDetails() {
    this.profileService.profileDetails().subscribe({
      next: (data: any) => {
        this.detailsModel = {
          firstname: data.firstname,
          lastname: data.lastname,
          email: data.email,
          birthdate: data.dateOfBirth,
        };
      },
      error: (err) => {
        console.error('Failed to load profile details', err);
      },
    });
  }

  setActiveTab(tab: 'details' | 'password') {
    this.activeTab = tab;
    this.successMessage = null;
    this.errorMessage = null;
  }

  passwordsMatch(): boolean {
    return this.passwordModel.newPassword === this.passwordModel.confirmPassword;
  }

  resetPasswordForm(form: any) {
    form.resetForm();
    this.passwordModel = { currentPassword: '', newPassword: '', confirmPassword: '' };
    this.successMessage = null;
    this.errorMessage = null;
  }

  onPasswordSubmit(form: any) {
    this.successMessage = null;
    this.errorMessage = null;
    if (!form.valid) {
      return;
    }
    if (!this.passwordsMatch()) {
      this.errorMessage = 'Le password non coincidono';
      return;
    }
    this.loading = true;
    this.profileService
      .changeMyPassword({
        body: {
          oldPassword: this.passwordModel.currentPassword,
          newPassword: this.passwordModel.newPassword,
        },
      })
      .subscribe({
        next: () => {
          this.loading = false;
          form.resetForm();
          this.successMessage = 'Password aggiornata correttamente';
          this.passwordModel = { currentPassword: '', newPassword: '', confirmPassword: '' };
        },
        error: (err) => {
          this.loading = false;
          this.errorMessage = err?.error?.message;
        },
      });
  }

  onDetailsSubmit(form: any) {
    this.successMessage = null;
    this.errorMessage = null;
    if (!form.valid) {
      return;
    }
    this.loading = true;
    this.profileService.updateProfile({ body: this.detailsModel }).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = 'Profilo aggiornato correttamente';
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err?.error?.message || "Errore durante l'aggiornamento del profilo";
      },
    });
  }
}
