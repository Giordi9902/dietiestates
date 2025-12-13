import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AdminService } from '../../../../services/services';

@Component({
  selector: 'app-create-new-agent',
  imports: [CommonModule, FormsModule],
  templateUrl: './create-new-agent.html',
  styleUrl: './create-new-agent.css',
})
export class CreateNewAgent {
  constructor(private adminService: AdminService, private router: Router) {}

  model = {
    firstname: '',
    lastname: '',
    email: '',
  };

  loading: boolean = false;
  errorMessage: string | null = null;

  onSubmit(form: any) {
    this.errorMessage = null;
    if (!form.valid) {
      return;
    }
    this.loading = true;
    this.adminService
      .createAgent({
        body: {
          firstname: this.model.firstname,
          lastname: this.model.lastname,
          email: this.model.email,
        },
      })
      .subscribe({
        next: () => {
          this.loading = false;
          this.router.navigate(['/dashboard/agents']);
        },
        error: (err) => {
          this.loading = false;
          this.errorMessage = err?.error?.message;
        },
      });
  }
}
