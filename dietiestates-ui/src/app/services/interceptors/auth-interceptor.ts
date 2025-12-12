import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { TokenService } from '../token/token';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const tokenService = inject(TokenService);
  const access = tokenService.access_token;
  const isAuthEndpoint = req.url.includes('/auth/');
  const isListingsSearch = req.url.includes('/api/v1/listings/search');
  const isListingById = /\/api\/v1\/listings\/[^/]+$/.test(req.url);
  const isPublic = isAuthEndpoint || isListingsSearch || isListingById;

  if (access && !isPublic) {
    const authReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${access}`),
    });
    return next(authReq);
  }

  return next(req);
};
