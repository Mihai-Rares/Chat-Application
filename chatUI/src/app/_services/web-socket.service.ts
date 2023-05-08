import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { UserAuthService } from './user-auth.service';
import { throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private stompClient: any;

  constructor(private authService : UserAuthService) {}

  connect() {
    const token : String | null = this.authService.getToken();
    if(token != null){
      console.log(token);
      const socket = new SockJS(`http://localhost:9090/stream/message-flux?token=${token}`);
      this.stompClient = Stomp.over(socket);
      this.stompClient.connect({}, (frame: any) => {
        this.stompClient.subscribe('stream/topic/greetings', (notification: any) => {
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
    if (this.stompClient !== null) {
      this.stompClient.disconnect();
    }
  }

  sendMessage(message: string) {
    this.stompClient.send('/stream/app/hello', {}, message);
  }
}
