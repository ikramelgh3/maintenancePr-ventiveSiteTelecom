import {responsableDTO} from "./responsableDTO";
import {Site} from "./Site";
import {Intervention} from "./intervention";

export interface PlanningMaintenanceDTO{

   id:number;
  name: string ;
  dateDebutRealisation: Date ;
  dateFinRealisation: Date ;
  semestre: String ;
   description:String;
    dateCreation:Date;
    status:String;
    id_Respo:string;
  id_Site:number;
  responsableMaint:responsableDTO;
  site:Site;
  interventions:Intervention[];

}
