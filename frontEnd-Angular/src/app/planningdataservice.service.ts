import { Injectable } from '@angular/core';
import {PlanningServiceService} from "../../service/planning-service.service";
import {PlanningMaintenanceDTO} from "./models/PlanningMaintenanceDTO";
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class PlanningdataserviceService {

  private planningSubject = new BehaviorSubject<PlanningMaintenanceDTO[]>([]);
  public plannings$ = this.planningSubject.asObservable();
  private planningDetailsSubject = new BehaviorSubject<any>(null);
  public planningDetails$ = this.planningDetailsSubject.asObservable();

  constructor(private planningService: PlanningServiceService) {}

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
}
