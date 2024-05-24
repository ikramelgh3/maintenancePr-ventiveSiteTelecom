import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Site} from "../models/Site";
import {PlanningServiceService} from "../../../service/planning-service.service";
import {Equipement} from "../models/equipement";
import {error} from "@angular/compiler-cli/src/transformers/util";
import {PlanningMaintenanceDTO} from "../models/PlanningMaintenanceDTO";
import {Intervention} from "../models/intervention";
import {MatSnackBar, MatSnackBarRef} from "@angular/material/snack-bar";
import {DialogService} from "../service/dialog.service";
import {PlanningdataserviceService} from "../planningdataservice.service";
import {NewPlanningComponent} from "../new-planning/new-planning.component";
import {MatDialog} from "@angular/material/dialog";
import {NewSiteComponent} from "../new/new-site/new-site.component";
import {OpenPicSiteComponent} from "../openPic/open-pic-site/open-pic-site.component";
import {Route, Router} from "@angular/router";
import {AddPictoSiteComponent} from "../add-picto-site/add-picto-site.component";
import {UpdatePlanningComponent} from "../update-planning/update-planning.component";
import {UpdateSiteComponent} from "../update-site/update-site.component";
import {Observable} from "rxjs";
import {Immuble} from "../models/immuble";
import {Etage} from "../models/etage";
import {Salle} from "../models/salle";
import {NewEquipementComponent} from "../equipements/new-equipement/new-equipement.component";
import {AddEquipemntToSiteComponent} from "./add-equipemnt-to-site/add-equipemnt-to-site.component";
import {AddPlanningtoSiteComponent} from "./add-planningto-site/add-planningto-site.component";
import {DetailOfSousLiieuxComponent} from "./detail-of-sous-liieux/detail-of-sous-liieux.component";

@Component({
  selector: 'app-site',
  templateUrl: './site.component.html',
  styleUrl: './site.component.css'
})
export class SiteComponent implements  OnInit {

  p: any;
  sites!: Site[];
  searchInput: string = '';
  selectedSiteDetails: Boolean = false;
  slSite: any;
  SiteDetails!: Site;
  equipements!: Equipement[];
  not_found: Boolean = false;
  not_foundPl: Boolean = false;
  plannings!: PlanningMaintenanceDTO[];
  Interventions!: Intervention[];
  not_foundImg:Boolean= false;
  not_foundInterv: Boolean = false;
  nbrePlanning: number = 0;
   totalItems!:number ;
  images: any;
  immubles!:Immuble[];
  //@ViewChild('siteDetails') siteDetails!: ElementRef;

  constructor(private rout:Router,private ser: PlanningServiceService, private dialog: MatDialog, private dataser: PlanningdataserviceService, private dialogService: DialogService, private snackBar: MatSnackBar) {


  }

  ngOnInit() {
    // Récupérer la liste initiale des sites
    this.getSites();
    this.getTotalElment();
    //this.getAllFiles();
  this.ser._refreshNeeded$.subscribe(()=>{
    this.getSites();
    this.getTotalElment();
    this.getEquipementsOfSite(this.SiteDetails.id)
    this.getPlanningsOfSite(this.SiteDetails.id)

  })
  }

  getTotalElment(){
    this.ser.getTotal().subscribe((data)=>
      this.totalItems=data,
    )

  }

  getSites() {
    this.ser.getAllSites().subscribe((data) => {
        this.sites = data;
        console.log(this.sites);

      },
      (error) => {
        console.log(error)
      })
  }


  referechDate() {
    this.ser.getAllSites().subscribe((res: any) => {
      this.sites = res;
      this.nbrePlanning = this.sites.length;
    });
  }

  getSiteById(id: number) {
    this.ser.getSiteById(id).subscribe((data) => {
        this.SiteDetails = data;
        this.getImmublesOfSite(id);
        this.getAllFiles();
       // this.getEtageOfImmuble(id);
        //this.getSalleOfETAGE(id);
        this.getEquipementsOfSite(id);
        this.getPlanningsOfSite(id);
        this.getInterventionOfSite(id);
        this.selectedSiteDetails = true;

        console.log(this.SiteDetails);
      },
      (error) => {
        console.log(error);

      })
  }
  noResultsFound:Boolean = false;
  getSiteByType(type:String){
    this.ser.getSiteByType(type).subscribe(
      (data) => {
        this.sites = data;
        this.noResultsFound = this.sites.length === 0;

      },
      (error)=>
      {
        console.log("Error");
      }
    )
  }

  getFileNameFromResponse(response: any): string {
    const contentDispositionHeader = response.headers.get('Content-Disposition');
    const fileNameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
    const matches = fileNameRegex.exec(contentDispositionHeader);
    if (matches != null && matches[1]) {
      return matches[1].replace(/['"]/g, '');
    }
    return `sites_${this.getCurrentDate()}.xlsx`; // Fallback filename if the filename cannot be extracted
  }

  downloadFile(data: any, fileName: string) {
    const blob = new Blob([data], { type: 'application/octet-stream' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    document.body.appendChild(a);
    a.style.display = 'none';
    a.href = url;
    a.download = fileName; // Utilisez le nom de fichier extrait du backend
    a.click();
    window.URL.revokeObjectURL(url);
  }

  getCurrentDate(): string {
    const currentDate = new Date();
    const year = currentDate.getFullYear();
    const month = (currentDate.getMonth() + 1).toString().padStart(2, '0');
    const day = currentDate.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }
  exporterSites(){

      this.ser.exporterSites().subscribe(
        (response: any) => {
          const fileName = this.getFileNameFromResponse(response);
          this.downloadFile(response.body, fileName);
          const snackBarRef: MatSnackBarRef<any> = this.snackBar.open('Les sites sont bien exporter', '', {
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
  search(): void {
    if (this.searchInput.trim() !== '') {
      this.ser.getSitseByKeyword(this.searchInput).subscribe(
        (data: Site[]) => {
          this.sites = data;
          this.noResultsFound = this.sites.length === 0;
        },
        (error) => {
          console.error('Erreur lors de la recherche :', error);
        }
      );
    } else {
      this.getSites();
    }
  }


  UpdateSite() {

      const popup = this.dialog.open(UpdateSiteComponent, {
        width: '50%', height: '599px',
        exitAnimationDuration: '0',
        data: { id: this.SiteDetails.id }

      });
    this.ser._refreshNeeded$.subscribe(()=>{
      this.getSiteById(this.SiteDetails.id);
    })


  }

  deleteSite(id: number) {
    this.dialogService.openConfirmDialof('Voulez vous supprimer ce site?').afterClosed()
      .subscribe(res => {
        if (res) {
          this.ser.deleteSite(id).subscribe(
            () => {
              console.log('Planning deleted successfully');
              this.selectedSiteDetails = false;
              this.referechDate();
              const snackBarRef: MatSnackBarRef<any> = this.snackBar.open('Le site a été supprimé', '', {
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

  getEquipementsOfSite(id: number) {


    this.ser.getEquipementsOfSite(this.SiteDetails.id).subscribe(
      (data) => {
        this.equipements = data;
        this.not_found = false;
        if (this.equipements.length == 0) {
          this.not_found = true;
        }
        console.log(this.equipements);
      },
      (error) => {
        console.log(error());
      }
    )
  }

  getPlanningsOfSite(id: number) {

    this.ser.getPlanningOfSite(id).subscribe((data) => {
        this.plannings = data;
        console.log(this.plannings);
        this.not_foundPl = false;
        if (this.plannings.length == 0) {
          this.not_foundPl = true;
        }
      },
      (error) => {
        console.log(error);
      })
  }

  getInterventionOfSite(id: number) {
    this.ser.getInterventionOfSite(id).subscribe((data) => {
        this.Interventions = data;
        this.not_foundInterv = false;
        if (this.Interventions.length == 0) {
          this.not_foundInterv = true;
        }
        console.log("les ineteventions");
        console.log("interv:" + this.Interventions);
      },
      (error) => {
        console.log(error);
      })
  }

  getSizePlanning() {
    this.ser.getNombreSites().subscribe((data) => {
        this.nbrePlanning = data;

      },
      (error) => {
        console.log(error)
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




  files: any = [];
  file!: any;
  sourceImg !: any;

  getAllFiles(): void {
    this.ser._refreshNeeded$.subscribe(()=>{
      this.getAllFiles();
    })
      const id = this.SiteDetails.id; // Récupérer l'ID du site directement
    this.ser.getAllFiles(id).subscribe((response: any[]) => {
      //console.log(response);
      // Réinitialiser la variable not_foundImg à false
      this.not_foundImg = false;

      this.files = response.map(file => ({
        ...file,
        processedImage: "data:image/jpg;base64," + file.picByte // Utiliser picByte pour créer l'URL base64
      }));
      console.log("files",this.files);
      // Vérifier si la liste des fichiers est vide
      if (this.files.length === 0) {
        // Définir la variable not_foundImg à true si aucune photo n'est trouvée
        this.not_foundImg = true;
      }
    });
  }


  OpenNewSite() {

    var popup = this.dialog.open(NewSiteComponent, {
      width: '50%', height: '599px',
      exitAnimationDuration: '500ms',
      data: {title: 'Site'}
    })
    this.referechDate();

  }

  openPict(imageSrc: string) {
    var popup = this.dialog.open(OpenPicSiteComponent, {

      exitAnimationDuration: '500ms',
      data: {
        imageSrc: imageSrc
      }
    })

  }

  addPict(id:number){
    var popup = this.dialog.open(AddPictoSiteComponent, {
      width: '50%', height: '599px',
      exitAnimationDuration: '500ms',
      data: {id: id }
    })
  }

  deletePic(id:number){
    this.dialogService.openConfirmDialof('Voulez vous supprimer cet photo?').afterClosed()
      .subscribe(res => {
        if (res) {
          this.ser.deletePicOOfSite(id).subscribe(
            () => {
              console.log('photo deleted successfully');
           this.getAllFiles()
              this.referechDate();
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
  NotFoundImmubke:Boolean= false;
  etage!:Etage[]
  getImmublesOfSite(id: number) {
    this.ser.getImmublesOfSite(id).subscribe(
      (data) => {
        this.immubles = data;
        console.log("immubles",this.immubles)
        this.NotFoundImmubke = this.immubles.length === 0;

        if (!this.NotFoundImmubke) {
          this.immubles.forEach(im => im.etages = this.etage);
        }

        console.log(this.etage);
        console.log(this.immubles, id);
      },
      (error) => {
        console.error('Erreur lors de la récupération des immeubles', error);
        this.NotFoundImmubke = true;
      }
    );
  }


  getEtageOfImmuble(id:number){
     this.ser.getEtageOfImmuble(id).subscribe((data)=>{
       this.etage= data
     })
  }
  salle!:Salle[]
  getSalleOfETAGE(id:number){
    this.ser.getSalleOfEtage(id).subscribe((data)=>{
      this.salle= data
    })
  }

  addEquipet(){
      var popup = this.dialog.open(AddEquipemntToSiteComponent, {
      width: '50%', height: '540px',
      exitAnimationDuration: '500ms',
      data: { id: this.SiteDetails.id }
    })
  }




  OpenNewPlanning() {
    var popup = this.dialog.open(AddPlanningtoSiteComponent, {
      width: '50%', height: '550px',
      exitAnimationDuration: '500ms',
      data: { site: this.SiteDetails }
    })

  }


  openSousLieuxDetails(){
    var popup = this.dialog.open(DetailOfSousLiieuxComponent, {
      width: '50%', height: '550px',
      exitAnimationDuration: '500ms',
      data: { site: this.SiteDetails }
    })
  }
}
