import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  get username(): string {
    let user = sessionStorage.getItem('username');
    if(user!=null){
      return user;
    }

    return "";
  }
  PATH_OF_API: string="http://localhost:9090/api";
  requestHeader = new HttpHeaders({ 'No-Auth': 'True' });
  constructor(private httpClient: HttpClient) {}

  public login(loginData: any){
    console.log("post login message");
    sessionStorage.setItem('username', loginData.username);
    return this.httpClient.post(this.PATH_OF_API+"/login", loginData, {headers: this.requestHeader});
  }

  public register(registerData: any){
    return this.httpClient.post(this.PATH_OF_API+"/register", registerData, {headers: this.requestHeader});
  }

  public getChannels(){
    // console.log("get channels request\n");
    return this.httpClient.get(this.PATH_OF_API + "/channels");
  }
  public getMessages(){
    // console.log("get message request\n");
    return this.httpClient.get(this.PATH_OF_API + "/messages");
  }
  public sendMessage(messageData: any){
    console.log("send message request\n");
    return this.httpClient.post(this.PATH_OF_API+"/sendMessage", messageData);
  }
  public startConversation(username: string){
    return this.httpClient.post(this.PATH_OF_API+"/startConversation", username);
  }

}
