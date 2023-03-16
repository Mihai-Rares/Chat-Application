import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserAuthService {

  constructor() { }

  public setToken(jwtToken: string) {
    sessionStorage.setItem('token', jwtToken);
  }

  public getToken(): string | null{
    return sessionStorage.getItem('token');
  }

  public clear() {
    sessionStorage.clear();
  }

  public isLoggedIn(): boolean {
    return this.getToken()!=null;
  }
}
