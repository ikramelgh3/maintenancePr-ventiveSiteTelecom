import {Component, OnInit} from '@angular/core';
import {Intervention} from "../../models/intervention";
import {PlanningServiceService} from "../../../../service/planning-service.service";
import {MatDialog} from "@angular/material/dialog";
import {GlobalChecklist} from "../../models/GlobalChecklist";
import {Checklist} from "../../models/checklist";
import {NewInterventionComponent} from "../../interventions/new-intervention/new-intervention.component";
import {AddPtToChecklistComponent} from "../../interventions/add-pt-to-checklist/add-pt-to-checklist.component";
import {KeycloakProfile} from "keycloak-js";
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-mes-interventions',
  templateUrl: './mes-interventions.component.html',
  styleUrl: './mes-interventions.component.css'
})
export class MesInterventionsComponent implements OnInit {
  searchInput:String=''
  noResultsFound:boolean= false


  public profil: KeycloakProfile | null = null;

  public userId: string | null = null;
  interventions!:Intervention[]
  ngOnInit() {
    this.keycloakService.loadUserProfile().then(
      (profile: KeycloakProfile) => {
        this.profil = profile;
        console.log('Profil chargé :', this.profil);
        this.userId = this.profil?.id ?? null;
        console.log("userId", this.profil.id);
        this.getInterventions(); // Appeler getInterventions une fois que userId est défini
        this.getTot();
        this.ser._refreshNeeded$.subscribe(() => {
          this.getInterventions();
          this.getTot();
        });
      }
    );
  }

  constructor(private  ser:PlanningServiceService, private dialog: MatDialog,private keycloakService: KeycloakService) {
  }

  tot:number =  0
  getTot(){this.ser.totInterventions().subscribe((data)=>{
    this.tot= data
    console.log("userId52",this.userId)
  })}


  getShortDescription(description: String): String {
    if (description.length > 45) {
      return description.substring(0, 45) + '...';
    }
    return description;
  }

  isDescriptionLong(description: String): boolean {
    return description.length > 45;
  }
  getInterventions() {
    console.log("interve")
    if (this.userId !== null) {
      this.ser.getInterventionOfTechnicein(this.userId).subscribe((data) => {
        this.interventions = data;
        console.log(this.interventions)
      })
    }
  }
  search(){
    if (this.searchInput.trim() !== '') {
      this.ser.getInterventionByKeyword(this.searchInput).subscribe(
        (data: Intervention[]) => {
          this.interventions = data;
          this.noResultsFound = this.interventions.length === 0;
        },
        (error) => {
          console.error('Erreur lors de la recherche :', error);
        }
      );
    } else {
      this.getInterventions();
    }
  }
  inter!:Intervention
  selectedPlanningDetails:boolean=false
  selectIntervention(id:number){


    this.ser.getInterventionById(id).subscribe((data)=>{
      this.inter= data;
      this.getChecklist(this.inter.equipement.typeEquipementt.id)
      this.selectedPlanningDetails = true
      console.log(this.inter)
      this.ser._refreshNeeded$.subscribe(()=>{
        this.getChecklist(this.inter.equipement.typeEquipementt.id)



      })
    })

  }
  Glob!:GlobalChecklist

  pointMesure: Checklist[] = [];
  getChecklist(id: number) {
    this.ser.getChecklistOfType(id).subscribe((data) => {
      if (data && data.measurementPoints && data.measurementPoints.length > 0) {
        // Vérifiez si les données reçues sont valides
        console.log("Données reçues :", data);

        this.Glob = data;
        this.pointMesure = this.Glob.measurementPoints;

        // Vérifiez si pointMesure est correctement rempli
        console.log("PointMesure :", this.pointMesure);
      }
    });
  }
  OpenNewIntervention() {
    var popup = this.dialog.open(NewInterventionComponent, {
      width: '60%', height: '569px',
      exitAnimationDuration: '500ms',
      data: { title: 'Interventions' }
    })

  }

  exportToExcel(){

  }
  p:any

  updateInter(){

  }

  deleteInter(id:number){

  }

  addPoint(){

    console.log("typeee", this.inter.equipement.typeEquipementt )
    const popup = this.dialog.open(AddPtToChecklistComponent, {
      width: '40%', height: '426px',
      exitAnimationDuration: '0',
      data: {   typeEquipement: this.inter.equipement.typeEquipementt }

    });
  }

  filterByStatus(status:String){
    this.ser.getInterventionByStatut(status).subscribe(
      (data) => {
        this.interventions = data;
        this.noResultsFound = this.interventions.length === 0;
      },
      (error)=>
      {
        console.log("Error");
      }
    )
  }

  getInterventionByType(type:String){
    this.ser.getInterventionByType(type).subscribe(
      (data) => {
        this.interventions = data;
        this.noResultsFound = this.interventions.length === 0;
      },
      (error)=>
      {
        console.log("Error");
      }
    )
  }

  getInterventionByPriorite(priorite:String){
    this.ser.getInterventionByPriorite(priorite).subscribe(
      (data) => {
        this.interventions = data;
        this.noResultsFound = this.interventions.length === 0;
      },
      (error)=>
      {
        console.log("Error");
      }
    )
  }
}
