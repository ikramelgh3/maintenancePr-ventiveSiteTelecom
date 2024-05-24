import {TypeEquipement} from "./typeEquipement";
import {responsableDTO} from "./responsableDTO";

export  interface Checklist{
  id: number;
  attribut: string;
  resultatsPossibles: string[];
  //resultats: Resultat[];
  respoId: number;
  respoMaint: responsableDTO;
  typeEquipementId: number;
  typeEquipent: TypeEquipement;
}
