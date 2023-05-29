import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ChatService } from '../_services/chat.service';
import { UserService } from '../_services/user.service';

@Component({
  selector: 'app-chat-input',
  templateUrl: './chat-input.component.html',
  styleUrls: ['./chat-input.component.css']
})
export class ChatInputComponent implements OnInit {

  constructor(protected userService: UserService, private chatService : ChatService) { }

  ngOnInit(): void {
  }

  sendMessage(text: NgForm){
    let message : any = {text: text.value.text, to : this.chatService.correspondent};
    console.log("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    console.log(message);
    let subscription =
      this.userService.sendMessage(message).subscribe(
      (response : any) => {},
      (error)=>{console.log(error)}
    );
  }
  clearText(event: Event) {
    const target = event.target as HTMLInputElement;
    target.value = '';
  }
}
