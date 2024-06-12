import { Injectable } from '@angular/core';
import {KeycloakProfile} from "keycloak-js";
import {KeycloakService} from "keycloak-angular";

@Injectable({
  providedIn: 'root'
})
export class AuthServiceService{ public profil: KeycloakProfile | undefined;

  constructor() { }

  updateProfile(profile: KeycloakProfile) {
    this.profil = profile;
  }
}
