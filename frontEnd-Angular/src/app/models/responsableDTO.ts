import {Role} from "./Role";

export interface responsableDTO{
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  userName: string;
  password: string;
  roles: string[];
  roleName:string;
  phone_number:string;
  address:String;
  jobTitle:String;
  available:string;
  city:String;
  //skills: String[];
  type:String;
  enabled:boolean;

}
