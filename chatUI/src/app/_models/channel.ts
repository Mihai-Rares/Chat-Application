import { Message } from "./message";

export interface Channel {
  getId():string;
  getName():string;
  getLastMessage():Message;
  getMessages():Message[];
  setMessages(messages: Message[]):void;
  addMessage(message: Message):void;
}
