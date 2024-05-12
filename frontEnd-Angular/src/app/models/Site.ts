import {CentreTechnique} from "./centreTechnique";
import {Equipement} from "./equipement";
import {Photo} from "./photo";

export interface Site{
   id:number
    code:String;
   name:String;
   typeSite :String;
  latitude:number;
   longitude:number;
   addresse:String;
   typeInstallation :String;
  typeAlimentation:String;
 typeTransmission:String;
  presenceGESecours:String;
  lieuInsatallationBTS:String;
  hauteurSupportAntenne:String;
  supportAntennes:String
  centreTechnique:CentreTechnique
  equipements:Equipement[];
  photos: Photo[];
  getPhotosImagePaths(): string[];
}
