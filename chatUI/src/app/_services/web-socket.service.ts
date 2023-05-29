import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { UserAuthService } from './user-auth.service';
import {BehaviorSubject, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private stompClient: any;
  private connectionStatus = new BehaviorSubject<boolean>(false);
  private sessionId = "";
  constructor(private authService : UserAuthService) {
    this.connect();
  }

  connect() {
    const token : String | null = this.authService.getToken();
    if(token != null){
      console.log(token);
      const socket = new SockJS(`http://localhost:9090/stream/message-flux?token=${token}`);
      this.stompClient = Stomp.over(socket);
      this.stompClient.connect({}, (frame: any) => {
        let url = this.stompClient.ws._transport.url;
        url = url.replace(
          "ws://localhost:9090/stream/message-flux",  "");
        url = url.replace(/^\/[0-9]+\//, "");
        url = url.replace("/websocket", "");
        url = url.replace(`?token=${token}`, "");
        console.log("Your current session is: " + url);
        this.sessionId = url;
        this.connectionStatus.next(true);
      });
    } else{
      throwError(()=>"ERROR WEBSOCKET CONNECTION");
    }
    //this.sendMessage("Hello");
  }

  disconnect() {
    if (this.stompClient != null) {
      this.stompClient.disconnect();
    }
  }
  subscribeToTopic(topic : string, listener : any){
    this.connectionStatus.subscribe(connected => {
      if (connected) {
        this.stompClient.subscribe(`/stream/user${topic}`+ '-user' + this.sessionId, listener);
      }
    });

  }
  sendMessage(message: string) {
    if (this.stompClient != null) {
      this.stompClient.send('/stream/app/hello', {}, message);
    }
  }
}
