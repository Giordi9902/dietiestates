import { Component, inject } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { UserService } from '../../../services/user/user';

@Component({
  selector: 'app-dashboard-layout',
  imports: [RouterOutlet, RouterLink],
  templateUrl: './dashboard-layout.html',
  styleUrl: './dashboard-layout.css',
  standalone: true,
})
export class DashboardLayout {
  userService = inject(UserService);

  isAdmin(): boolean {
    return this.userService.hasRole('ROLE_ADMIN');
  }
}
