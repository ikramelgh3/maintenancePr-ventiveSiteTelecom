import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {PlanningServiceService} from "../../../../service/planning-service.service";
import {TypeEquipement} from "../../models/typeEquipement";
import {MatDialogRef} from "@angular/material/dialog";
import {Site} from "../../models/Site";
import {Checklist} from "../../models/checklist";
import {MatSnackBar, MatSnackBarRef} from "@angular/material/snack-bar";
import {Equipement} from "../../models/equipement";

@Component({
  selector: 'app-add-checklist',
  templateUrl: './add-checklist.component.html',
  styleUrl: './add-checklist.component.css'
})
export class AddChecklistComponent implements  OnInit{

  addForm!:FormGroup
  typeEquipement!: TypeEquipement[];
  checklist!:Checklist
  constructor(private  ser:PlanningServiceService , private snackBar: MatSnackBar,private fb: FormBuilder,private dialogRef: MatDialogRef< AddChecklistComponent>) {
    dialogRef.disableClose = true;
  }
ngOnInit() {
    this.getAllTypeEquipemnt();
  this.addForm = this.fb.group({
    attribut: ['', Validators.required],
    typeEquipent: ['', Validators.required],
    resultatsPossibles: ['', Validators.required]})
}
  id!:number;



  getAllTypeEquipemnt(){
     this.ser.getAllTypeEquip().subscribe((data)=>{this.typeEquipement= data})
                      }
  add() {
    this.checklist = this.addForm.value;
    this.checklist.resultatsPossibles = this.addForm.value.resultatsPossibles.split('/');
  console.log(this.checklist.attribut);
  console.log(this.checklist.typeEquipent.id);
  console.log(this.checklist.resultatsPossibles)
      this.ser.checkkIfChecklistExistByName(this.checklist.attribut).subscribe(
        (exists) => {
          if (exists) {
            console.log("exist",exists);
            const snackBarRef1: MatSnackBarRef<any> = this.snackBar.open('Ce point de mesure existe déjà. ', 'Modifier', {
              duration: 5000,
              horizontalPosition: 'right',
              verticalPosition: 'top',
              panelClass: ['blue-snackbar']
            });
          } else {
            console.log("checklist:", this.addForm.value);
            this.ser.addPointMesure(this.addForm.value, this.checklist.typeEquipent.id).subscribe((data) => {
              console.log("idType: ",this.checklist.typeEquipent.id);
              this.close();
              const snackBarRef1: MatSnackBarRef<any> = this.snackBar.open('Point de mesure bien ajouté. ', 'OK', {
                duration: 5000,
                horizontalPosition: 'right',
                verticalPosition: 'top',
                panelClass: ['blue-snackbar']
              });

            });
          }
        }
      )
  }

  addEquipement(site: Equipement, idType: number, idSalle:number) {
    this.ser.addNewEquipemnt(site, idType, idSalle).subscribe(
      data => {
        // Traitement après l'ajout réussi
        this.showSnackBar('L a été ajouté avec succès');

      },

      error => {
        console.error(error);
        this.showSnackBar('Une erreur s\'est produite lors de l\'ajout du site.');
      }
    );
  }

  showSnackBar(message: string) {
    this.snackBar.open(message, 'Ok', {
      duration: 5000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
      panelClass: ['blue-snackbar']
    });
  }

  close(){
    this.dialogRef.close();
  }
}
