import {Technicein} from "./technicein";

export interface Intervention{
    id:number;
    name:String;
    dateDebut:Date

    dateFin:Date;

    description:String;

    status:String;
    technicien:Technicein

}
