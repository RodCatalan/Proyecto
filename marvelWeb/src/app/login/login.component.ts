import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../authentication/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  public username: string = '';
  public password: string = '';


  constructor(private authenticationService : AuthenticationService) { }

  ngOnInit(): void {
  }

  login() {

    this.authenticationService.login(this.username, this.password);

  }


}
