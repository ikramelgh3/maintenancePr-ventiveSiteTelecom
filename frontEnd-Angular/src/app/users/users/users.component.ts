import { Component, OnInit } from '@angular/core';
import { PlanningServiceService } from '../../../../service/planning-service.service';
import { User } from '../../models/user';
import { Role } from '../../models/Role';
import {MatSnackBar, MatSnackBarRef} from "@angular/material/snack-bar";
import {DialogService} from "../../service/dialog.service";
import {NewEquipementComponent} from "../../equipements/new-equipement/new-equipement.component";
import {MatDialog} from "@angular/material/dialog";
import {NewUserComponent} from "../new-user/new-user.component";
import {UpdateEquipementComponent} from "../../equipements/update-equipement/update-equipement.component";
import {UpdateUserComponent} from "../update-user/update-user.component";
import {PlanningMaintenanceDTO} from "../../models/PlanningMaintenanceDTO";

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {
  users: User[] = [];
  roles: { [userId: string]: Role[] } = {};
  noResultsFound: boolean = false;
  tot: number = 0;
  p: number = 1;
  isTechnicien: boolean = false;
  user!: User;
  selectedUser: boolean = false;
  searchInput: string = '';

  constructor(private ser: PlanningServiceService,  private dialog: MatDialog ,private dialogService: DialogService, private snackBar: MatSnackBar) {}

  ngOnInit() {
    this.getTotalUser();
    this.getAllUser();
    this.ser._refreshNeeded$.subscribe(()=>{
      this.getTotalUser();
      this.getAllUser();

    })
  }
  getTotalUser() {
    this.ser.getTotalUsers().subscribe((data) => {
      this.tot = data;
    });
  }

  getAllUser() {
    this.ser.getAllUsers().subscribe((data) => {
      this.users = data;
      this.noResultsFound = this.users.length === 0;
      this.loadRoles();
      console.log(this.users);
    });
  }

  loadRoles() {
    this.users.forEach(user => {
      this.ser.getRolesOfUser(user.id).subscribe((roles) => {
        this.roles[user.id] = roles;
      });
    });
  }

  getRoles(userId: string): string {
    return this.roles[userId]?.map(role => role.name).join(', ') || 'No roles';
  }
idd!:string
  getUserById(id:string){
    console.log("user by id")
    this.ser.getUserById(id).subscribe((data)=>{
      this.idd=id
      console.log("id",this.idd)
      this.user = data;
      this.selectedUser = true;
      this.checkUserRole(this.user); // Vérifiez le rôle chaque fois qu'un utilisateur est récupéré
      console.log(this.user);
    })
  }

  checkUserRole(user: User) {
    this.ser.getRolesOfUser(user.id).subscribe((roles) => {
      const roleNames = roles.map(role => role.name);
      this.isTechnicien = roleNames.includes('TECHNICIEN');
    });
  }






  deleteUser(id: string) {
    this.dialogService.openConfirmDialof('Voulez vous supprimer ce utilisateur?').afterClosed()
      .subscribe(res => {
        if (res) {
          this.ser.deleteUser(id).subscribe(
            () => {
              console.log('Planning deleted successfully');
             this.selectedUser= false
              const snackBarRef: MatSnackBarRef<any> = this.snackBar.open('L\'utilisateur a été supprimé', '', {
                duration: 8000,
                horizontalPosition: 'right',
                verticalPosition: 'top',
              });
              snackBarRef.onAction().subscribe(() => {
                snackBarRef.dismiss();
              });
            },
            error => {
              console.error('Error deleting user:', error);
            }
          );
        }
      });
  }

  addUesr(){
    var popup = this.dialog.open(NewUserComponent
      , {
      width: '55%', height: '534px',
      exitAnimationDuration: '500ms',
      data: { title: 'Utilisateur' }
    })
  }


  UpdateUser(){
    const popup = this.dialog.open(UpdateUserComponent, {
      width: '50%', height: '460px',
      exitAnimationDuration: '0',
      data: { id: this.user.id}
    });
    this.ser._refreshNeeded$.subscribe(()=>{
      this.getUserById(this.user.id);
    })
  }


  search(): void {
    if (this.searchInput.trim() !== '') {
      this.ser.getUserByUsername(this.searchInput).subscribe(
        (data: User[]) => {
          this.users = data;
          this.noResultsFound = this.users.length === 0;
        },
        (error) => {
          console.error('Erreur lors de la recherche :', error);
        }
      );
    } else {
      this.getAllUser();
    }
  }

  filterByRole(status: string) {
    this.ser.getUsersByRole(status).subscribe(
      (data) =>{
        this.users = data;
        this.noResultsFound = this.users.length === 0;
      },
      (error)=>
      {
        console.log("Error");
      }
    )
  }
}
