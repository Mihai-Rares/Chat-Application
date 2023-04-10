import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import { ChannelsService } from './channels.service';

@Injectable({
  providedIn: 'root'
})
export class WebSocketServiceService {
  private client: Client;
  private channelsService : ChannelsService;
    constructor(channelsService : ChannelsService) {
      this.channelsService = channelsService;
      this.client = new Client({
        webSocketFactory: () => new SockJS('http://localhost:9090/chat'),
        debug: (str) => {
          console.log(str);
        },
        onConnect: () => {
          this.client.subscribe('/user/newConversation', message => {
            this.channelsService.addNewConversation(message, "Robert Brown");
          });
          // this.client.subscribe('/topic/your-second-topic', message => {
          //   console.log('Received message on second topic: ', message);
          // });
        }
      });
      this.client.activate();
    }

  public subscribe(destination: string, callback: (message: any) => void) {
      this.client.subscribe(destination, message => {
        callback(message);
      });
    }
}
