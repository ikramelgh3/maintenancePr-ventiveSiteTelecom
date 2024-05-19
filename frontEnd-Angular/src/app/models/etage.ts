import {Salle} from "./salle";
import {Immuble} from "./immuble";

export interface Etage{
  id: number;
  numeroEtage: number;
  immuble:Immuble;

  codeEtage:String
}
