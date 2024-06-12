import {Technicein} from "./technicein";
import {Equipement} from "./equipement";
import {User} from "./user";
import {PlanningMaintenanceDTO} from "./PlanningMaintenanceDTO";

export interface Intervention{
    id:number;
    name:string;
    dateDebut:Date

    dateFin:Date;
  priorite:String

    description:String;

    status:String;
    technicien:User
planning:PlanningMaintenanceDTO
  type:string,
  equipement:Equipement,
  heureDebut: string
  responsable:User;

}
