import {Equipement} from "./equipement";
import {Etage} from "./etage";

export interface Salle{
  id: number;
  numeroSalle: number;
  capacity: number;
codeSalle:string
  etage:Etage;

}
