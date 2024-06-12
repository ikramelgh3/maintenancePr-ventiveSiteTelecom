import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from "@angular/material/dialog";
import { NewEquipementComponent } from "../../equipements/new-equipement/new-equipement.component";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { PlanningServiceService } from "../../../../service/planning-service.service";
import { MatSnackBar } from "@angular/material/snack-bar";
import { User } from "../../models/user";
import {CentreTechnique} from "../../models/centreTechnique";

@Component({
  selector: 'app-update-user',
  templateUrl: './update-user.component.html',
  styleUrls: ['./update-user.component.css']
})
export class UpdateUserComponent implements OnInit {
  updateForm!: FormGroup;
  roles: string[] = ["RESPONSABLE DE MAINTENANCE", "TECHNICIEN", "INJECTEUR", "REPORTING"];
  type: string[] = ["Interne", "Externe"];
  user!: User;
  id!: string;
  cites:string[]  = [
    "Agadir",
    "Al Hoceima",
    "Beni Mellal",
    "Casablanca",
    "Dakhla",
    "El Jadida",
    "Essaouira",
    "Fès",
    "Kenitra",
    "Laâyoune",
    "Marrakech",
    "Meknès",
    "Mohammedia",
    "Nador",
    "Oujda",
    "Rabat",
    "Salé",
    "Tanger",
    "Taza",
    "Tetouan"
  ];

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { id: number },
    private dialog: MatDialog,
    private ref: MatDialogRef<NewEquipementComponent>,
    private fb: FormBuilder,
    private ser: PlanningServiceService,
    private snackBar: MatSnackBar
  ) {
    ref.disableClose = true;
  }
  centr: string[] = [];

  CentreTechniques!:CentreTechnique[]
  getCentreTec() {
    this.ser.getAllCT().subscribe((data: CentreTechnique[]) => {
      this.CentreTechniques = data;
      this.centr = data.map(centre => centre.name);  // Extraire les noms des centres techniques
    });
  }
  ngOnInit() {
    this.initForm();
    this.getCentreTec()
    this.id = this.data.id.toString();
    console.log(this.id);
    console.log(this.data);

    this.ser.getUserById(this.id).subscribe((data) => {
      this.user = data;
      console.log("user", this.user);

      // Nettoyage et assignation du rôle
      const cleanedRoleName = this.getRoleValue(this.user.roleName);

      console.log("role", cleanedRoleName)
      this.updateForm.patchValue({
        lastName: this.user.lastName,
        firstName: this.user.firstName,
        phone_number: this.user.phone_number,
        email:this.user.email,
        userName: this.user.userName,
        city: this.user.city,
        available: this.user.available,
        roleName: cleanedRoleName,
        type: this.user.type,
        cites:this.user.city

      });
    });
  }

  initForm() {
    this.updateForm = this.fb.group({
      lastName: ['', Validators.required],
      firstName: ['', [Validators.required]],
      email: ['', [Validators.required]],
      phone_number: ['', [Validators.required]],
      //userName: this.fb.control({ value: '', disabled: true }, Validators.required),
      city: ['', [Validators.required]],
      type: [''],
      available:['', [Validators.required]],
      roleName: ['', [Validators.required]],
      //type: ['']

    });
  }

  getRoleValue(roleName: string | null): string | null {
    if (roleName) {
      // Nettoyage de la valeur du rôle
      const cleanedRoleName = roleName.replace(/\[|\]/g, '').trim();
      console.log("Original Role Name:", roleName);
      console.log("Cleaned Role Name:", cleanedRoleName);
      // Vérification si le rôle nettoyé est dans le tableau des rôles
      if (this.roles.includes(cleanedRoleName)) {
        return cleanedRoleName;
      } else {
        console.warn(`Le rôle nettoyé '${cleanedRoleName}' n'existe pas dans le tableau des rôles`);
      }
    } else {
      console.warn("Le rôle fourni est null ou vide");
    }
    return null;
  }


  updateUser() {
    if (this.updateForm.valid) {
      const updatedUser = this.updateForm.value;
      delete updatedUser.availability;
      console.log("avant user updated", updatedUser)
      updatedUser.id = this.id;
      updatedUser.roleName = `[${updatedUser.roleName}]`;
      console.log('Updated User:', updatedUser);
      console.log('Updated email:', updatedUser.email);
      console.log('Updated id:', updatedUser.id);
      this.ser.checkUserExistByEmail1(updatedUser.email, updatedUser.id).subscribe((data)=>{
        if(data){
          this.snackBar.open('L\'adresse e-mail existe déjà', 'Fermer', {
            duration: 3000,
            horizontalPosition: 'right',
            verticalPosition: 'top',

          });
        } else {
          this.ser.updateUser(updatedUser).subscribe(response => {
            this.snackBar.open('L\'utilisateur est bien modifié', 'Fermer', {
              duration: 3000,
              horizontalPosition: 'right',
              verticalPosition: 'top',
            });
            console.log("apres user updated", updatedUser)
            this.ref.close();
          }, error => {
            console.error('Error updating user:', error);
            this.snackBar.open('Failed to update user', 'Fermer', {
              duration: 3000,
              horizontalPosition: 'right',
              verticalPosition: 'top',
            });
          });
        }
      });
    }
  }


  close() {
    this.ref.close();
  }


}
