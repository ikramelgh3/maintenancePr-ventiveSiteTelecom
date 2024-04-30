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

@Component({
  selector: 'app-planning-maintenance',
  templateUrl: './planning-maintenance.component.html',
  styleUrls: ['./planning-maintenance.component.css']
})
export class PlanningMaintenanceComponent implements OnInit {
  showPlanningDetails: boolean = false;

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

  constructor(private planningService: PlanningServiceService, private dialog: MatDialog,
              private dialogService: DialogService, private refrechS:RefrechSerService,
              private not: NotificationService, private route: Router,private snackBar: MatSnackBar
              , private activatedRoute: ActivatedRoute , private dataser:PlanningdataserviceService) { }

  ngOnInit() {
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
    this.getPlanning();
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

  getTotalElment(){
    this.planningService.getSize().subscribe((data)=>
    this.totalItems=data,
      )

  }
  getPlanning() {
    this.planningService.getPlannings().subscribe(
      (data) => {
        this.plannings = data;
        this.noResultsFound = this.plannings.length === 0; // Mettre à jour l'état noResultsFound
        console.log(this.plannings);
      }
    )
  }


  showSnackBar(message: string, action: string) {
    const config = new MatSnackBarConfig();
    config.panelClass = ['blue-snackbar']; // Ajoute une classe pour personnaliser le style du snackbar
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
      this.totalItems = this.plannings.length; // Mettre à jour le nombre total d'éléments
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
                duration: 8000, // Durée indéfinie, la notification restera affichée
                horizontalPosition: 'right',
                verticalPosition: 'top',
              });

              // Ajouter un écouteur pour fermer la notification lorsque l'utilisateur interagit avec elle
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


        console.log(data); // Stockez les détails récupérés dans une variable pour les afficher dans le HTML
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


    console.log(planningId); // Appelez la méthode pour récupérer les détails lorsque vous sélectionnez un planning
  }


  toggleDetails(planning: PlanningMaintenanceDTO) {
    if (this.selectedPlanning === planning) {
      this.selectedPlanning = null; // Cliquez à nouveau pour masquer les détails
    } else {
      this.selectedPlanning = planning; // Afficher les détails du planning sélectionné
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


}
