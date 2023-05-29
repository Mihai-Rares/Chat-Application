import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-message-view',
  templateUrl: './message-view.component.html',
  styleUrls: ['./message-view.component.css']
})
export class MessageViewComponent implements OnInit {
  @Input()
  public textInput: string="text";
  @Input()
  public from: string="";
  constructor() { }

  ngOnInit(): void {
  }

}
