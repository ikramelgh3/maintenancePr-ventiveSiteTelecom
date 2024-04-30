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

@Component({
  selector: 'app-update-planning',
  templateUrl: './update-planning.component.html',
  styleUrl: './update-planning.component.css'
})
export class UpdatePlanningComponent implements OnInit{

  inputdata:any;
  Semestre: string[]=[ 'Semestre 1' , 'Semestre 2'];
  Planning! :PlanningMaintenanceDTO ;
  id!:number;
  updateForm!: FormGroup;
  planningUpdated: boolean = false;

  constructor(@Inject(MAT_DIALOG_DATA) public data: { id: number },private ref:MatDialogRef<NewPlanningComponent>
  ,private fb:FormBuilder , private ser:PlanningServiceService , private router:Router
    , private not:NotificationService ,private snackBar: MatSnackBar,
              private refrechS:RefrechSerService , private r:ActivatedRoute ,  private dataser:PlanningdataserviceService) {
  }

  ngOnInit() {
    this.initForm();
    this.id = this.data.id;
    this.ser.getPlanningById(this.id).subscribe(data =>{
      this.Planning = data;

      console.log("this is data", this.Planning);
        this.updateForm.patchValue({
          name: data.name,
          semestre: data.semestre,
          dateDebutRealisation: data.dateDebutRealisation,
          dateFinRealisation: data.dateFinRealisation,
          description: data.description,
          id_Site: data.id_Site,
          id_Respo: data.id_Respo
        });
      },
      error => console.log(error)
    );
  }

  initForm() {
    this.updateForm = this.fb.group({
      name:this.fb.control (['', Validators.required]),
      semestre:this.fb.control('', [Validators.required]),
      dateDebutRealisation:this.fb.control('', [Validators.required]),
      dateFinRealisation:this.fb.control('', [Validators.required]),
      description:this.fb.control('', [Validators.required]),
      id_Site:this.fb.control('',[Validators.required]),
      id_Respo:this.fb.control('',[Validators.required])

    });
  }
  closePopup(){

    this.ref.close();
  }
  updatePlanning() {
    if (this.updateForm.valid) {
      const formData = this.updateForm.value;
      this.ser.updatePlanning(this.id, formData).subscribe(response => {
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
