import { Component, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-new-conversation-search',
  templateUrl: './new-conversation-search.component.html',
  styleUrls: ['./new-conversation-search.component.css']
})
export class NewConversationSearchComponent implements OnInit {
  @Output() usernameSubmit = new EventEmitter<string>();
  username = '';

  constructor() { }
  
  onSubmit() {
    this.usernameSubmit.emit(this.username);
    this.username = '';
  }
  ngOnInit(): void {
    console.log("merge aparent");
  }

}
