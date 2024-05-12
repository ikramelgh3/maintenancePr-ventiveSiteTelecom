import {Component, EventEmitter, Inject, OnInit, Output} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {NewPlanningComponent} from "../new-planning/new-planning.component";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {PlanningServiceService} from "../../../service/planning-service.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NotificationService} from "../service/notification.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {RefrechSerService} from "../service/refrech-ser.service";
import {PlanningMaintenanceDTO} from "../models/PlanningMaintenanceDTO";
import {HttpErrorResponse} from "@angular/common/http";
import {PlanningdataserviceService} from "../planningdataservice.service";
import {responsableDTO} from "../models/responsableDTO";
import {Site} from "../models/Site";

@Component({
  selector: 'app-update-planning',
  templateUrl: './update-planning.component.html',
  styleUrl: './update-planning.component.css'
})
export class UpdatePlanningComponent implements OnInit{

  inputdata:any;
  Semestre: string[]=[ 'Trimestriel' , 'Mensuel' , 'Annuel'];
  Planning! :PlanningMaintenanceDTO ;
  id!:number;
  updateForm!: FormGroup;
  planningUpdated: boolean = false;
  id_Respo!:number;
  id_Site!:number;
  Respo!:responsableDTO;
  Sit!:Site
  responsableDTO!:responsableDTO[];
  site!:Site[]
  constructor(@Inject(MAT_DIALOG_DATA) public data: { id: number },private ref:MatDialogRef<NewPlanningComponent>
  ,private fb:FormBuilder , private ser:PlanningServiceService , private router:Router
    , private not:NotificationService ,private snackBar: MatSnackBar,
              private refrechS:RefrechSerService , private r:ActivatedRoute ,  private dataser:PlanningdataserviceService) {
  }

  ngOnInit() {
    this.initForm();
    this.id = this.data.id;
    this.ser.getPlanningById(this.id).subscribe(data => {
      this.Planning = data;
      this.getRespo(data.id_Respo);
      this.getSite(data.id_Site);
      console.log("this is data", this.Planning);
      this.updateForm.patchValue({
        name: data.name,
        semestre: data.semestre,
        dateDebutRealisation: data.dateDebutRealisation,
        dateFinRealisation: data.dateFinRealisation,
        description: data.description,
      });
      // Charger la liste des responsables et des sites
      this.listRespo();
      this.listSite();
    }, error => console.log(error));
  }

  getRespo(Id:number){
    this.ser.getRespoById(Id).subscribe((data) => {
      this.Respo = data;
      this.id_Respo = data.id;
      // Mettre à jour la valeur du champ responsable dans le formulaire
      this.updateForm.patchValue({
        Respo: data.id // Ou autre propriété appropriée
      });
    });
  }

  getSite(Id:number){
    this.ser.getSiteById(Id).subscribe((data) => {
      this.Sit = data;
      this.id_Site=data.id;
      // Mettre à jour la valeur du champ site dans le formulaire
      this.updateForm.patchValue({
        Sit: data.id // Ou autre propriété appropriée
      });
    });
  }
  listRespo() {
    this.ser.getAllResponsable().subscribe(
      (data)=>{
        this.responsableDTO=data;
        console.log(this.responsableDTO);
      },
      (error)=>{
        console.log(error);

      }
    )
  }

  listSite() {
    this.ser.getAllSites().subscribe(
      (data)=>{
        this.site=data;
        console.log(this.site);
      },
      (error)=>{
        console.log(error);

      }
    )
  }

  onRespoSelectionChange(event: any) {
    this.id_Respo = event.value;
  }

  onSiteSelectionChange(event: any) {
    this.id_Site = event.value;
  }

  initForm() {
    this.updateForm = this.fb.group({
      name:this.fb.control (['', Validators.required]),
      semestre:this.fb.control('', [Validators.required]),
      dateDebutRealisation:this.fb.control('', [Validators.required]),
      dateFinRealisation:this.fb.control('', [Validators.required]),
      description:this.fb.control('', [Validators.required]),
      Sit:this.fb.control('',[Validators.required]),
      Respo:this.fb.control('',[Validators.required])

    });
  }
  closePopup(){

    this.ref.close();
  }
  updatePlanning() {
    if (this.updateForm.valid) {
      const formData = this.updateForm.value;
      this.ser.updatePlanning(this.id, formData , this.id_Site , this.id_Respo
      ).subscribe(response => {
        if (response instanceof HttpErrorResponse) {
          if (response.status === 400 && response.error === 'Un planning avec ce nom existe déjà') {
            this.snackBar.open('Un planning avec ce nom existe déjà', 'Fermer', {
              duration: 8000,
              horizontalPosition: 'end',
              verticalPosition: 'top',
              panelClass: ['error-snackbar']
            });
          }
        } else {

          console.log(this.id_Site);
          console.log(this.id_Respo);
          console.log(this.Sit);
          this.snackBar.open('Planning mis à jour avec succès', 'Fermer', {
            duration: 8000,
            horizontalPosition: 'end',
            verticalPosition: 'top'
          });
          this.dataser.refreshPlannings();
          this.dataser.refreshPlanningDetails(this.id)
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
    } else {
      console.log('Le formulaire n\'est pas valide.');
    }
  }


}
