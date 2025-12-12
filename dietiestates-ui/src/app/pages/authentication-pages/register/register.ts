import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RegistrationRequest } from '../../../services/models';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../../services/services';

@Component({
  selector: 'app-register',
  imports: [FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  registrationRequest: RegistrationRequest = {
    email: '',
    firstname: '',
    lastname: '',
    password: '',
  };

  acceptedPrivacy = false;
  errorMsg: Array<string> = [];

  constructor(private router: Router, private authenticationService: AuthenticationService) {}

  register() {
    this.errorMsg = [];
    if (!this.acceptedPrivacy) {
      this.errorMsg.push("Per procedere è necessario accettare l'informativa sulla Privacy");
      return;
    }
    this.authenticationService
      .register({
        body: this.registrationRequest,
      })
      .subscribe({
        next: () => {
          this.router.navigate(['activate']);
        },
        error: (err) => {
          this.errorMsg = err.error.validationErrors;
        },
      });
  }

  login() {
    this.router.navigate(['login']);
  }
}
