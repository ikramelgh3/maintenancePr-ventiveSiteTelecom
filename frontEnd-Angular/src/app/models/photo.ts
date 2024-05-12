import {Site} from "./Site";

export interface Photo{
  id:number;
  name:String;
  type:String;
  picByte:any;
  thumbnail:any
  site:Site | null;
  dateAjout:Date
  imageUrl:String

}
