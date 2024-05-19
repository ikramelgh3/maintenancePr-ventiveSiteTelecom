import {Component, OnInit} from '@angular/core';
import {PlanningServiceService} from "../../../../service/planning-service.service";
import {Equipement} from "../../models/equipement";
import {Intervention} from "../../models/intervention";
import {TypeEquipement} from "../../models/typeEquipement";
import {NewPlanningComponent} from "../../new-planning/new-planning.component";
import {NewEquipementComponent} from "../new-equipement/new-equipement.component";
import {MatDialog} from "@angular/material/dialog";
import {MatSnackBar, MatSnackBarRef} from "@angular/material/snack-bar";
import {DialogService} from "../../service/dialog.service";
import {UpdatePlanningComponent} from "../../update-planning/update-planning.component";
import {UpdateEquipementComponent} from "../update-equipement/update-equipement.component";
import {OpenPicSiteComponent} from "../../openPic/open-pic-site/open-pic-site.component";
import {AddPictoSiteComponent} from "../../add-picto-site/add-picto-site.component";
import {OpenImgaeComponent} from "../open-imgae/open-imgae.component";
import {AddPicComponent} from "../add-pic/add-pic.component";
import {Site} from "../../models/Site";

@Component({
  selector: 'app-equiipements',
  templateUrl: './equiipements.component.html',
  styleUrl: './equiipements.component.css'
})
export class EquiipementsComponent implements  OnInit{

  equipements!:Equipement[];
  equipement!:Equipement;
  nbreTot!:number
  searchInput:String = ''
  intervention!:Intervention[]
  selectedEquipDetails: Boolean = false;
  not_foundInterv: Boolean = false;
  not_foundImg:Boolean=false
  p:any
  id!:number
  Localisation!: String
  constructor(private ser:PlanningServiceService  ,private dialogService: DialogService,  private dialog: MatDialog ,private snackBar: MatSnackBar) {
  }
  ngOnInit() {
this.getNbreTotal();
    this.getEquip();
    this.ser._refreshNeeded$.subscribe(()=>{
      this.getNbreTotal();
      this.getEquip();
      this.getAllFiles();
    })
  }
  getEquip(){
     this.ser.getEquipements().subscribe((data)=>{
       this.equipements = data;
       console.log(this.equipements)
     })
  }


  getLocalisationOfEqui(id:number){
    this.ser.getLocalisatonOfEqui(id).subscribe((data)=>{
      this.Localisation = data;
      console.log(this.Localisation);
    })
  }

  getEquipementById(id:number){
    this.selectedEquipDetails = true;
    this.id=id;
    this.ser.getEquiById(id).subscribe((data)=>{
      this.equipement = data;
      this.getAllFiles();
      this.getInterventionOfEquipement(this.id)
      console.log(this.equipement);
    })

    this.checkIfEquipementHorsService(this.id);
  }
  getNbreTotal(){
     this.ser.getNbreEqui().subscribe((data)=>{
        this.nbreTot = data;
        console.log((this.nbreTot))
     })
  }

  getStatusColor(status: String): String {
    switch (status) {
      case 'En service':
        return '#4CAF50';
        case 'En maintenance':
        return '#2196F3'; // Vert
      case 'Hors service':
        return '#FF0000'; // Jaune
      default:
        return '#333'; // Couleur par défaut
    }
  }

  getStatusColorIntervention(status:String ) {
  switch (status) {
  case "PLANIFIEE":
    return "#1E90FF"; // Bleu
  case "EN_COURS":
    return "#FFA500"; // Orange
  case "TERMINEE":
    return "#32CD32"; // Vert
  case "ANNULEE":
    return "#FF4500"; // Rouge
  case "REPORTEE":
    return "#FFD700"; // Jaune
  default:
    return "#333333"; // Couleur par défaut (Gris foncé)
  }
}
  getInterventionOfEquipement(id:number){
     this.ser.getInterventionOfEquipement(id).subscribe((data)=>
     {
        this.intervention = data;
       this.not_foundInterv=false
       if(this.intervention.length===0){
         this.not_foundInterv =true
       }
        console.log(this.intervention)
     })
  }

  addEquipet(){
    var popup = this.dialog.open(NewEquipementComponent, {
      width: '50%', height: '540px',
      exitAnimationDuration: '500ms',
      data: { title: 'Equipement' }
    })
  }

  deleteEquipemt(id: number) {
    this.dialogService.openConfirmDialof('Voulez vous supprimer ce site?').afterClosed()
      .subscribe(res => {
        if (res) {
          this.ser.deleteEquipemnt(id).subscribe(
            () => {
              console.log('equipement deleted successfully');
              this.selectedEquipDetails = false;
              const snackBarRef: MatSnackBarRef<any> = this.snackBar.open('L\'équipement a été supprimé', '', {
                duration: 8000,
                horizontalPosition: 'right',
                verticalPosition: 'top',
              });

              snackBarRef.onAction().subscribe(() => {
                snackBarRef.dismiss();
              });
            },
            error => {
              console.error('Error deleting equipement:', error);
            }
          );
        }
      });
  }
  UpdateEquipement(){
    const popup = this.dialog.open(UpdateEquipementComponent, {
      width: '50%', height: '550px',
      exitAnimationDuration: '0',
      data: { id: this.id }
    });
    this.ser._refreshNeeded$.subscribe(()=>{
      this.getEquipementById(this.equipement.id);
    })
  }


  selectedFile!: File;
  uploadProgress!: number;

  onFileSelected(event: any) {

    const fileList: FileList = event.target.files;
    if (fileList && fileList.length > 0) {
      this.selectedFile = fileList[0];
    }

  }
  deletePic(id:number){
    this.dialogService.openConfirmDialof('Voulez vous supprimer cet photo?').afterClosed()
      .subscribe(res => {
        if (res) {
          this.ser.deletePicOfEquipement(id).subscribe(
            () => {
              console.log('photo deleted successfully');
              this.getAllFiles()
              const snackBarRef: MatSnackBarRef<any> = this.snackBar.open('La photo est bien supprimée', '', {
                duration: 8000,
                horizontalPosition: 'right',
                verticalPosition: 'top',
              });

              snackBarRef.onAction().subscribe(() => {
                snackBarRef.dismiss();
              });
            },
            error => {
              console.error('Error deleting planning:', error);
            }
          );
        }
      });
  }




  files: any = [];
  file!: any;
  sourceImg !: any;

  getAllFiles(): void {
    this.ser._refreshNeeded$.subscribe(()=>{
      this.getAllFiles();
    })
    const id = this.equipement.id; // Récupérer l'ID du site directement
    console.log("id equip", id)
    this.ser.getAllFilesEquipemts(id).subscribe((response: any[]) => {
      console.log(response);
      // Réinitialiser la variable not_foundImg à false
      this.not_foundImg = false;
      this.files = response.map(file => ({
        ...file,
        processedImage: "data:image/jpg;base64," + file.picByte // Utiliser picByte pour créer l'URL base64
      }));
      // Vérifier si la liste des fichiers est vide
      if (this.files.length === 0) {
        // Définir la variable not_foundImg à true si aucune photo n'est trouvée
        this.not_foundImg = true;
      }
      console.log("files",this.files)
    });
  }
  openPict(imageSrc: string) {
    var popup = this.dialog.open(OpenImgaeComponent, {

      exitAnimationDuration: '500ms',
      data: {
        imageSrc: imageSrc
      }
    })

  }

  addPict(id:number){
    var popup = this.dialog.open(AddPicComponent, {
      width: '50%', height: '599px',
      exitAnimationDuration: '500ms',
      data: {id: id }
    })
  }



    getCurrentDate(): string {
      const currentDate = new Date();
      const year = currentDate.getFullYear();
      const month = (currentDate.getMonth() + 1).toString().padStart(2, '0');
      const day = currentDate.getDate().toString().padStart(2, '0');
      return `${year}-${month}-${day}`;
    }

  getFileNameFromResponse(response: any): string {
    const contentDispositionHeader = response.headers.get('Content-Disposition');
    const fileNameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
    const matches = fileNameRegex.exec(contentDispositionHeader);
    if (matches != null && matches[1]) {
      return matches[1].replace(/['"]/g, '');
    }
    return `equipements${this.getCurrentDate()}.xlsx`; // Fallback filename if the filename cannot be extracted
  }

  downloadFile(data: any, fileName: string) {
    const blob = new Blob([data], { type: 'application/octet-stream' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    document.body.appendChild(a);
    a.style.display = 'none';
    a.href = url;
    a.download = fileName;
    a.click();
    window.URL.revokeObjectURL(url);
  }
  exporeterEquipements(){

      this.ser.exporterSites().subscribe(
        (response: any) => {
          const fileName = this.getFileNameFromResponse(response);
          this.downloadFile(response.body, fileName);
          const snackBarRef: MatSnackBarRef<any> = this.snackBar.open('Les équipements sont bien exporter', '', {
            duration: 8000,
            horizontalPosition: 'right',
            verticalPosition: 'top',
          });

          snackBarRef.onAction().subscribe(() => {
            snackBarRef.dismiss();
          });
        },
        (error) => {
          console.error('Erreur lors de l\'exportation vers Excel : ', error);
        }
      );

  }
  horsService:Boolean = false
  checkIfEquipementHorsService(id:number){
     this.ser.checkIfEquipementHorsService(id).subscribe((data)=>{
        this.horsService= data;
        if(this.horsService===true){
          this.horsService = true
        }
        else {
           this.horsService=false;
        }
        console.log(this.horsService)
     })
  }

  noResultsFound:Boolean= false
  search(): void {
    if (this.searchInput.trim() !== '') {
      this.ser.getEquipementByKeyword(this.searchInput).subscribe(
        (data: Equipement[]) => {
          this.equipements = data;
          this.noResultsFound = this.equipements.length === 0;
        },
        (error) => {
          console.error('Erreur lors de la recherche :', error);
        }
      );
    } else {
      this.getEquip();
    }
  }
}
