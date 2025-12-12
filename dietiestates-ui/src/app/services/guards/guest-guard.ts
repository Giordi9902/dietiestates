import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { UserService } from '../user/user';

export const guestGuard: CanActivateFn = () => {
  const user = inject(UserService);
  const router = inject(Router);
  if (!user.isLoggedIn()) return true;
  router.navigate(['/']);
  return false;
};
