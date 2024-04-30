import {Role} from "./Role";

export interface responsableDTO{
  id:number;
    nom:String;
    username:String;
    password:String;
    roles:Role[];

}
