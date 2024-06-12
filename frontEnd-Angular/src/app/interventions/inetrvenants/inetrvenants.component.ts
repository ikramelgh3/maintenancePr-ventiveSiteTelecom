import {Component, OnInit} from '@angular/core';
import {PlanningServiceService} from "../../../../service/planning-service.service";
import {User} from "../../models/user";
import {Intervention} from "../../models/intervention";
import {AddPtToChecklistComponent} from "../add-pt-to-checklist/add-pt-to-checklist.component";
import {MatDialog} from "@angular/material/dialog";
import {AddInterventionToUserComponent} from "../../users/add-intervention-to-user/add-intervention-to-user.component";

@Component({
  selector: 'app-inetrvenants',
  templateUrl: './inetrvenants.component.html',
  styleUrl: './inetrvenants.component.css'
})
export class InetrvenantsComponent  implements  OnInit{


  tot:number=0
  users!:User[]
  p:any
  selectedUser:boolean= false
  searchInput:string=''
  noResultsFound:boolean = false
  user!:User
  id!:string
  search(): void {
    if (this.searchInput.trim() !== '') {
      this.ser.getUserByUsername(this.searchInput).subscribe(
        (data: User[]) => {
          this.users = data;
          this.noResultsFound = this.users.length === 0;
        },
        (error) => {
          console.error('Erreur lors de la recherche :', error);
        }
      );
    } else {
      this.getAllTechn();
    }
  }
  constructor(private  ser:PlanningServiceService, private dialog: MatDialog) {
  }
  ngOnInit() {
    this.getAllTechn()
    this.getTot()

    this.ser._refreshNeeded$.subscribe(()=>{
     this.getUserById(this.id)

    })
  }

  getTot(){
    this.ser.totoTech().subscribe((data)=>{
      this.tot=data
      if(this.tot===0){
        this.noResultsFound=true
      }
    })
  }
  getAllTechn(){
    this.ser.getAlltechniciens().subscribe((data)=>{
      this.users= data;
    })
  }

  getUserById(id:string){
    this.ser.getUserById(id).subscribe((data)=>{
      this.user = data;
      this.id=id
      this.selectedUser = true;
      this.getIntervention(this.user.id);
      console.log(this.user);
    })
  }


  getTechnicienInterene(){
this.ser.getAlltechniciensInterns().subscribe((data)=>{
  this.users = data;
  this.noResultsFound = this.users.length === 0;
})
  }
  getTechnicienExterne(){
    this.ser.getAlltechniciensExterne().subscribe((data)=>{
      this.users = data;
      this.noResultsFound = this.users.length === 0;
    })
  }

  getTechnicienDisp(){
    this.ser.getDisponibleTech().subscribe((data)=>{
      this.users = data;
      this.noResultsFound = this.users.length === 0;
    })
  }

  getNonDisp(){
    this.ser.getNonDisponibleTech().subscribe((data)=>{
      this.users = data;
      this.noResultsFound = this.users.length === 0;
    })
  }

  InterventionTech!:Intervention[]
  not_foundInterv:boolean=false
  getIntervention(id:string){
    this.ser.getInterventionOfTechnicein(id).subscribe((data)=>{
      this.InterventionTech = data

      this.not_foundInterv = this.InterventionTech.length === 0;
      console.log(this.InterventionTech)
    })
  }




addIntervention(){

    console.log("typeee", this.user.id )
    const popup = this.dialog.open(AddInterventionToUserComponent, {
      width: '60%', height: '569px',
      exitAnimationDuration: '0',
      data: {   user2:  this.user }

    });
  }
}
