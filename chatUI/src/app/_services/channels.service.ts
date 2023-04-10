import { Injectable, OnDestroy, OnInit } from '@angular/core';
import { Channel } from '../_models/channel';
import { Conversation } from '../_models/conversation';
import { Message } from '../_models/message';
import { UserService } from './user.service';
import * as SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
@Injectable({
  providedIn: 'root'
})
export class ChannelsService implements OnInit, OnDestroy{
  private _channels: Channel[] = [];
  private channelMap : Map<string, Channel> = new Map();
  private intervalId ?: any;
  constructor(public userService: UserService) {
    this.ngOnInit();
  }

  ngOnDestroy(): void {
        clearInterval(this.intervalId);
    }

  public get channels(): Channel[] {
    return this._channels;
  }

  public ngOnInit(): void {
      this.computeChannels();

  }
  public addNewConversation(basicChannel : any, name : string){
    let channel : Channel = new Conversation(basicChannel.id, name, []);
    this._channels.unshift(channel);
    this.channelMap.set(basicChannel.id, channel);
  }

  public async computeChannels(){
      let channelMap : Map<string, Channel> = new Map();
      let rslt = await this.userService.getChannels().toPromise();
    let basicChannels : any = rslt;

    console.log(basicChannels);
    for (let basicChannel of basicChannels){
      if(basicChannel.name==""){
        let channel : Channel = new Conversation(basicChannel.id, "Person", []);
        channelMap.set(basicChannel.id, channel);
      }
    }
      console.log(channelMap);
      let messages : any = [];
      messages = await this.userService.getMessages().toPromise();
    console.log(messages);
      for (let m of messages){
        let message = new Message(m.id, new Date(parseInt(m.date)), m.text, m.from);
        let channel = channelMap.get(m.to);
        if(channel != undefined)
          channel.addMessage(message);
      }
      this._channels = [];
      this.channelMap = channelMap;
      for(let channel of channelMap.values()) {
        this._channels.push(channel);
      }
      this._channels.sort((a, b) => {
        if (a.getLastMessage().id < b.getLastMessage().id) {
          return -1;
        }
        if (a.getLastMessage().id > b.getLastMessage().id) {
          return 1;
        }
        return 0;
      });
    }
    getMessages(channelId: string):Message[]{
      let channel = this.channelMap.get(channelId);
      if(channel != undefined){
        return channel.getMessages();
      }
      return [];
    }
}
