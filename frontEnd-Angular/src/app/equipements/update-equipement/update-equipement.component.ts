import {Component, Inject, OnInit} from '@angular/core';
import {TypeEquipement} from "../../models/typeEquipement";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Equipement} from "../../models/equipement";
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {NewPlanningComponent} from "../../new-planning/new-planning.component";
import {PlanningServiceService} from "../../../../service/planning-service.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NotificationService} from "../../service/notification.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {RefrechSerService} from "../../service/refrech-ser.service";
import {PlanningdataserviceService} from "../../planningdataservice.service";
import {NewEquipementComponent} from "../new-equipement/new-equipement.component";
import {ArborescenceComponent} from "../arborescence/arborescence.component";
import {Salle} from "../../models/salle";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-update-equipement',
  templateUrl: './update-equipement.component.html',
  styleUrl: './update-equipement.component.css'
})
export class UpdateEquipementComponent implements OnInit{

  typeEquipement!: TypeEquipement[];
  typeEquipementtt!: TypeEquipement;
  Respo!: TypeEquipement;
  salle!: Salle;
  public updateForm!: FormGroup;
  statuss: string[] = ['En service', 'En maintenance', 'Hors service'];
  equipement!:Equipement
  site!:String
  id!:number
  id_type!:number;
  id_salle!:number;
  constructor(@Inject(MAT_DIALOG_DATA) public data: { id: number },
              private dialog: MatDialog,private ref:MatDialogRef<NewEquipementComponent>
    ,private fb:FormBuilder , private ser:PlanningServiceService ,
    private snackBar: MatSnackBar) {
    ref.disableClose = true;
  }
  selectedSalle!: Salle;
  ngOnInit() {
    this.initForm();
    this.id = this.data.id;
    this.ser.getEquiById(this.id).subscribe(data => {
      this.equipement = data;
      this.getRespo(data.typeEquipementt.id);
      this.getType(this.id_type);
      this.getSalleById(this.equipement.salle.id);
      console.log("salle id",this.equipement.salle.id)
      console.log(this.equipement.id);
      console.log("this is data", this.equipement);
      this.selectedSalle= this.equipement.salle
      this.id_salle = this.equipement.salle.id
      console.log("salle",this.equipement.salle)
      this.updateForm.patchValue({
        nom : this.equipement.nom,
        code : this.equipement.code,
        statut: this.equipement.statut,
        descreption: this.equipement.descreption,
        marque:this.equipement.marque,
        numeroSerie:this.equipement.numeroSerie
      });
      this.getAllTYpeEquip();
    }, error => console.log(error));
  }

  initForm() {
    this.updateForm = this.fb.group({
      nom:this.fb.control (['', Validators.required]),
      code:this.fb.control('', [Validators.required]),
      Respo:this.fb.control('', [Validators.required]),
      statut:this.fb.control('', [Validators.required]),
      numeroSerie:this.fb.control('', [Validators.required]),
      marque:this.fb.control('',[Validators.required]),
      descreption:this.fb.control('',[Validators.required])
    });
  }

  getType(Id: number) {
    this.ser.getTypeEQUIById(Id).subscribe(
      (data) => {
        this.Respo = data;
        console.log("getTypeEquipement", this.typeEquipementtt);
        this.id_type = data.id;
        // Mettre à jour la valeur du champ responsable dans le formulaire
        this.updateForm.patchValue({
          Respo: data.id // Ou autre propriété appropriée
        });
      },
      (error) => {
        console.log(error);
      }
    );
  }
  id_typeE!:number
  getRespo(Id:number){
    this.ser.getTypeEQUIById(Id).subscribe((data) => {
      this.Respo = data;
      this.id_typeE = data.id;
      // Mettre à jour la valeur du champ responsable dans le formulaire
      this.updateForm.patchValue({
        Respo: data.id // Ou autre propriété appropriée
      });
    });
  }
  arbo() {
    const dialogRef = this.dialog.open(ArborescenceComponent, {
      width: '30%',
      height: '300px',
      exitAnimationDuration: '500ms',
      data: { title: 'Equipement' }
    });

    dialogRef.afterClosed().subscribe((selectedSalle: Salle) => {
      if (selectedSalle) {
        this.selectedSalle = selectedSalle;
        this.updateForm.patchValue({});
      }
    });
  }
  sal!:Salle
  getSalleById(id: number) {
    this.ser.getSalleById(id).subscribe(
      (data) => {
        this.salle = data;
        this.id_salle = data.id
        console.log("getSalle", this.salle);
        // Mettre à jour la valeur du champ responsable dans le formulaire avec le nom de la salle
        this.updateForm.patchValue({
          sal: data.id // Ou une autre propriété appropriée de la salle

        });
      },
      (error) => {
        console.log(error);
      }
    );
  }

  getAllTYpeEquip() {
    this.ser.getAllTypeEquip().subscribe((data) => {
      this.typeEquipement = data;
    });
  }


  onSalleSelectionChange(event: any) {
    this.selectedSalle = event.value;
    //this.id_salle =this.selectedSalle.id
  }

  onTypeSelectionChange(event: any) {
    this.id_typeE = event.value;
  }

codeEqui!:string
  updateEquipement() {
    if (this.updateForm.valid) {
      const formData = this.updateForm.value;
      console.log("formdata",formData)
      console.log(  "idSalleNew",this.selectedSalle.id)
      const nouvelleIdSalle = this.selectedSalle.id;
      this.codeEqui = formData.code;
      console.log(this.codeEqui);
      this.checkIfExist( this.codeEqui,this.id );
    } else {
      this.snackBar.open('Veuillez remplir tous les champs', 'Fermer', {
        duration: 8000,
        horizontalPosition: 'end',
        verticalPosition: 'top'
      });
    }
  }
  closePopup(){
     this.ref.close();
  }
exist!:Boolean
  checkIfExist(code:string,id:number ){
     this.ser.checkifEquipeExisteByCode( code,id).subscribe((data)=>{
        console.log( "boolde check", data);
        this.exist = data;
       const formData = this.updateForm.value;
       console.log("formdata",formData)
       console.log(  "idSalleNew",this.selectedSalle.id)
       const nouvelleIdSalle = this.selectedSalle.id;
        if(data){
          this.snackBar.open('Un planning avec ce nom existe déjà', 'Fermer', {
            duration: 8000,
            horizontalPosition: 'end',
            verticalPosition: 'top',
            panelClass: ['error-snackbar']
          });
        }
        else {
          this.ser.updateEquiepement(this.id, formData , this.id_typeE , nouvelleIdSalle
          ).subscribe(response => {
            if (response instanceof HttpErrorResponse) {
              if (response.status === 400 && response.error === 'Un planning avec ce nom existe déjà') {
                this.snackBar.open('Un équipement existe déjà', 'Fermer', {
                  duration: 8000,
                  horizontalPosition: 'end',
                  verticalPosition: 'top',
                  panelClass: ['error-snackbar']
                });
              }
            } else {

              console.log(this.selectedSalle.id);
              this.snackBar.open('Equipement mis à jour avec succès', 'Fermer', {
                duration: 8000,
                horizontalPosition: 'end',
                verticalPosition: 'top'
              });

              this.closePopup();

            }
          }, error => {
            console.error('Un planning avec ce nom existe déjà: ', error);
            this.snackBar.open('Un planning avec ce nom existe déjà', 'Fermer', {
              duration: 8000,
              horizontalPosition: 'end',
              verticalPosition: 'top',
              panelClass: ['error-snackbar']
            });
          });
        }
     })
  }
}
