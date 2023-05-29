import { Injectable, OnDestroy, OnInit } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Account } from '../_models/account';
import { Channel } from '../_models/channel';
import { Conversation } from '../_models/conversation';
import { Group } from '../_models/group';
import { Message } from '../_models/message';
import { UserService } from './user.service';
import { WebSocketService } from './web-socket.service';

@Injectable({
  providedIn: 'root'
})
export class ChannelsService{
  private _channels : Channel[] = [];
  private channelsObserver = new BehaviorSubject<Channel[]>(this._channels);
  private channelMap : Map<any, Channel> = new Map();
  private intervalId ?: any;
  private channelsStatus = new BehaviorSubject<boolean>(false);

  constructor(public userService: UserService, private socketService : WebSocketService) {
    this.computeChannels();
    this.channelsStatus.subscribe(connected => {
      if (connected) {
        this.handleMessages();
        this.handleNotifications();
      }
    });

  }

  private handleMessages(){
    this.socketService.subscribeToTopic('/queue/new-message', (notification : any) =>{
      const message : any = JSON.parse(notification.body);
      const channel = this.channelMap.get(Number(message.to));
      if(channel != null){
        channel.addMessage(
          new Message(message.id, new Date(parseInt(message.date))
            , message.text, message.from));
        this.channelsObserver.next(this._channels);
      }
      else{
        throw new Error("message to non-registered channel");
      }
    });
  }

  private handleNotifications(){
    this.socketService.subscribeToTopic('/queue/notifications', (update : any) =>{
      const notification : any = JSON.parse(update.body);
      switch (notification.type) {
        case "NEW_CONVERSATION":{
          this.handleNewConversation(notification.content);
          this.channelsObserver.next(this._channels);
          break;
        }
        case "NEW_GROUP":{
          let basicChannel = notification.content;
          let channel : Channel = new Group(basicChannel.id, basicChannel.name,
            basicChannel.members, basicChannel.admins, []);
          this.channelMap.set(basicChannel.id, channel);
          this._channels.push(channel);
          this.channelsObserver.next(this._channels);
          break;
        }
        case "NEW_GROUP_MEMBER":{
          let newMember : Account = notification.content.account;
          let channel_id = notification.content.channel_id;
          let group : Group = this.channelMap.get(channel_id) as Group;
          group.addMember(newMember);
          break;
        }
        default:
          break;
      }

    });
  }
  private handleNewConversation(content : any){
    let correspondent : Account;
    let basicChannel = content;
    correspondent =
      basicChannel.members[0].username == this.userService.username?
        basicChannel.members[1]:basicChannel.members[0];
    //console.log(basicChannel.id);
    let channel : Channel = new Conversation(basicChannel.id, correspondent, []);
    this.channelMap.set(basicChannel.id, channel);
    this._channels.push(channel);
  }

  public get channels(): Channel[] {
    return this._channels;
  }
  public isGroup(channelId : string) : boolean{
    let channel = this.channelMap.get(channelId);
    if(channel != undefined){
      return channel.getName()!="";
    }
    return false;
  }

  public async computeChannels(){
      let channelMap : Map<string, Channel> = new Map();
      console.log("computing channels");
      let rslt = await this.userService.getChannels().toPromise();
      console.log("computed channels");
    let basicChannels : any = rslt;
    for (let basicChannel of basicChannels){
      if(!basicChannel.group){
        let correspondent : Account;
        correspondent =
          basicChannel.members[0].username == this.userService.username?
            basicChannel.members[1]:basicChannel.members[0];
        //console.log(basicChannel.id);
        let channel : Channel = new Conversation(basicChannel.id, correspondent, []);
        channelMap.set(basicChannel.id, channel);
      }
      else{
        let channel : Channel = new Group(basicChannel.id, basicChannel.name,
          basicChannel.members, basicChannel.admins, []);
        channelMap.set(basicChannel.id, channel);
      }
    }
      // console.log(channelMap);
      let messages : any = [];
      messages = await this.userService.getMessages().toPromise();
    // console.log(messages);
      for (let m of messages){
        let message = new Message(m.id, new Date(parseInt(m.date)), m.text, m.from.username);
        let channel = channelMap.get(m.to.id);
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
      this.channelsObserver.next(this._channels);
    this.channelsStatus.next(true);
    }

    getMessages(channelId: string):Message[]{
      let channel = this.channelMap.get(channelId);
      if(channel != undefined){
        return channel.getMessages();
      }
      return [];
    }
    public subscribeToChannelUpdates(channelsListener : any){
      this.channelsObserver.subscribe(channelsListener);
    }
}
