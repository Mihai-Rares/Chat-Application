import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private stompClient: any;

  constructor() {}

  connect() {
    const socket = new SockJS('http://localhost:9090/stream/stomp-endpoint');
    this.stompClient = Stomp.over(socket);
    this.stompClient.connect({}, (frame: any) => {
      this.stompClient.subscribe('stream/topic/greetings', (notification: any) => {
        console.log(notification);
        console.log("????????????????????????");
      });
    });
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
