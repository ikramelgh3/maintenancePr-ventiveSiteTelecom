import {Component, Inject, OnInit} from '@angular/core';
import {PlanningMaintenanceDTO} from "../../models/PlanningMaintenanceDTO";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Site} from "../../models/Site";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {PlanningServiceService} from "../../../../service/planning-service.service";
import {MatSnackBar, MatSnackBarRef} from "@angular/material/snack-bar";
import {PlanningdataserviceService} from "../../planningdataservice.service";
import {responsableDTO} from "../../models/responsableDTO";
import {KeycloakProfile} from "keycloak-js";
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-add-planningto-site',
  templateUrl: './add-planningto-site.component.html',
  styleUrl: './add-planningto-site.component.css'
})
export class AddPlanningtoSiteComponent implements OnInit{
  Semestre: string[]=[ 'Trimestriel' , 'Mensuel' , 'Annuel'];
  planning! : PlanningMaintenanceDTO;
  public addForm!:FormGroup;
  responsableDTO!:responsableDTO[];
  siteE!:Site;
  public  profil!:KeycloakProfile
siteId!:number
  constructor(private ref:MatDialogRef<AddPlanningtoSiteComponent>,
               private fb:FormBuilder , private ser:PlanningServiceService ,private snackBar: MatSnackBar,
               private dataser:PlanningdataserviceService  , @Inject(MAT_DIALOG_DATA) public data: any,public  ky :KeycloakService
  ) {
    this.siteE = data.site;
    ref.disableClose = true;
  }

  inputdata:any;
  public userId: string | null = null;

  ngOnInit() {
    if (this.ky.isLoggedIn()) {
      this.ky.loadUserProfile().then((profile) => {
        this.profil = profile;
        console.log(this.profil);
        this.userId = this.profil?.id ?? null;
        console.log("id", this.userId);})}
    this.inputdata=this.data;
    this.addForm=this.fb.group({
      name:this.fb.control('', [Validators.required]),
      semestre:this.fb.control('', [Validators.required]),
      dateDebutRealisation:this.fb.control('', [Validators.required]),
      dateFinRealisation:this.fb.control('', [Validators.required]),
      description:this.fb.control('', [Validators.required]),
     // site:this.fb.control('',[Validators.required]),


    });this.listRespo();
    //this.listSite();
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

    const idSite = this.siteE.id;
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


            console.log("le site complet", this.siteE);
            this.ser.addPlanningComplet(this.planning, this.userId, this.siteE.id).subscribe(
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




}
