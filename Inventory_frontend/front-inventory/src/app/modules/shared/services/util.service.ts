import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';

@Injectable({
  providedIn: 'root'
})
export class UtilService {

  constructor(private keyCloakService: KeycloakService) { }

  getRoles() {
    return this.keyCloakService.getUserRoles();
  }

  isAdmin() {
    let roles = this.getRoles().filter(role => role == 'admin');
    if (roles.length > 0) {
      return true;
    }
    return false;
  }
}
