import { Component, OnDestroy, OnInit } from '@angular/core';
import { ChannelComponent } from '../channel/channel.component';
import { Channel } from '../_models/channel';
import { ChannelsService } from '../_services/channels.service';
import { UserService } from '../_services/user.service';

@Component({
  selector: 'app-chat-navigation',
  templateUrl: './chat-navigation.component.html',
  styleUrls: ['./chat-navigation.component.css']
})
export class ChatNavigationComponent implements OnInit, OnDestroy {
  public channels : Channel[];
  private intervalId ?: any;
  constructor(private channelService : ChannelsService, private userService : UserService) {
    this.channels = channelService.channels;
    console.log(channelService.channels);
  }
  getUsername():string{
    return this.userService.username;
  }
  ngOnDestroy(): void {
      clearInterval(this.intervalId);
    }

  ngOnInit(): void {
    this.intervalId = setInterval(()=>{this.channels = this.channelService.channels}, 500);
  }
  trackByFn(index: number, item: Channel): string {
    return item.getId();
  }
}
