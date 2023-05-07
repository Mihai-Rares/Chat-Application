import { Component, NgModule, OnDestroy, OnInit } from '@angular/core';
import { MessageViewComponent } from '../message-view/message-view.component';
import { Message } from '../_models/message';
import { ChannelsService } from '../_services/channels.service';
import { ChatService } from '../_services/chat.service';
import { UserService } from '../_services/user.service';
import { WebSocketService } from '../_services/web-socket.service';

@Component({
  selector: 'app-chat-view',
  templateUrl: './chat-view.component.html',
  styleUrls: ['./chat-view.component.css']
})
export class ChatViewComponent implements OnInit, OnDestroy {
  public messages : Message[] = [];
  public username : string;
  private intervalId ?: any;
  constructor(private userService : UserService,
              private channelsService : ChannelsService,
              private chatService : ChatService,
              private socketService : WebSocketService) {
    this.username = userService.username;
  }

  ngOnDestroy(): void {
    clearInterval(this.intervalId);
    }

  ngOnInit(): void {
    this.socketService.connect();
    this.intervalId = setInterval(
      ()=> {
        this.messages =  this.channelsService.getMessages(this.chatService.correspondent);
        this.set
      },
      500);

  }
  trackByFn(index: number, item: Message): string {
    return item.id;
  }
}
