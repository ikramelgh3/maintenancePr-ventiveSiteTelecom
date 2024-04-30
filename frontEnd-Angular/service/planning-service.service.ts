import { Injectable } from '@angular/core';
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {BehaviorSubject, catchError, Observable, throwError} from "rxjs";
import {PlanningMaintenanceDTO} from "../src/app/models/PlanningMaintenanceDTO";
import { responsableDTO} from "../src/app/models/responsableDTO";
import {Site} from "../src/app/models/Site";

@Injectable({
  providedIn: 'root'
})
export class PlanningServiceService {

  private baseUrl = "http://localhost:8888/PLANNING-MAINTENANCE";
  private baseUrlRespo = "http://localhost:8888/USER-SERVICE";
  private baseUrlSite = "http://localhost:8888/SITE-SERVICE";

  constructor(private http: HttpClient) {

  }

  getPlannings(): Observable<Array<PlanningMaintenanceDTO>> {
    return this.http.get<Array<PlanningMaintenanceDTO>>(this.baseUrl + `/get/all/plannings`);
  }

  addPlanning(formData: PlanningMaintenanceDTO): Observable<any> {
    return this.http.post<any>(this.baseUrl + "/add/planning", formData);
  }

  public deletePlanning(id: number): Observable<any> {
    const url = `${this.baseUrl}/delete/planning/${id}`; // Utilisez l'ID dans l'URL
    return this.http.delete(url);
  }

  public getPlanningById(id: number): Observable<any> {
    const url = `${this.baseUrl}/find/planning/${id}`;
    return this.http.get(url);
  }

  checkPlanningExists(name: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/check-existence/${name}`);
  }

  updatePlanning(id: number, planning: PlanningMaintenanceDTO): Observable<any> {
    const url = `${this.baseUrl}/updatePlanning/${id}`;
    return this.http.patch<any>(url, planning).pipe(
      catchError(error => {
        return throwError(error);
      })
    );
  }

  private selectedPlanningSubject = new BehaviorSubject<PlanningMaintenanceDTO | null>(null);
  selectedPlanning$ = this.selectedPlanningSubject.asObservable();

  updateSelectedPlanning(planning: PlanningMaintenanceDTO) {
    this.selectedPlanningSubject.next(planning);
  }

  getAllResponsable(): Observable<responsableDTO[]> {
    return this.http.get<responsableDTO[]>(this.baseUrlRespo + "/all/Respo");
  }

  getAllSites(): Observable<Site[]> {
    return this.http.get<Site[]>(this.baseUrlSite + "/sites");
  }

  addPlanningComplet(planning: PlanningMaintenanceDTO, idRes: number, idSite: number): Observable<any> {
    const url = `${this.baseUrl}/add/planningComplet/${idRes}/${idSite}`;
    return this.http.post<any>(url, planning);
  }

  getSize() {
    return this.http.get<number>(this.baseUrl + '/size/plannings');
  }

  getRespoById(id: number): Observable<responsableDTO> {
    const url = `${this.baseUrlRespo}/respo/id/${id}`;
    return this.http.get<responsableDTO>(url);
  }

  getPlanningByStatus(status: String): Observable<PlanningMaintenanceDTO[]> {

    const url = `${this.baseUrl}/get/plannings/${status}`;
    return this.http.get<PlanningMaintenanceDTO[]>(url);

  }

  getPlanningByTypeSite( type:String) : Observable<PlanningMaintenanceDTO[]>{
    const  url = `${this.baseUrl}/planning/type/${type}`;
    return this.http.get<PlanningMaintenanceDTO[]>(url);
}

}
