import {Component, OnInit} from '@angular/core';
import {PlanningServiceService} from "../../../../service/planning-service.service";
import {Equipement} from "../../models/equipement";
import {Intervention} from "../../models/intervention";

@Component({
  selector: 'app-equiipements',
  templateUrl: './equiipements.component.html',
  styleUrl: './equiipements.component.css'
})
export class EquiipementsComponent implements  OnInit{

  equipements!:Equipement[];
  equipement!:Equipement;
  nbreTot!:number
  searchInput:String = ''
  intervention!:Intervention[]
  selectedEquipDetails: Boolean = false;
  p:any
  Localisation!: String
  constructor(private ser:PlanningServiceService) {
  }
  ngOnInit() {
this.getNbreTotal();
    this.getEquip();

  }

  getEquip(){
     this.ser.getEquipements().subscribe((data)=>{
       this.equipements = data;
       console.log(this.equipements)
     })
  }
  search(){

  }

  getLocalisationOfEqui(id:number){
    this.ser.getLocalisatonOfEqui(id).subscribe((data)=>{
      this.Localisation = data;
      console.log(this.Localisation);
    })
  }

  getEquipementById(id:number){
    this.selectedEquipDetails = true;
    this.ser.getEquiById(id).subscribe((data)=>{
      this.equipement = data;
      console.log(this.equipement);
    })
   this.getInterventionOfEquipement(id)
  }
  getNbreTotal(){
     this.ser.getNbreEqui().subscribe((data)=>{
        this.nbreTot = data;
        console.log((this.nbreTot))
     })
  }

  getStatusColor(status: String): String {
    switch (status) {
      case 'En service':
        return '#4CAF50';
        case 'En maintenance':
        return '#2196F3'; // Vert
      case 'Hors service':
        return '#FF0000'; // Jaune
      default:
        return '#333'; // Couleur par défaut
    }
  }

  getInterventionOfEquipement(id:number){
     this.ser.getInterventionOfEquipement(id).subscribe((data)=>
     {
        this.intervention = data;
        console.log(this.intervention)
     })
  }

}
