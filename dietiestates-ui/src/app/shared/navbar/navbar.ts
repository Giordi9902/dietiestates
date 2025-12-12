import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { UserService } from '../../services/user/user';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {
  private userService = inject(UserService);
  private router = inject(Router);

  get isLoggedIn(): boolean {
    return this.userService.isLoggedIn();
  }

  get isUser(): boolean {
    return this.userService.hasRole('ROLE_USER');
  }

  get isAdmin(): boolean {
    return this.userService.hasRole('ROLE_ADMIN');
  }

  get isAgent(): boolean {
    return this.userService.hasRole('ROLE_AGENT');
  }

  get fullName(): string | null {
    return this.userService.fullName;
  }

  logout(): void {
    this.userService.logout();
    this.router.navigate(['/']);
  }
}
