import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {PlanningMaintenanceDTO} from "../../models/PlanningMaintenanceDTO";
import {Site} from "../../models/Site";
import {PlanningServiceService} from "../../../../service/planning-service.service";
import {MatDialog} from "@angular/material/dialog";
import {DialogService} from "../../service/dialog.service";
import {RefrechSerService} from "../../service/refrech-ser.service";
import {NotificationService} from "../../service/notification.service";
import {ActivatedRoute, Router} from "@angular/router";
import {MatSnackBar, MatSnackBarConfig, MatSnackBarRef} from "@angular/material/snack-bar";
import {PlanningdataserviceService} from "../../planningdataservice.service";
import {NewPlanningComponent} from "../../new-planning/new-planning.component";
import {UpdatePlanningComponent} from "../../update-planning/update-planning.component";
import {ImportDialogComponent} from "../../import-dialog/import-dialog.component";
import {responsableDTO} from "../../models/responsableDTO";
import {Intervention} from "../../models/intervention";
import {NewInterventionComponent} from "../new-intervention/new-intervention.component";
import {GlobalChecklist} from "../../models/GlobalChecklist";
import {PointMesure} from "../../models/pointMesure";
import {Checklist} from "../../models/checklist";
import {AddChecklistComponent} from "../../parametres/add-checklist/add-checklist.component";
import {AddPtToChecklistComponent} from "../add-pt-to-checklist/add-pt-to-checklist.component";

@Component({
  selector: 'app-interventions',
  templateUrl: './interventions.component.html',
  styleUrl: './interventions.component.css'
})
export class InterventionsComponent implements OnInit {
  searchInput:String=''
  noResultsFound:boolean= false


  interventions!:Intervention[]
  ngOnInit() {


    this.getInterventions();
    this.getTot()
    this.ser._refreshNeeded$.subscribe(()=>{
      this.getInterventions();
      this.getTot();



    })
  }
  constructor(private  ser:PlanningServiceService, private dialog: MatDialog) {
  }

  tot:number =  0
  getTot(){this.ser.totInterventions().subscribe((data)=>{
    this.tot= data
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
  getInterventions(){

    this.ser.getAllIntervention().subscribe((data)=>{
      this.interventions = data;
       console.log(this.interventions)
    })
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

