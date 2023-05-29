import { Account } from "./account";
import { Channel } from "./channel";
import { Message } from "./message";

export class Group implements Channel {
  constructor(private id:string, private name : string, private members: Account[], 
              private admins : Account[], private _messages: Message[]) {
    _messages.sort(((a, b) => {
      return a.sent.getTime() - b.sent.getTime();
    }));
  }

  setMessages(value: Message[]) {
    this._messages = value;
  }

  getId(): string {
    return this.id;
  }
  getName(): string {
    return this.name;
  }
  getLastMessage(): Message {
    const message = this._messages[this._messages.length-1];
    if(message != null){
      return message;
    }
    return new Message("", new Date(), "", "");
  }
  getMessages(): Message[] {
    return this._messages;
  }
  addMessage(message : Message): void{
    this._messages.push(message);
  }
  public addMember(account : Account ){
    this.members.push(account);
  }
}
