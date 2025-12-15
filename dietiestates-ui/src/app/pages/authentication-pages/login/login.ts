import { Component, inject } from '@angular/core';
import { AuthenticationRequest } from '../../../services/models';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../../services/services';
import { TokenService } from '../../../services/token/token';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  authRequest: AuthenticationRequest = { email: '', password: '' };
  errorMsg: Array<string> = [];
  successMsg: string | null = null;
  router = inject(Router);
  authService = inject(AuthenticationService);
  tokenService = inject(TokenService);

  login() {
    this.errorMsg = [];
    this.authService
      .authenticate({
        body: this.authRequest,
      })
      .subscribe({
        next: (res) => {
          this.tokenService.access_token = res.jwt_token as string;
          this.tokenService.refresh_token = res.refresh_token as string;
          this.router.navigate(['']);
        },
        error: (err) => {
          this.errorMsg = [];
          if (err.error?.violations) {
            this.errorMsg = err.error.violations.map((v: any) => v.message);
          } else if (err.error?.message) {
            this.errorMsg.push(err.error.message);
          } else {
            this.errorMsg.push('Credenziali non valide o si è verificato un errore.');
          }
        },
      });
  }

  register() {
    this.router.navigate(['register']);
  }
}
