import {Salle} from "./salle";
import {TypeEquipement} from "./typeEquipement";

export interface Equipement{
  id: number;
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
