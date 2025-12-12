import { inject, Injectable } from '@angular/core';
import { AuthenticationService } from '../services/authentication.service';
import { jwtDecode } from 'jwt-decode';
import { TokenService } from '../token/token';

export interface JwtPayload {
  fullName: string;
  sub: string;
  authorities: string[];
  exp: number;
  iat: number;
}

@Injectable({
  providedIn: 'root',
})
export class UserService {
  authService = inject(AuthenticationService);
  tokenService = inject(TokenService);

  private decode(token: string | null): JwtPayload | null {
    if (!token) return null;
    try {
      return jwtDecode<JwtPayload>(token);
    } catch {
      return null;
    }
  }

  get payload(): JwtPayload | null {
    return this.decode(this.tokenService.access_token);
  }

  private isExpired(token: string | null): boolean {
    const p = this.decode(token);
    if (!p) return true;
    return Date.now() >= p.exp * 1000;
  }

  isLoggedIn(): boolean {
    const at = this.tokenService.access_token;
    return !!at && !this.isExpired(at);
  }

  hasRole(role: string): boolean {
    const roles = this.payload?.authorities ?? [];
    return roles.includes(role);
  }

  hasAnyRole(roles: string[]): boolean {
    const userRoles = this.payload?.authorities ?? [];
    return roles.some((r) => userRoles.includes(r));
  }

  get fullName(): string | null {
    return this.payload?.fullName ?? null;
  }

  get email(): string | null {
    return this.payload?.sub ?? null;
  }

  logout(): void {
    this.tokenService.clear();
  }
}
