import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CodeInputModule } from 'angular-code-input';
import { AuthenticationService } from '../../../services/services';
@Component({
  selector: 'app-activate',
  imports: [CodeInputModule],
  templateUrl: './activate.html',
  styleUrl: './activate.css',
})
export class Activate {
  message: string = '';
  isOkay: boolean = true;
  submitted: boolean = false;

  constructor(private router: Router, private authService: AuthenticationService) {}

  onCodeCompleted(otp: string) {
    this.confirmAccount(otp);
  }

  redirectToLogin() {
    this.router.navigate(['login']);
  }

  private confirmAccount(otp: string) {
    this.authService
      .confirm({
        otp,
      })
      .subscribe({
        next: () => {
          this.message =
            'Il tuo account è stato attivato con successo.\n Ora puoi procedere con il login.';
          this.submitted = true;
        },
        error: (err) => {
          this.message = "L'OTP è scaduto";
          this.submitted = true;
          this.isOkay = false;
        },
      });
  }
}
