import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { MatDialog } from "@angular/material/dialog";
import { NewPlanningComponent } from "../new-planning/new-planning.component";
import { PlanningMaintenanceDTO } from "../models/PlanningMaintenanceDTO";
import { PlanningServiceService } from "../../../service/planning-service.service";
import { DialogService } from "../service/dialog.service";
import { NotificationService } from "../service/notification.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatSnackBar, MatSnackBarConfig, MatSnackBarRef, SimpleSnackBar} from '@angular/material/snack-bar';
import {RefrechSerService} from "../service/refrech-ser.service";
import {UpdatePlanningComponent} from "../update-planning/update-planning.component";
import {responsableDTO} from "../models/responsableDTO";
import {PlanningdataserviceService} from "../planningdataservice.service";
import {Site} from "../models/Site";
import {ImportDialogComponent} from "../import-dialog/import-dialog.component";
import {KeycloakProfile} from "keycloak-js";
import {KeycloakService} from "keycloak-angular";
import {Intervention} from "../models/intervention";
import {
  AddInterventionToEquipemntComponent
} from "../equipements/add-intervention-to-equipemnt/add-intervention-to-equipemnt.component";
import {
  AddInterventionToPlanningComponent
} from "./add-intervention-to-planning/add-intervention-to-planning.component";

@Component({
  selector: 'app-planning-maintenance',
  templateUrl: './planning-maintenance.component.html',
  styleUrls: ['./planning-maintenance.component.css']
})
export class PlanningMaintenanceComponent implements OnInit {
  showPlanningDetails: boolean = false;
  public  profil!:KeycloakProfile
  public plannings!: PlanningMaintenanceDTO[];
  public totalItems!:number ; // Nombre total d'éléments
  public id!: number;
  selectedPlanningDetails: any;
  @ViewChild('planningDetailsContainer') planningDetailsContainer!: ElementRef;
  private selectedPlanning: any;
  planningUpdated: boolean = false;
  private responsableDTO!:responsableDTO;
  p:any;
  private filteredPlannings!: PlanningMaintenanceDTO[];
  private site!: Site;

  protected noResultsFound: boolean=false;
  searchInput: string = '';
  private selectedFile!: File;
  intervention!:Intervention[]
  public userId: string | null = null;
  constructor(private planningService: PlanningServiceService, private dialog: MatDialog,public  ky :KeycloakService,
              private dialogService: DialogService, private refrechS:RefrechSerService,
              private not: NotificationService, private route: Router,private snackBar: MatSnackBar
              , private activatedRoute: ActivatedRoute , private dataser:PlanningdataserviceService) { }

  ngOnInit() {
    if (this.ky.isLoggedIn()) {
      this.ky.loadUserProfile().then((profile) => {
        this.profil = profile;
        console.log("profile", this.profil);
        this.userId = this.profil?.id ?? null;
        console.log("idU", this.userId);
       this.getPlanning()
      });
      this.planningService._refreshNeeded$.subscribe(() => {


        this.getPlanningDetailsById(this.id)

      })

      this.getTotalElment();
      this.dataser.plannings$.subscribe((plannings) => {
        this.plannings = plannings;
        this.totalItems = this.plannings.length; // Mettre à jour le nombre total d'éléments
      });

      this.dataser.planningDetails$.subscribe((planningDetails) => {
        this.selectedPlanningDetails = planningDetails;
        if (this.planningDetailsContainer) {
          this.planningDetailsContainer.nativeElement.scrollIntoView({ behavior: 'smooth' });
        }
      });
    }
  }

  not_foundInterv:boolean=false

  getInterventionOfPlanning(id:number){
     this.planningService.getInterventionsOfPlanning(id).subscribe((data)=>{{this.intervention = data}
     console.log("inteventionn",this.intervention)})
  }
  getStatusColorIntervention(status:String ) {
    switch (status) {
      case "PLANIFIEE":
        return "#1E90FF"; // Bleu
      case "EN_COURS":
        return "#FFA500"; // Orange
      case "TERMINEE":
        return "#32CD32"; // Vert
      case "ANNULEE":
        return "#FF4500"; // Rouge
      case "REPORTEE":
        return "#FFD700"; // Jaune
      default:
        return "#333333"; // Couleur par défaut (Gris foncé)
    }
  }

  filterByStatus(status: string) {
   this.planningService.getPlanningByStatus(status).subscribe(
     (data) =>{
       this.plannings = data;
       this.noResultsFound = this.plannings.length === 0;
     },
     (error)=>
     {
       console.log("Error");
     }
   )
  }

  getPlanningOfResp(){
    if (this.userId !== null) { this.planningService.getAllPlanningOfRespo(this.userId).subscribe((data)=>{this.plannings=data})

    console.log("plannnnf",this.plannings)}

  }
  getPlanningByType(type:String){
     this.planningService.getPlanningByTypeSite(type).subscribe(
       (data) => {
         this.plannings = data;
         this.noResultsFound = this.plannings.length === 0;
       },
       (error)=>
       {
         console.log("Error");
       }
     )
  }

  getPlanningBySemestre( seme:String){
    this.planningService.getPlanningBySemestre(seme).subscribe(
      (data) => {
        this.plannings = data;
        this.noResultsFound = this.plannings.length === 0;
      },
      (error)=>
      {
        console.log("Error");
      }
    )

  }
  getTotalElment(){
    this.planningService.getSize().subscribe((data)=>
    this.totalItems=data,
      )

  }
  getPlanning() {
    this.planningService.getPlannings().subscribe(
      (data) => {
        this.plannings = data;
        this.noResultsFound = this.plannings.length === 0;
        console.log(this.plannings);
      }
    )
  }


  showSnackBar(message: string, action: string) {
    const config = new MatSnackBarConfig();
    config.panelClass = ['blue-snackbar'];
    this.snackBar.open(message, action, config);
  }



  OpenNewPlanning() {
    var popup = this.dialog.open(NewPlanningComponent, {
      width: '50%', height: '550px',
      exitAnimationDuration: '500ms',
      data: { title: 'Planning de maintenance' }
    })
    this.referechDate();
  }

  referechDate() {
    this.planningService.getPlannings().subscribe((res: any) => {
      this.plannings = res;
      this.totalItems = this.plannings.length;
    });
  }


  deletePlanning(id: number) {
    this.dialogService.openConfirmDialof('Voulez vous supprimer ce planning?').afterClosed()
      .subscribe(res => {
        if (res) {
          this.planningService.deletePlanning(id).subscribe(
            () => {
              console.log('Planning deleted successfully');
              this.referechDate();
              this.selectedPlanningDetails = null;
              // Rediriger vers la page des plannings après la suppression
              const snackBarRef: MatSnackBarRef<any> = this.snackBar.open('Le planning a été supprimé', '', {
                duration: 8000,
                horizontalPosition: 'right',
                verticalPosition: 'top',
              });

              snackBarRef.onAction().subscribe(() => {
                snackBarRef.dismiss();
              });
            },
            error => {
              console.error('Error deleting planning:', error);
            }
          );
        }
      });
  }
  getStatusColor(status: String): String {
    switch (status) {
      case 'PLANIFIEE':
        return '#072f5f';
      case 'EN_COURS':
        return '#4CAF50'; // Bleu
      case 'TERMINE':
        return '#2196F3'; // Vert
      case 'EN_ATTENTE':
        return '#072f5f'; // Jaune
      default:
        return '#333'; // Couleur par défaut
    }
  }

  getShortDescription(description: String): String {
    if (description.length > 45) {
      return description.substring(0, 45) + '...';
    }
    return description;
  }

  isDescriptionLong(description: String): boolean {
    return description.length > 45;
  }

  getRespoById(id:number){
    this.planningService.getRespoById(id).subscribe((data)=> this.responsableDTO=data);
    console.log("respo" +this.responsableDTO);
  }
  getPlanningDetailsById(id: number) {
    this.getInterventionOfPlanning(id);
    this.planningService.getPlanningById(id).subscribe(
      (data) => {

        this.selectedPlanningDetails = data;
        this.getRespoById(this.selectedPlanningDetails.id_Respo);
        this.getRespoById(this.selectedPlanningDetails.id_Respo);
        console.log(data);
        if (this.selectedPlanningDetails && this.selectedPlanningDetails.responsableMaint) {
          console.log("cccccc" + this.selectedPlanningDetails.responsableMaint.nom);
        } else {
          console.log("ResponsableMaint est null");
        }


        console.log(data);
        if (this.planningDetailsContainer) {
          this.planningDetailsContainer.nativeElement.scrollIntoView({ behavior: 'smooth' });
        }
      },
      (error) => {
        console.error('Error fetching planning details:', error);
      }
    );

  }

  selectPlanning(planningId: number) {
    this.id = planningId;
    this.getPlanningDetailsById(planningId);


    console.log(planningId);
  }


  toggleDetails(planning: PlanningMaintenanceDTO) {
    if (this.selectedPlanning === planning) {
      this.selectedPlanning = null;
    } else {
      this.selectedPlanning = planning;
      console.log(planning);
    }
  }


  UpdatePlanning() {
    const popup = this.dialog.open(UpdatePlanningComponent, {
      width: '50%', height: '550px',
      exitAnimationDuration: '0',
      data: { id: this.id }
    });

  }
  search(): void {
    if (this.searchInput.trim() !== '') {
      this.planningService.getPlanningByKeyword(this.searchInput).subscribe(
        (data: PlanningMaintenanceDTO[]) => {
          this.plannings = data;
          this.noResultsFound = this.plannings.length === 0;
        },
        (error) => {
          console.error('Erreur lors de la recherche :', error);
        }
      );
    } else {
     this.getPlanning();
    }
  }

  exportToExcel() {
    this.planningService.exportToExcel().subscribe(
      (response: any) => {
        const fileName = this.getFileNameFromResponse(response);
        this.downloadFile(response.body, fileName);
        const snackBarRef: MatSnackBarRef<any> = this.snackBar.open('Les plannings sont bien exporter', '', {
          duration: 8000,
          horizontalPosition: 'right',
          verticalPosition: 'top',
        });

        snackBarRef.onAction().subscribe(() => {
          snackBarRef.dismiss();
        });
      },
      (error) => {
        console.error('Erreur lors de l\'exportation vers Excel : ', error);
      }
    );
  }

  getFileNameFromResponse(response: any): string {
    const contentDispositionHeader = response.headers.get('Content-Disposition');
    const fileNameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
    const matches = fileNameRegex.exec(contentDispositionHeader);
    if (matches != null && matches[1]) {
      return matches[1].replace(/['"]/g, '');
    }
    return `plannings_de_maintenance_${this.getCurrentDate()}.xlsx`; // Fallback filename if the filename cannot be extracted
  }

  downloadFile(data: any, fileName: string) {
    const blob = new Blob([data], { type: 'application/octet-stream' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    document.body.appendChild(a);
    a.style.display = 'none';
    a.href = url;
    a.download = fileName; // Utilisez le nom de fichier extrait du backend
    a.click();
    window.URL.revokeObjectURL(url);
  }

  getCurrentDate(): string {
    const currentDate = new Date();
    const year = currentDate.getFullYear();
    const month = (currentDate.getMonth() + 1).toString().padStart(2, '0');
    const day = currentDate.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }
  openImportDialog(): void {
    const dialogRef = this.dialog.open(ImportDialogComponent, {
      width: '400px',
    });
    this.referechDate();

  }


  addInterventionToPlanning(){
    var popup = this.dialog.open(AddInterventionToPlanningComponent, {
      width: '60%', height: '569px',
      exitAnimationDuration: '500ms',
      data: { eq: this.selectedPlanningDetails }
    })

  }
}
