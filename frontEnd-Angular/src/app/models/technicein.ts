import {Role} from "./Role";

export interface Technicein{
  id:number;
  nom:String;
  username:String;
  password:String;
  roles:Role[];
}
