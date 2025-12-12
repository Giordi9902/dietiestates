import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class TokenService {
  private readonly ACCESS_KEY = 'access_token';
  private readonly REFRESH_KEY = 'refresh_token';

  get access_token(): string | null {
    try {
      return localStorage.getItem(this.ACCESS_KEY);
    } catch {
      return null;
    }
  }

  set access_token(value: string | null) {
    try {
      if (value) {
        localStorage.setItem(this.ACCESS_KEY, value);
      } else {
        localStorage.removeItem(this.ACCESS_KEY);
      }
    } catch {}
  }

  get refresh_token(): string | null {
    try {
      return localStorage.getItem(this.REFRESH_KEY);
    } catch {
      return null;
    }
  }

  set refresh_token(value: string | null) {
    try {
      if (value) {
        localStorage.setItem(this.REFRESH_KEY, value);
      } else {
        localStorage.removeItem(this.REFRESH_KEY);
      }
    } catch {}
  }

  clear(): void {
    try {
      localStorage.removeItem(this.ACCESS_KEY);
      localStorage.removeItem(this.REFRESH_KEY);
    } catch {}
  }
}
