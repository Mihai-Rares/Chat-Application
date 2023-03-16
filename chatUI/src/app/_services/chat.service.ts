import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private _correspondent: string = "";

  get correspondent(): string {
    return this._correspondent;
  }

  set correspondent(value: string) {
    this._correspondent = value;
  }

  constructor() { }
}
