import {Component, Inject, OnInit} from '@angular/core';
import {TypeEquipement} from "../../models/typeEquipement";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Equipement} from "../../models/equipement";
import {PlanningServiceService} from "../../../../service/planning-service.service";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {MatSnackBar, MatSnackBarRef} from "@angular/material/snack-bar";
import {Salle} from "../../models/salle";
import {ArborescenceComponent} from "../../equipements/arborescence/arborescence.component";

@Component({
  selector: 'app-add-equipemnt-to-site',
  templateUrl: './add-equipemnt-to-site.component.html',
  styleUrl: './add-equipemnt-to-site.component.css'
})
export class AddEquipemntToSiteComponent  implements OnInit {
  typeEquipement!: TypeEquipement[];
  public addForm!: FormGroup;
  statuss: string[] = ['En service', 'En maintenance', 'Hors service'];
  equipement!:Equipement
  salle!:string
  site!:String
  siteId!:number
  constructor(
    private ser: PlanningServiceService,
    private fb: FormBuilder,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<AddEquipemntToSiteComponent>, @Inject(MAT_DIALOG_DATA) public data: any

  ) {
    this.siteId = data.id;
    this.getAllTYpeEquip();
  this.getAllSalleOfSite(this.siteId);
    this.addForm = this.fb.group({
      code: this.fb.control('', Validators.required),
      nom: this.fb.control('', Validators.required),
      numeroSerie: this.fb.control('', Validators.required),
      typeEquipementt: this.fb.control('', Validators.required),
      statut: this.fb.control('En service', Validators.required),
      salle: this.fb.control('', Validators.required),
      marque: this.fb.control('', Validators.required),
      dateMiseService:this.fb.control('', Validators.required),
      descreption:this.fb.control('', Validators.required)
    });



  }

  getAllTYpeEquip() {
    this.ser.getAllTypeEquip().subscribe((data) => {
      this.typeEquipement = data;
    });
  }
salles!:Salle[]
  getAllSalleOfSite(id:number) {
    this.ser.getSalleOfSite(id).subscribe((data) => {
      this.salles = data;
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

  add() {
    this.equipement = this.addForm.value;

    if (!this.isEquipementComplet(this.equipement)) {
      this.showSnackBar('Veuillez remplir tous les champs.');
      return;
    }

    const idRes = this.equipement.typeEquipementt.id;
    const idSalle = this.equipement.salle.id;
    console.log("type equipement", this.equipement.typeEquipementt.name);
    console.log("Type", idRes);
    console.log("salle ID:", idSalle);

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
          this.ser.addNewEquipemnt(this.addForm.value, idRes, idSalle).subscribe((data) => {
            this.close();
            const snackBarRef1: MatSnackBarRef<any> = this.snackBar.open(`Equipement bien ajouté au site ID: `+ this.siteId+'.', 'OK', {
              duration: 5000,
              horizontalPosition: 'right',
              verticalPosition: 'top',
              panelClass: ['blue-snackbar']
            });
          });
        }
      }
    );
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
  isEquipementComplet(equip: Equipement): boolean {
    return !!(equip.nom && equip.code && equip.typeEquipementt && equip.salle.id && equip.marque && equip.numeroSerie && equip.descreption && equip.statut);
  }

}
