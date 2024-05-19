import {Salle} from "./salle";
import {TypeEquipement} from "./typeEquipement";
import {Statut} from "./statut";

export interface Equipement{
  id: number;
  code:String;
  numeroSerie: string;
  nom: string;
  descreption: string;
  type: string;
  marque: string;
  statut: string;
  dateMiseService: Date;
  dateMiseHorsService: Date;
  salle:Salle;
  typeEquipementt: TypeEquipement;
}
