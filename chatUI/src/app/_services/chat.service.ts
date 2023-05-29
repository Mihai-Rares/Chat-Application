import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private _correspondent = new BehaviorSubject<string>("")

  get correspondent(): string {
    return this._correspondent.getValue();
  }

  set correspondent(value: string) {
    this._correspondent.next(value);
  }
  
  public observeCorrespondent(correspondentListener : any){
    this._correspondent.subscribe(correspondentListener);
  }
  constructor() { }
}
