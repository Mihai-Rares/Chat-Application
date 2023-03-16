import { Component, Input, OnInit } from '@angular/core';
import { ChatService } from '../_services/chat.service';

@Component({
  selector: 'app-channel',
  templateUrl: './channel.component.html',
  styleUrls: ['./channel.component.css']
})
export class ChannelComponent implements OnInit {
  @Input()
  public channelName : string = "channel";
  @Input()
  public sent : Date = new Date();
  @Input()
  public text : string = "last message";
  @Input()
  public channel_id : string="";
  constructor(private chatService : ChatService) { }
  setCorrespondent():void{
    this.chatService.correspondent = this.channel_id;
  }
  ngOnInit(): void {
  }

}
