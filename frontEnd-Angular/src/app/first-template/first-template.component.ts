import {Component, OnInit} from '@angular/core';
import {KeycloakService} from "keycloak-angular";
import {KeycloakProfile} from "keycloak-js";

@Component({
  selector: 'app-first-template',
  templateUrl: './first-template.component.html',
  styleUrl: './first-template.component.css'
})
export class FirstTemplateComponent implements OnInit{
 public  profil!:KeycloakProfile
ngOnInit() {
  if(this.ky.isLoggedIn()){
    this.ky.loadUserProfile().then((profile)=>{this.profil=profile })
  }
}

  constructor(public  ky :KeycloakService) {
  }

  logOut(){
    this.ky.logout(window.window.origin);
  }
}
