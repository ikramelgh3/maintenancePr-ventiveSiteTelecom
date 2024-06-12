import {TypeEquipement} from "./typeEquipement";
import {Checklist} from "./checklist";

export interface  PointMesure{
  id: number;
  attribut: string;
  resultatsPossibles: string[];
  checklists: Checklist[];
  //resultats: Resultat[];
  typeEquipementId: number;
  typeEquipent: TypeEquipement;
}
