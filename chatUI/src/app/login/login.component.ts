import { Component, OnInit } from '@angular/core';
import {NgForm} from "@angular/forms";
import {UserService} from "../_services/user.service";
import {UserAuthService} from "../_services/user-auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(protected userService: UserService, protected authService: UserAuthService,
              protected router: Router) { }

  ngOnInit(): void {
  }

  login(loginForm: NgForm){
    let subscription = this.userService.login(loginForm.value).subscribe(
      (response : any) => {
        this.authService.setToken(response.access_token);
        this.router.navigate(['/home']);
        },
      (error)=>{console.log(error)}
    );
  }
}

