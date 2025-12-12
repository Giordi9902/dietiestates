import { CanMatchFn, Route, UrlSegment, Router } from '@angular/router';
import { inject } from '@angular/core';
import { UserService } from '../user/user';

export const roleGuard =
  (roles: string[]): CanMatchFn =>
  (_route: Route, _segments: UrlSegment[]) => {
    const user = inject(UserService);
    const router = inject(Router);
    if (user.isLoggedIn() && user.hasAnyRole(roles)) return true;
    router.navigate(['/forbidden']);
    return false;
  };
