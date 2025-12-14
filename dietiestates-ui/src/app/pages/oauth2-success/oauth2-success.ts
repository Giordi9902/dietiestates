import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TokenService } from '../../services/token/token';

@Component({
  selector: 'app-oauth2-success',
  standalone: true,
  templateUrl: './oauth2-success.html',
  styleUrl: './oauth2-success.css',
})
export class Oauth2Success implements OnInit {
  router = inject(Router);
  route = inject(ActivatedRoute);
  tokenService = inject(TokenService);

  ngOnInit(): void {
    // Tokens are passed in the URL fragment (#) for better security
    this.route.fragment.subscribe((fragment) => {
      if (fragment) {
        const params = new URLSearchParams(fragment);
        const accessToken = params.get('accessToken');
        const refreshToken = params.get('refreshToken');

        if (accessToken && refreshToken) {
          this.tokenService.access_token = accessToken;
          this.tokenService.refresh_token = refreshToken;
          // Navigate to home, the URL fragment will be cleared automatically
          this.router.navigate(['/']);
          return;
        }
      }
      // If no fragment or no tokens, redirect to login
      this.router.navigate(['/login']);
    });
  }
}
