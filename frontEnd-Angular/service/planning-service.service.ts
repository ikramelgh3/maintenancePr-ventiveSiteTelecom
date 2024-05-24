import { Injectable } from '@angular/core';
import {HttpClient, HttpClientModule, HttpEvent, HttpEventType, HttpParams, HttpResponse} from "@angular/common/http";
import {BehaviorSubject, catchError, forkJoin, map, Observable, of, Subject, tap, throwError} from "rxjs";
import {PlanningMaintenanceDTO} from "../src/app/models/PlanningMaintenanceDTO";
import { responsableDTO} from "../src/app/models/responsableDTO";
import {Site} from "../src/app/models/Site";
import {Equipement} from "../src/app/models/equipement";
import {Immuble} from "../src/app/models/immuble";
import {Intervention} from "../src/app/models/intervention";
import {Photo} from "../src/app/models/photo";
import {Dr} from "../src/app/models/dr";
import {CentreTechnique} from "../src/app/models/centreTechnique";
import {TypeEquipement} from "../src/app/models/typeEquipement";
import {Etage} from "../src/app/models/etage";
import {Salle} from "../src/app/models/salle";
import {Dc} from "../src/app/models/dc";
import {Checklist} from "../src/app/models/checklist";

@Injectable({
  providedIn: 'root'
})
export class PlanningServiceService {

  private baseUrl = "http://localhost:8888/PLANNING-MAINTENANCE";
  private baseUrlRespo = "http://localhost:8888/USER-SERVICE";
  private baseUrlSite = "http://localhost:8888/SITE-SERVICE";
  private baseUrlIntervention = "http://localhost:8888/INTERVENTION-SERVICE";
  private baseUrlChecklist = "http://localhost:8888/CHECKLIST-SERVICE";

  constructor(private http: HttpClient) {

  }
  private _refreshBeeded$ = new Subject<void>();
  get _refreshNeeded$(){
    return this._refreshBeeded$;
  }
  getPlannings(): Observable<Array<PlanningMaintenanceDTO>> {
    return this.http.get<Array<PlanningMaintenanceDTO>>(this.baseUrl + `/get/all/plannings`);
  }

  addPlanning(formData: PlanningMaintenanceDTO): Observable<any> {
    return this.http.post<any>(this.baseUrl + "/add/planning", formData).pipe(
      catchError(error => {
        return throwError(error);
      })
    );
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


checkCTExist(name: string): Observable<boolean> {
  return this.http.get<boolean>(`${this.baseUrlSite}/existCT/${name}`);
}

  checkPointMesureExist(name: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrlChecklist}/existCT/${name}`);
  }

  checkkIfChecklistExistByName(name:String):Observable<Boolean>{
    return this.http.get<boolean>(`${this.baseUrlChecklist}/check/ptMesure/${name}`);
  }

checkEquipExists(numeroSérie:string,code:String ): Observable<boolean> {
  return this.http.get<boolean>(`${this.baseUrlSite}/exist/${numeroSérie}/${code}`);
}

  checkSiteNameUnique1(code: string, name: string): Promise<boolean | undefined> {
    const url = `${this.baseUrlSite}/existsbyCode/${code}/${name}`;
    return this.http.get<boolean>(url).toPromise();
  }


  checkCTExsit( name: string): Promise<boolean | undefined> {
    const url = `${this.baseUrlSite}/existCT/${name}`;
    return this.http.get<boolean>(url).toPromise();
  }

  checkPointExist( name: string): Promise<boolean | undefined> {
   const url = `${this.baseUrlChecklist}/checklist/exist/${name}`
    return this.http.get<boolean>(url).toPromise();
  }

  checkPointExist1( name: string, id:number):Observable<boolean> {
    const url = `${this.baseUrlChecklist}/checklist/exist/${name}/${id}`
    return this.http.get<boolean>(url);
  }
  updatePlanning(id: number, planning: PlanningMaintenanceDTO , idSite:number, idResp:number): Observable<any> {
    const url = `${this.baseUrl}/updatePlanning/${id}/${idSite}/${idResp}`;
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
    return this.http.post<any>(url, planning).pipe(
      tap(()=>{
        this._refreshBeeded$.next();
      })
    );
  }

  getSize() {
    return this.http.get<number>(this.baseUrl + '/size/plannings');
  }

  getRespoById(id: number): Observable<responsableDTO> {
    const url = `${this.baseUrlRespo}/respo/id/${id}`;
    return this.http.get<responsableDTO>(url);
  }

  getSiteById(id: number): Observable<Site> {
    const url = `${this.baseUrlSite}/site/id/${id}`;
    return this.http.get<Site>(url);
  }
  getPlanningByStatus(status: String): Observable<PlanningMaintenanceDTO[]> {

    const url = `${this.baseUrl}/get/plannings/${status}`;
    return this.http.get<PlanningMaintenanceDTO[]>(url);

  }

  getPlanningBySemestre(semestre:String):Observable<PlanningMaintenanceDTO[]>{
    const  url = `${this.baseUrl}/find/by/semsetre/${semestre}`;
    return this.http.get<PlanningMaintenanceDTO[]>(url);

  }
  getPlanningByTypeSite( type:String) : Observable<PlanningMaintenanceDTO[]>{
    const  url = `${this.baseUrl}/planning/type/${type}`;
    return this.http.get<PlanningMaintenanceDTO[]>(url);
}

getPlanningByKeyword(keyword:String):Observable<PlanningMaintenanceDTO[]>{
    const url = `${this.baseUrl}/find/Plannings/byKeyword/${keyword}`;
    return this.http.get<PlanningMaintenanceDTO[]>(url);
}

  exportToExcel(): Observable<any> {
    const url = `${this.baseUrl}/plannings/export/excel`;
    return this.http.get(url, { responseType: 'blob', observe: 'response' });
  }

  uploadFile(file: File){
    const formData = new FormData();
    formData.append('file', file);
    const url = `${this.baseUrl}/import-planningd`;
    return this.http.post(url, formData);
  }


  importSite(file:File){
    const formData = new FormData();
    formData.append('file', file);
    const url = `${this.baseUrlSite}/import-sites`;
    return this.http.post(url, formData);
  }


  importEquip(file:File){
    const formData = new FormData();
    formData.append('file', file);
    const url = `${this.baseUrlSite}/import-equip`;
    return this.http.post(url, formData);
  }
  checkPlanningNameExists(name: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/check-planning-name-exists/${name}`);
  }
  getEquipementsOfSite(id:number):Observable<Equipement[]>{
    const url = `${this.baseUrlSite}/equipements/ofSITE/${id}`;
    return this.http.get<Equipement[]>(url);
  }
  getImmubleOfSite(id:number):Observable<Immuble[]>{
    const url =`${this.baseUrlSite}/get/immubles/site/${id}`;
    return this.http.get<Immuble[]>(url);
  }



  getPlanningOfSite(id:number):Observable<PlanningMaintenanceDTO[]>{
    const url =`${this.baseUrl}/get/planningOfSite/${id}`;
    return this.http.get<PlanningMaintenanceDTO[]>(url);
  }

  getInterventionOfSite(id:number) : Observable<Intervention[]>{
    const  url = `${this.baseUrl}/intervention/site/${id}`;
    return this.http.get<Intervention[]>(url);
  }

  getNombreSites(): Observable<number>{
     const url = `${this.baseUrlSite}/get/size/sites`;
     return this.http.get<number>(url);
  }

  deleteSite(id:number):Observable<any>{
     const  url = `${this.baseUrlSite}/site/delete/${id}`;
     return  this.http.delete(url);
  }

  getImagePath(id:number):Observable<String[]>{
    const  url = `${this.baseUrlSite}/get/imagePath/${id}`;
    return this.http.get<String[]>(url);
  }


  getImages(id: number): Observable<string[]> {
    return this.http.get<string[]>(`${this.baseUrl}/SITE-SERVICE/image/get/site-service/site-images/${id}`);
  }


  uploadFiles1(files: File[], idSite: number): Observable<string> {
    const formData: FormData = new FormData();
    const  url = `${this.baseUrlSite}/upload/pic/${idSite}`;
    files.forEach(file => {
      formData.append('files', file, file.name);
    });

    return this.http.post<string>(url, formData).pipe(
      tap(()=>{
        this._refreshBeeded$.next();
      })
    );
  }


  uploadFilesEq( idEq: number,files: File[]): Observable<string> {
    const formData: FormData = new FormData();
    const  url = `${this.baseUrlSite}/upload/picE/${idEq}`;
    files.forEach(file => {
      formData.append('files', file, file.name);
    });
    return this.http.post<string>(url, formData).pipe(
      tap(()=>{
        this._refreshBeeded$.next();
      })
    );
  }
  uploadFiles(siteId: number, files: File[]): Observable<string> {
    const formData: FormData = new FormData();
    files.forEach((file: File) => {
      formData.append('files', file, file.name);
    });

    const url = `${this.baseUrlSite}/upload/pic/${siteId}`;
    return this.http.post<string>(url, formData).pipe(
      tap(()=>{
        this._refreshBeeded$.next();
      })
    );
  }

  private  getUploadProgress(event:any):number {
    if(event.type===HttpEventType.UploadProgress){
      const  percentDone = Math.round((event.loaded/event.total)*100);
      return percentDone;
    }
    return 0;
  }
  getAllFiles(id:number):Observable<any>{
    const url = `${this.baseUrlSite}/files/${id}`;
    return this.http.get<any>(url);
  }
  getAllFilesEquipemts(id:number):Observable<any>{
    const url = `${this.baseUrlSite}/filesEq/${id}`;
    return this.http.get<any>(url);
  }
  getImageById(id:number):Observable<any>{
    const  url = `${this.baseUrlSite}/image/imageById/${id}`;
    return this.http.get<any>(url);
  }
  getAllCT():Observable<CentreTechnique[]>{
     const  url = `${this.baseUrlSite}/CT/all`;
     return this.http.get<CentreTechnique[]>(url);
  }

  addCT(formData: Site , idDC: number): Observable<any> {
    const  url = `${this.baseUrlSite}/add/ct/${idDC}`;
    return this.http.post<any>(url, formData).pipe(
      tap(()=>{
        this._refreshBeeded$.next();
      })
    );
  }


  addSiteMobile(formData: Site , idC: number): Observable<any> {
    const  url = `${this.baseUrlSite}/ajouter/site/mobile/${idC}`;
    return this.http.post<any>(url, formData).pipe(
      tap(()=>{
        this._refreshBeeded$.next();
      })
    );
  }

  addSiteFixe(formData: Site ,idC: number): Observable<any> {
    const  url = `${this.baseUrlSite}/ajouter/site/fixe/${idC}`;
    return this.http.post<any>(url, formData).pipe(
      tap(()=>{
        this._refreshBeeded$.next();
      })
    );
  }

  addPointMesure(formData: Site , idType: number): Observable<any> {
    const  url = `${this.baseUrlChecklist}/add/checklist/${idType}`;
    return this.http.post<any>(url, formData).pipe(
      tap(()=>{
        this._refreshBeeded$.next();
      })
    );
  }

  checkSiteNameUnique(code:String, name:String):Observable<boolean>{
    const  url = `${this.baseUrlSite}/existsbyCode/${code}/${name}`;
    return  this.http.get<boolean>(url);
  }
  checkSiteCodeUnique(name:String):Observable<Boolean>{
    const  url = `${this.baseUrlSite}/existsbyCode/${name}`;
    return  this.http.get<Boolean>(url);
  }

  getCentreTehniwueById(id:number):Observable<CentreTechnique>{
     const  url = `${this.baseUrlSite}/CT/${id}`;
      return this.http.get<CentreTechnique>( url);
  }


  getChecklistById(id:number):Observable<Checklist>{
    const  url = `${this.baseUrlChecklist}/checklist/id/${id}`;
    return this.http.get<Checklist>( url);
  }
  updateSiteFixe(id: number, updatedSite: Site , idC:number): Observable<Site> {
    return this.http.put<Site>(`${this.baseUrlSite}/updatte/siteFixe/${id}/${idC}`, updatedSite).pipe(
      tap(()=>{
        this._refreshBeeded$.next();

      })
    );
  }

  updateSiteMobile(id: number, updatedSite: Site , idC:number): Observable<Site> {
    return this.http.put<Site>(`${this.baseUrlSite}/update/siteMobile/${id}/${idC}`, updatedSite).pipe(
      tap(()=>{
        this._refreshBeeded$.next();

      })
    );
  }

  getPathImage(idSite:number):Observable<String[]>{
     const  url = `${this.baseUrlSite}/getPath/${idSite}`;
     return  this.http.get<String[]>(url);
  }

  deletePicOOfSite(id:number):Observable<any>{
     const  url =`${this.baseUrlSite}/delete/pic/${id}`;
     return this.http.delete<any>(url);
  }
  getTotal():Observable<number>{
     const  url =`${this.baseUrlSite}/getTotal`;
     return  this.http.get<number>( url);
  }
  getSiteByType(type:String):Observable<Site[]>{
     const url =`${this.baseUrlSite}/get/sites/type/${type}`;
     return this.http.get<Site[]>(url);
  }

  getSitseByKeyword(keyword:String):Observable<Site[]>{
     const  url = `${this.baseUrlSite}/findSite/byKeyword/${keyword}`;
     return this.http.get<Site[]>(url);
  }

  exporterSites():Observable<any>{
     const  url = `${this.baseUrlSite}/sites/export/excel`;
     return  this.http.get(url ,{ responseType: 'blob', observe: 'response' });
  }
  getEquipements():Observable<Equipement[]>{
    const url=`${this.baseUrlSite}/equipement/all`;
     return  this.http.get<Equipement[]>(url);
  }

  getLocalisatonOfEqui(id:number):Observable<String>{
     const  url = `${this.baseUrlSite}/getLocalOfSite/${id}`;
      return this.http.get<String>(url);
  }

  getNbreEqui():Observable<number>{
     const  url = `${this.baseUrlSite}/nbre/equi`;
      return this.http.get<number>(url);
  }
  getEquiById(id:number):Observable<Equipement>{
     const  url = `${this.baseUrlSite}/equipement/${id}`;
     return this.http.get<Equipement>(url);
  }
  getInterventionOfEquipement(id:number):Observable<Intervention[]>{
     const  url = `${this.baseUrlIntervention}/get/interventions/ofEqui/${id}`
    return  this.http.get<Intervention[]>(url);
  }
  getAllTypeEquip():Observable<TypeEquipement[]>{
    const  url = `${this.baseUrlSite}/All/type/equipements`
    return this.http.get<TypeEquipement[]>(url);

  }

  addNewEquipemnt(formData: Equipement,idTy:number, idSalle:number ):Observable<any >{
    const  url = `${this.baseUrlSite}/equipement/add/${idTy}/${idSalle}`
    return this.http.post<any>(url , formData).pipe(
      tap(()=>{
        this._refreshBeeded$.next();
      })
    );
  }
  getIdSalleByCodeSalle(code:string):Observable<Salle>{
     const  url= `${this.baseUrlSite}/getId/${code}`;
      return  this.http.get<Salle>(url)
  }
  getIdByCode(code:string):Observable<number>{
    const  url= `${this.baseUrlSite}/getIdSalle/${code}`;
    return  this.http.get<number>(url)
  }
  getIdTypeByName(name:String):Observable<number>{
     const url = `${this.baseUrlSite}/getByName/${name}`
    return this.http.get<number>(url)
  }
  deleteEquipemnt(id:number):Observable<any>{
    const  url =`${this.baseUrlSite}/equipement/delete/${id}`;
    return this.http.delete<any>(url).pipe(
      tap(()=>{
        this._refreshBeeded$.next();
      })
    );
  }
  updateEquiepement(id: number, equipement: Equipement , idtype:number, idSalle:number): Observable<any> {
    const url = `${this.baseUrlSite}/updatte/equipement/${id}/${idtype}/${idSalle}`;
    return this.http.put<any>(url, equipement).pipe(
      tap(()=>{
        this._refreshBeeded$.next();

      })
    );
  }

getTypeEQUIById(id: number): Observable<TypeEquipement> {
  const url = `${this.baseUrlSite}/findTypeEquimeent/${id}`;
  return this.http.get<TypeEquipement>(url);
}
getSalleById(id:number):Observable<Salle>{
  const url = `${this.baseUrlSite}/salle/${id}`;
  return this.http.get<Salle>(url);
}
checkifEquipeExisteByCode(code:string ,id:number):Observable<Boolean>{
    const  url= `${this.baseUrlSite}/existeEquipemnt/${code}/${id}`;
     return this.http.get<Boolean>(url)
}
  deletePicOfEquipement(id:number):Observable<any>{
    const  url =`${this.baseUrlSite}/deletePicEquipement/${id}`;
    return this.http.delete<any>(url).pipe(
      tap(()=>{
        this._refreshBeeded$.next();

      })
    );
  }
  exporterEquipements():Observable<any>{
    const  url = `${this.baseUrlSite}/equipements/export/excel`;
    return  this.http.get(url ,{ responseType: 'blob', observe: 'response' });
  }
  checkIfEquipementHorsService(id:number): Observable<Boolean>
  {const url =`${this.baseUrlSite}/check/equipement/horsService/${id}`
    return  this.http.get<Boolean>(url);
  }

  getEquipementByKeyword(keyword:String):Observable<Equipement[]>{
    const  url = `${this.baseUrlSite}/findEquipement/byKeyword/${keyword}`;
    return this.http.get<Equipement[]>(url);
  }
  checkifSiteExisteByCode(code:string ,id:number):Observable<Boolean>{
    const  url= `${this.baseUrlSite}/existeSite/${code}/${id}`;
    return this.http.get<Boolean>(url)
  }

  checkifSiteExisteByName(name:string ,id:number):Observable<Boolean>{
    const  url= `${this.baseUrlSite}/existeSiteName/${name}/${id}`;
    return this.http.get<Boolean>(url)
  }

  getImmublesOfSite(id:number):Observable<Immuble[]>{
    const  url= `${this.baseUrlSite}/getImmubles/ofSite/${id}`;
    return this.http.get<Immuble[]>(url)
  }

  getSalleOfEtage(id:number):Observable<Salle[]>{
    const  url= `${this.baseUrlSite}/getSalle/ofEtage/${id}`;
    return this.http.get<Salle[]>(url)
  }


  getEtageOfImmuble(id:number):Observable<Etage[]>{
    const url =`${this.baseUrlSite}/etageg/byImmuble/${id}`;
    return this.http.get<Etage[]>(url);
  }

  getSalleOfSite(id:number):Observable<Salle[]>{
    const  url= `${this.baseUrlSite}/getSalle/ofSite/${id}`;
    return this.http.get<Salle[]>(url).pipe(
      catchError(error => {
        return throwError(error);
      })
    );
  }

  importCentreTechnique(file:File){
    const formData = new FormData();
    formData.append('file', file);
    const url = `${this.baseUrlSite}/import-ct`;
    return this.http.post(url, formData).pipe(
      tap(()=>{
        this._refreshBeeded$.next();

      })
    );
  }
  deleteCentreTechnique(centreTechnique: CentreTechnique): Observable<any> {
    const url = `${this.baseUrlSite}/CT`;
    return this.http.delete(url, { body: centreTechnique });
  }

  deletePointMeusre(checklist: Checklist): Observable<any> {
    const url = `${this.baseUrlChecklist}/ptM`;
    return this.http.delete(url, { body: checklist });
  }
  getDR():Observable<Dr[]>{
    const url =`${this.baseUrlSite}/all/DR`;
    return this.http.get<Dr[]>(url);
  }

  getDCsbyDR(id:number):Observable<Dc[]>{
    const url =`${this.baseUrlSite}/dc/bydr/${id}`;
    return this.http.get<Dc[]>(url);
  }
  getDRById(id:number):Observable<Dr>{
    const url =`${this.baseUrlSite}/Dr/${id}`;
    return this.http.get<Dr>(url);
  }



updateCentre(id: number, updatedSite: CentreTechnique): Observable<CentreTechnique> {
  return this.http.put<CentreTechnique>(`${this.baseUrlSite}/update/centre/${id}`, updatedSite).pipe(
    tap(()=>{
      this._refreshBeeded$.next();

    })
  );
}



  updateChecklist(id: number, updatedSite: Checklist): Observable<CentreTechnique> {
    return this.http.put<CentreTechnique>(`${this.baseUrlChecklist}/update/pointMesure/${id}`, updatedSite).pipe(
      tap(()=>{
        this._refreshBeeded$.next();

      })
    );
  }


  checkPointExist2(attribut: string, id: number): Observable<boolean> {
    const params = new HttpParams()
      .set('name', encodeURIComponent(attribut))
      .set('id', id.toString());
    return this.http.get<boolean>(`${this.baseUrlChecklist}/checklist/exist`, { params });
  }

  checkIfExisteCT(name:String,idDC:number, idDR:number):Observable<boolean>{
    const url =`${this.baseUrlSite}/exists/${name}/${idDC}/${idDR}`;
    return this.http.get<boolean>(url);
  }


  importChecklist(file:File){
    const formData = new FormData();
    formData.append('file', file);
    const url = `${this.baseUrlChecklist}/import-checklists`;
    return this.http.post(url, formData);
  }
  getChecklist():Observable<Checklist[]>{
    const url =`${this.baseUrlChecklist}/checklist/all`;
    return this.http.get<Checklist[]>(url);
  }



}
