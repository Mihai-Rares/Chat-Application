import { Channel } from "./channel";
import { Message } from "./message";

export class Conversation implements Channel {
  constructor(private id:string, private correspondent: string, private _messages: Message[]) {
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
        return this.correspondent;
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
}
