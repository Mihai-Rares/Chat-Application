import { Component, OnInit } from '@angular/core';
import {NgForm} from "@angular/forms";
import {UserService} from "../_services/user.service";
import {UserAuthService} from "../_services/user-auth.service";
import {Router} from "@angular/router";
import {LoginComponent} from "../login/login.component";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent extends LoginComponent{

  constructor(userS: UserService, authS: UserAuthService,
              r: Router) {
    super(userS, authS, r);
  }
  register(registerForm: NgForm){
    this.userService.register(registerForm.value).subscribe(
      (response : any) => {
        this.login(registerForm);
      },
      (error)=>{console.log(error)}
    );
  }

}
