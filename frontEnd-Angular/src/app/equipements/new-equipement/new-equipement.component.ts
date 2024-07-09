import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import { PlanningServiceService } from '../../../../service/planning-service.service';
import { TypeEquipement } from '../../models/typeEquipement';
import { ArborescenceComponent } from '../arborescence/arborescence.component';
import {Immuble} from "../../models/immuble";
import {Etage} from "../../models/etage";
import {Salle} from "../../models/salle";
import {Site} from "../../models/Site";
import {dateTimestampProvider} from "rxjs/internal/scheduler/dateTimestampProvider";
import {Equipement} from "../../models/equipement";
import {MatSnackBar, MatSnackBarRef} from "@angular/material/snack-bar";

@Component({
  selector: 'app-new-equipement',
  templateUrl: './new-equipement.component.html',
  styleUrls: ['./new-equipement.component.css']
})
export class NewEquipementComponent implements OnInit {
  typeEquipement!: TypeEquipement[];
  public addForm!: FormGroup;
  statuss: string[] = ['En service', 'En maintenance', 'Hors service'];
equipement!:Equipement
  salle!:string
  site!:String

  constructor(
    private ser: PlanningServiceService,
    private fb: FormBuilder,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<NewEquipementComponent>,
  ) {
    this.getAllTYpeEquip();
    dialogRef.disableClose = true;

    this.addForm = this.fb.group({
      code: this.fb.control('', Validators.required),
      nom: this.fb.control('', Validators.required),
      numeroSerie: this.fb.control('', Validators.required),
      typeEquipementt: this.fb.control('', Validators.required),
      statut: this.fb.control('En service', Validators.required),
      salle: this.fb.control(this.sal, Validators.required),
      marque: this.fb.control('', Validators.required),
      descreption:this.fb.control('', Validators.required)
    });



  }

  getAllTYpeEquip() {
    this.ser.getAllTypeEquip().subscribe((data) => {
      this.typeEquipement = data;
    });
  }
  idType!:number
  getIdTypeEqui(type:String){
     this.ser.getIdTypeByName(type).subscribe((data)=>{
        this.idType = data;
         console.log(this.idType)
     })
  }
  ngOnInit() {}
    sal!:Salle

  selectedSalle!: Salle;
  arbo() {
    const dialogRef = this.dialog.open(ArborescenceComponent, {
      width: '30%',
      height: '300px',
      exitAnimationDuration: '500ms',
      data: { title: 'Equipement' }
    });

    dialogRef.afterClosed().subscribe((selectedSalle: Salle) => {
      if (selectedSalle) {
        this.selectedSalle = selectedSalle; // Stockez l'objet salle sélectionné
      }
    });
  }

  add() {
    this.equipement = this.addForm.value;
    // Vérifiez d'abord si this.selectedSalle est défini avant d'essayer d'accéder à sa propriété id
    if (this.selectedSalle && this.selectedSalle.id) {
      console.log("idS", this.selectedSalle.id);
      console.log("salle", this.selectedSalle);
      const idRes = this.equipement.typeEquipementt.id;
      console.log("type equipement", this.equipement.typeEquipementt.name);
      console.log("yarabi", idRes);
      this.ser.checkEquipExists(this.equipement.numeroSerie, this.equipement.code).subscribe(
        (exists) => {
          if (exists) {
            console.log(exists);
            const snackBarRef1: MatSnackBarRef<any> = this.snackBar.open('Cet équipement existe déjà. ', 'Modifier', {
              duration: 5000,
              horizontalPosition: 'right',
              verticalPosition: 'top',
              panelClass: ['blue-snackbar']
            });
          } else {
            console.log("equipemmmmmment:", this.addForm.value);
            this.ser.addNewEquipemnt(this.addForm.value, idRes, this.selectedSalle.id).subscribe((data) => {
              console.log(this.selectedSalle.id);
              this.close();
              const snackBarRef1: MatSnackBarRef<any> = this.snackBar.open('Equipement bien ajouté. ', 'OK', {
                duration: 5000,
                horizontalPosition: 'right',
                verticalPosition: 'top',
                panelClass: ['blue-snackbar']
              });

            });
          }
        }
      );
    } else {
      // Gérez le cas où this.selectedSalle est indéfini ou sa propriété id est indéfinie
      console.error("Erreur : La salle sélectionnée est invalide.");
    }
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
