import { CanActivateFn, Router } from '@angular/router';
import { UserService } from '../user/user';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (route, state) => {
  const user = inject(UserService);
  const router = inject(Router);
  if (user.isLoggedIn()) return true;
  router.navigate(['/login'], { queryParams: { redirect: state.url } });
  return false;
};
