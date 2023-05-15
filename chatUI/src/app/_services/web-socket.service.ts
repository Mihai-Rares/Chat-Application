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
        this.connectionStatus.next(true);
        this.stompClient.subscribe('stream/user/test', (notification: any) => {
          console.log(notification);
          console.log("????????????????????????");
        });
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
        this.stompClient.subscribe(`stream/user/${topic}`, listener);
      }
    });
      
  }
  sendMessage(message: string) {
    if (this.stompClient != null) {
      this.stompClient.send('/stream/app/hello', {}, message);
    }
  }
}
