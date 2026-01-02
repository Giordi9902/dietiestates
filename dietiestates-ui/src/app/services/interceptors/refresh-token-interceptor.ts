import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { BehaviorSubject, throwError } from 'rxjs';
import { catchError, filter, switchMap, take } from 'rxjs/operators';
import { TokenService } from '../token/token';
import { AuthenticationService } from '../services/authentication.service';
import { Router } from '@angular/router';

let isRefreshing = false;
const refreshTokenSubject = new BehaviorSubject<string | null>(null);

export const refreshTokenInterceptor: HttpInterceptorFn = (req, next) => {
  const tokenService = inject(TokenService);
  const authService = inject(AuthenticationService);
  const router = inject(Router);

  return next(req).pipe(
    catchError((err: any) => {
      if (err instanceof HttpErrorResponse && err.status === 401) {
        const isAuthEndpoint = req.url.includes('/auth/');
        const isListingsSearch = req.url.includes('/api/v1/listings/search');
        const isListingById = /\/api\/v1\/listings\/[^/]+$/.test(req.url);
        const isPublic = isAuthEndpoint || isListingsSearch || isListingById;

        if (isPublic) {
          return throwError(() => err);
        }
        const refreshToken = tokenService.refresh_token;
        if (!refreshToken) {
          tokenService.clear();
          router.navigate(['login']);
          return throwError(() => err);
        }

        if (!isRefreshing) {
          isRefreshing = true;
          refreshTokenSubject.next(null);

          return authService.refresh({ body: { refresh_token: refreshToken } }).pipe(
            switchMap((res: any) => {
              const newAccess = res?.jwt_token ?? null;
              const newRefresh = res?.refresh_token ?? null;
              tokenService.access_token = newAccess;
              tokenService.refresh_token = newRefresh;
              isRefreshing = false;
              refreshTokenSubject.next(newAccess);

              const authReq = req.clone({
                headers: req.headers.set('Authorization', `Bearer ${newAccess}`),
              });
              return next(authReq);
            }),
            catchError((refreshErr) => {
              isRefreshing = false;
              tokenService.clear();
              router.navigate(['login']);
              return throwError(() => refreshErr);
            })
          );
        }

        return refreshTokenSubject.pipe(
          filter((t) => t != null),
          take(1),
          switchMap((token) => {
            const authReq = req.clone({
              headers: req.headers.set('Authorization', `Bearer ${token}`),
            });
            return next(authReq as any);
          })
        );
      }

      return throwError(() => err);
    })
  );
};
