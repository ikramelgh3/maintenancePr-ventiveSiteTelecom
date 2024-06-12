import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {TypeEquipement} from "../../models/typeEquipement";
import {Checklist} from "../../models/checklist";
import {PlanningServiceService} from "../../../../service/planning-service.service";
import {MatSnackBar, MatSnackBarRef} from "@angular/material/snack-bar";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Equipement} from "../../models/equipement";

@Component({
  selector: 'app-add-pt-to-checklist',
  templateUrl: './add-pt-to-checklist.component.html',
  styleUrl: './add-pt-to-checklist.component.css'
})
export class AddPtToChecklistComponent implements  OnInit{

  addForm!:FormGroup
  typeEquipement1!: TypeEquipement;
  checklist!:Checklist
  pointMesure: Checklist[] = [];
  constructor(private  ser:PlanningServiceService , private snackBar: MatSnackBar,private fb: FormBuilder,private dialogRef: MatDialogRef< AddPtToChecklistComponent>, @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.typeEquipement1 = data.typeEquipement;
    dialogRef.disableClose = true;
  }
  ngOnInit() {

    this.addForm = this.fb.group({
      attribut: ['', Validators.required],
      resultatsPossibles: ['', Validators.required],
      typeEquipent: [this.typeEquipement1]})
  }
  id!:number;


  add() {
    this.checklist = this.addForm.value;
    console.log("checklist", this.checklist)
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
          console.log("idTy:", this.typeEquipement1.id);
          this.ser.addPointMesure(this.addForm.value, this.typeEquipement1.id).subscribe((data) => {
            console.log("idType: ",this.checklist.typeEquipent.id);
            this.close();
            const snackBarRef1: MatSnackBarRef<any> = this.snackBar.open('Point de mesure bien ajouté à la checklist. ', 'OK', {
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
