import {Etage} from "./etage";
import {Site} from "./Site";

export interface Immuble{
  id: number;
  name: string;
  addr: string;
  codeImmuble:string
  site:Site
  etages:Etage[]
}
