import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {PlanningMaintenanceDTO} from "../models/PlanningMaintenanceDTO";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {PlanningServiceService} from "../../../service/planning-service.service";
import {Router} from "@angular/router";
import {NotificationService} from "../service/notification.service";
import {MatSnackBar, MatSnackBarRef} from "@angular/material/snack-bar";
import {RefrechSerService} from "../service/refrech-ser.service";
import {responsableDTO} from "../models/responsableDTO";
import {Site} from "../models/Site";
import {PlanningdataserviceService} from "../planningdataservice.service";

@Component({
  selector: 'app-new-planning',
  templateUrl: './new-planning.component.html',
  styleUrl: './new-planning.component.css'
})
export class NewPlanningComponent implements OnInit{
  Semestre: string[]=[ 'Semestre 1' , 'Semestre 2'];
  planning! : PlanningMaintenanceDTO;
  public addForm!:FormGroup;
  responsableDTO!:responsableDTO[];
  site!:Site[];

  constructor( @Inject(MAT_DIALOG_DATA) public data:any,private ref:MatDialogRef<NewPlanningComponent>,
               private fb:FormBuilder , private ser:PlanningServiceService , private router:Router
               , private not:NotificationService ,private snackBar: MatSnackBar,
               private refrechS:RefrechSerService,
               private dataser:PlanningdataserviceService ) {

  }

  inputdata:any;

  ngOnInit() {this.inputdata=this.data;

    this.addForm=this.fb.group({
      name:this.fb.control('', [Validators.required]),
      semestre:this.fb.control('', [Validators.required]),
      dateDebutRealisation:this.fb.control('', [Validators.required]),
      dateFinRealisation:this.fb.control('', [Validators.required]),
      description:this.fb.control('', [Validators.required]),
      site:this.fb.control('',[Validators.required]),
      responsableMaint:this.fb.control('',[Validators.required])

    });this.listRespo();
    this.listSite();
  }

  referechDate() {
    this.ser.getPlannings().subscribe((res: any) => {
      this.planning = res;

    });
  }
  closePopup(){

    this.ref.close();
  }

  add() {
    this.planning = this.addForm.value;
    const idRes = this.planning.responsableMaint.id;
    const idSite = this.planning.site.id;
    console.log(this.planning);

    // Vérifier si un planning avec le même nom existe déjà
    this.ser.checkPlanningExists(this.planning.name).subscribe(
      (exists) => {
        if (exists) {
          const snackBarRef1: MatSnackBarRef<any> = this.snackBar.open('Ce planning existe déjà. Veuillez choisir un autre nom.', 'Modifier', {
            duration: 0,
            horizontalPosition: 'right',
            verticalPosition: 'top',
            panelClass: ['blue-snackbar']
          });
        } else {
          // Vérifier si la date de fin de réalisation est supérieure à la date de début de réalisation
          if (new Date(this.planning.dateFinRealisation) <= new Date(this.planning.dateDebutRealisation)) {
            const snackBarRef2: MatSnackBarRef<any> = this.snackBar.open('La date de fin de réalisation doit être supérieure à la date de début de réalisation.', 'Modifier', {
              duration: 0,
              horizontalPosition: 'right',
              verticalPosition: 'top',
              panelClass: ['blue-snackbar']
            });
          } else {
            // Ajouter le planning
            this.ser.addPlanningComplet(this.planning , idRes,idSite).subscribe(
              (data) => {
                this.dataser.refreshPlannings();
                console.log(data);

                this.closePopup();


                const snackBarRef3: MatSnackBarRef<any> = this.snackBar.open('Le planning est bien ajouté', '', {
                  duration: 8000,
                  horizontalPosition: 'right',
                  verticalPosition: 'top',
                  panelClass: ['blue-snackbar']
                });
                snackBarRef3.onAction().subscribe(() => {
                  snackBarRef3.dismiss();
                });
              },
              (error) => {
                console.error(error);
                const snackBarRef4: MatSnackBarRef<any> = this.snackBar.open('Une erreur s\'est produite lors de l\'ajout du planning.', 'Ok', {
                  duration: 0,
                  horizontalPosition: 'right',
                  verticalPosition: 'top',
                  panelClass: ['blue-snackbar']
                });
              }
            );
          }
        }
      },
      (error) => {
        console.error(error);
        const snackBarRef5: MatSnackBarRef<any> = this.snackBar.open('Une erreur s\'est produite lors de la vérification de l\'existence du planning.', 'Ok', {
          duration: 0,
          horizontalPosition: 'right',
          verticalPosition: 'top',
        });
      }
    );
  }

  clear() {
    this.addForm.reset();
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


}
