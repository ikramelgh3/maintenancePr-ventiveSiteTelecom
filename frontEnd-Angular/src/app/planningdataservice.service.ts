import { Injectable } from '@angular/core';
import {PlanningServiceService} from "../../service/planning-service.service";
import {PlanningMaintenanceDTO} from "./models/PlanningMaintenanceDTO";
import {BehaviorSubject} from "rxjs";
import {Site} from "./models/Site";

@Injectable({
  providedIn: 'root'
})
export class PlanningdataserviceService {

  private planningSubject = new BehaviorSubject<PlanningMaintenanceDTO[]>([]);
  public plannings$ = this.planningSubject.asObservable();
  private planningDetailsSubject = new BehaviorSubject<any>(null);
  public planningDetails$ = this.planningDetailsSubject.asObservable();
  private siteSubject = new BehaviorSubject<Site[]>([]);
  public sites$ = this.siteSubject.asObservable();

  private siteDetailsSubject = new BehaviorSubject<any>(null);
  public siteDetails$ = this.siteDetailsSubject.asObservable();

  constructor(private planningService: PlanningServiceService) {}

  refreshSite() {
    this.planningService.getAllSites().subscribe(
      (data) =>{
        console.log("refreeeeessssssssssssssssssssssssssh")
        this.siteSubject.next(data);
      }
    );
  }

  refreshPlannings() {
    this.planningService.getPlannings().subscribe(
      (data) =>{
        this.planningSubject.next(data);
      }
    );
  }

  refreshPlanningDetails(id: number) {
    this.planningService.getPlanningById(id).subscribe(
      (data) => {
        this.planningDetailsSubject.next(data);
      },
      (error) => {
        console.error('Error fetching updated planning details:', error);
      }
    );
  }
  refreshSiteDetails(id: number) {
    this.planningService.getSiteById(id).subscribe(
      (data) => {
        this.siteDetailsSubject.next(data);
      },
      (error) => {
        console.error('Error fetching updated site details:', error);
      }
    );
  }
}
