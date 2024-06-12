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
import {KeycloakProfile} from "keycloak-js";
import {KeycloakService} from "keycloak-angular";
import {User} from "../models/user";

@Component({
  selector: 'app-new-planning',
  templateUrl: './new-planning.component.html',
  styleUrl: './new-planning.component.css'
})
export class NewPlanningComponent implements OnInit{
  Semestre: string[]=[ 'Trimestriel' , 'Mensuel' , 'Annuel'];
  planning! : PlanningMaintenanceDTO;
  public addForm!:FormGroup;
  responsableDTO!:responsableDTO[];
  site!:Site[];
  public profil: KeycloakProfile | null = null;
  constructor( @Inject(MAT_DIALOG_DATA) public data:any,private ref:MatDialogRef<NewPlanningComponent>,
               private fb:FormBuilder , private ser:PlanningServiceService ,private snackBar: MatSnackBar,
               private dataser:PlanningdataserviceService,private keycloakService: KeycloakService ) {
    ref.disableClose = true;
  }

  inputdata:any;
  respo!: User;

  public userId: string | null = null;
  ngOnInit() {this.inputdata=this.data;
    this.keycloakService.loadUserProfile().then(
      (profile: KeycloakProfile) => {
        this.profil = profile;
        console.log('Profil chargé :', this.profil);
    console.log('Profil chargé2 :', this.profil);
    this.addForm=this.fb.group({
      name:this.fb.control('', [Validators.required]),
      semestre:this.fb.control('', [Validators.required]),
      dateDebutRealisation:this.fb.control('', [Validators.required]),
      dateFinRealisation:this.fb.control('', [Validators.required]),
      description:this.fb.control('', [Validators.required]),
      site:this.fb.control('',[Validators.required]),


    })
    this.listSite();
    this.getSiteOfCentr()
        this.userId = this.profil?.id ?? null;
this.getCentreTechniqueOfProf()
    console.log("userId", this.profil.id)})
  }
centre!:String
  getCentreTechniqueOfProf(){
    // Vérifier si userId est défini
    if (this.userId !== null) {
      // Appeler la fonction getCentreTechniqueOfRespo avec userId
      this.ser.getCentreTechniqueOfRespo(this.userId).subscribe((data)=>{
        this.centre = data;
        this.getSiteOfCentr();
        this.getSiteByCt(this.centre);
        console.log("centre", this.centre)
      });
    } else {
      console.error('UserId est null. Impossible d\'appeler getCentreTechniqueOfRespo.');
    }
  }

  sitebyCt!:Site[]
  getSiteByCt(ct:String){
    this.ser.getSiteByCentre(ct).subscribe((data)=>{
      this.sitebyCt =data;
      console.log("les sites",this.sitebyCt)
    })
  }
sites!:Site[]
  getSiteOfCentr(){
    console.log("le centre shhbce",this.centre)
    this.ser.getAllSites().subscribe((data)=>{console.log("site",data)

    this.sites= data
    })
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
    const idRes = this.userId;
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
