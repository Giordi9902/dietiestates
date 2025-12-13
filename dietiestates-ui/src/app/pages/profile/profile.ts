import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ProfileService } from '../../services/services';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile {
  constructor(private profileService: ProfileService, private router: Router) {}

  model = {
    currentPassword: '',
    newPassword: '',
    confirmPassword: '',
  };

  loading: boolean = false;
  successMessage: string | null = null;
  errorMessage: string | null = null;

  passwordsMatch(): boolean {
    return this.model.newPassword === this.model.confirmPassword;
  }

  reset(form: any) {
    form.resetForm();
    this.model = { currentPassword: '', newPassword: '', confirmPassword: '' };
    this.successMessage = null;
    this.errorMessage = null;
  }

  onSubmit(form: any) {
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
          oldPassword: this.model.currentPassword,
          newPassword: this.model.newPassword,
        },
      })
      .subscribe({
        next: () => {
          this.loading = false;
          form.resetForm();
          this.successMessage = 'Password aggiornata correttamente';
          this.model = { currentPassword: '', newPassword: '', confirmPassword: '' };
        },
        error: (err) => {
          this.loading = false;
          this.errorMessage = err?.error?.message;
        },
      });
  }
}
