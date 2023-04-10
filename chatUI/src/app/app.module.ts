import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import {RouterModule, RouterOutlet} from "@angular/router";
import { AppRoutingModule } from './app-routing.module';
import {FormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {AuthGuard} from "./_auth/auth.guard";
import {AuthInterceptor} from "./_auth/auth.interceptor";
import { ChatInputComponent } from './chat-input/chat-input.component';
import { ChatViewComponent } from './chat-view/chat-view.component';
import { MessageViewComponent } from './message-view/message-view.component';
import { ChatNavigationComponent } from './chat-navigation/chat-navigation.component';
import { ChannelComponent } from './channel/channel.component';
import { NewConversationComponent } from './new-conversation/new-conversation.component';
import { NewConversationSearchComponent } from './new-conversation-search/new-conversation-search.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    ChatInputComponent,
    ChatViewComponent,
    MessageViewComponent,
    ChatNavigationComponent,
    ChannelComponent,
    NewConversationComponent,
    NewConversationSearchComponent
  ],
  imports: [
    BrowserModule,
    RouterOutlet,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    RouterModule
  ],
  providers: [
    AuthGuard,
    {
      provide : HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
