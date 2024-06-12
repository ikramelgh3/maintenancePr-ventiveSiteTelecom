import {ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {PlanningServiceService} from "../../../service/planning-service.service";
import {MatSnackBar, MatSnackBarRef} from "@angular/material/snack-bar";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {Router} from "@angular/router";
import {CentreTechnique} from "../models/centreTechnique";
import {MatPaginator, MatPaginatorModule} from '@angular/material/paginator';
import {MatTableDataSource, MatTableModule} from '@angular/material/table';
import {DialogService} from "../service/dialog.service";
import {UpdatePlanningComponent} from "../update-planning/update-planning.component";
import {UpdateCentreTechniqueComponent} from "../parametres/update-centre-technique/update-centre-technique.component";
import {Site} from "../models/Site";
import {Equipement} from "../models/equipement";
import {Checklist} from "../models/checklist";
import * as XLSX from 'xlsx';
import {map} from "rxjs";
import {UpdatePointMesureComponent} from "../parametres/update-point-mesure/update-point-mesure.component";
import {AddChecklistComponent} from "../parametres/add-checklist/add-checklist.component";
import {AddCTComponent} from "../parametres/add-ct/add-ct.component";
import {Dr} from "../models/dr";
@Component({
  selector: 'app-import-data',
  templateUrl: './import-data.component.html',
  styleUrl: './import-data.component.css'
})
export class ImportDataComponent implements  OnInit{
  ctNotFound:boolean =false
  checkliSuce:boolean = false;
  checklistNotFoudn: Boolean =false;
  siteSucc:Boolean = false;
  showSpinner:boolean=false;
  eqSucc:Boolean= false;
  displayImport:Boolean= true
  affichEqui:boolean= false
  showchek:boolean=false
  centreTEchnique!:CentreTechnique []
  afficheTableChecklist:boolean =false
  displayedColumns: string[] = ['id','dr', 'dc', 'name','action'];
  displayedColumns1: string[] = ['id','Groupe', 'Point de mesure', 'Résultat','action'];
  dataSource1 = new MatTableDataSource<any>;
  dataSource = new MatTableDataSource<any>;
  showImprtEquip:boolean= false;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  searchInput: string = '';
  searchInput1: string = '';
  constructor(

    private snackBar: MatSnackBar,private dialogService: DialogService,  private planningService: PlanningServiceService,
    private  router:Router, private dialog: MatDialog, private changeDetectorRef: ChangeDetectorRef
  ) { }

  ngOnInit() {
    this.getAllCentreTechnqiue();
    this.getSite()
    this.getAllEquipemenet()
    this.getChecklists();
    this.getAllDRS();
    this.dataSource.filterPredicate = (data: any, filter: string) => {
      const transformedFilter = filter.trim().toLowerCase();
      const matchId = data.id.toString().toLowerCase().includes(transformedFilter);
      const matchDr = data.dc?.dr?.name.toString().toLowerCase().includes(transformedFilter);
      const matchDc = data.dc?.name.toLowerCase().includes(transformedFilter);
      const matchName = data.name.toLowerCase().includes(transformedFilter);
      return matchId || matchDr || matchDc || matchName;
    }

  }

  getAllSite() {
    this.planningService.getAllSites().subscribe((data) => {
      this.sites = data;
      if (this.sites.length === 0) {
        this.showImprtEquip = true;
      }
      else{
        this.showImprtEquip = false;

      }
    })
  }




  processEquipments(data: any, fileInput: HTMLInputElement, file: File): void {
    const duplicates: Array<{ numeroSerie: string, code: string }> = [];
    const missingSites: Array<{ siteName: string }> = [];
    const missingTypes: Array<{ typeName: string }> = [];
    const importPromises: Array<Promise<void>> = [];

    data.slice(1).forEach((row: any) => {
      const numeroSerie = row[2]?.trim();
      const code = row[0]?.trim();
      const siteName = row[12]?.trim(); // assuming the site name is in the 13th column
      const typeEquipement = row[4]?.trim(); // assuming the type of equipment is in the 6th column

      if (numeroSerie && code) {
        const checkEquipPromise = this.planningService.checkEquipExists(numeroSerie, code).toPromise().then((exists: boolean | undefined) => {
          if (exists) {
            duplicates.push({ numeroSerie, code });
          }
        });
        importPromises.push(checkEquipPromise);
      }
      if (siteName) {
        const checkSitePromise = this.planningService.checkSiteExistsByName(siteName).toPromise().then((exists: boolean | undefined) => {
          if (!exists) {
            missingSites.push({ siteName });
          }
        });
        importPromises.push(checkSitePromise);
      }

    });

    Promise.all(importPromises).then(() => {
      if (duplicates.length > 0) {
        this.showSpinner = false;
        fileInput.value = '';
        this.showSnackBar('Le(s) équipement(s) suivant(s) exist(ent) déjà dans la base de données : ' + JSON.stringify(duplicates) + ', veuillez modifier les données de votre fichier', 'OK');
      } else if (missingSites.length > 0) {
        this.showSpinner = false;
        fileInput.value = '';
        this.showSnackBar('Le(s) site(s) suivant(s) n\'exist(ent) pas dans la base de données : ' + JSON.stringify(missingSites) + ', veuillez modifier les données de votre fichier', 'OK');
      }  else {
        this.planningService.importEquip(file).subscribe(
          response => {
            this.showSpinner = false;
            this.showSnackBar('Les équipements sont bien importés', '');
            this.eqSucc = true;
            fileInput.value = '';
            this.affichEqui=true
          },
          error => {
            this.showSpinner = false;
            fileInput.value = '';
            this.showSnackBar('Erreur lors de l\'importation des données!!!', 'OK');
          }
        );
      }
    }).catch(error => {
      fileInput.value = '';
      console.error('Error during processing equipments: ', error);
      this.showSpinner = false;
      this.showSnackBar('Erreur lors de la vérification des équipements!!!', 'OK');
    });
  }

  uploadFileEqui(fileInput: HTMLInputElement): void {
    const file = fileInput.files ? fileInput.files[0] : null;
    if (file) {
      this.showSpinner = true;
      if (file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet') {
        const reader = new FileReader();
        reader.onload = (e: any) => {
          const data = new Uint8Array(e.target.result);
          const workbook = XLSX.read(data, { type: 'array' });
          const firstSheet = workbook.Sheets[workbook.SheetNames[0]];
          const jsonData = XLSX.utils.sheet_to_json(firstSheet, { header: 1 });

          this.processEquipments(jsonData, fileInput, file);
        };
        reader.readAsArrayBuffer(file);
      } else {
        this.showSpinner = false;
        fileInput.value = '';
        this.showSnackBar('Le fichier sélectionné n\'est pas au format Excel.', 'OK');
      }
    } else {
      this.showSpinner = false;
      this.showSnackBar('Aucun fichier sélectionné.', 'OK');
    }
  }



  showSnackBar(message: string, action: string): void {
    this.showSpinner=false
    const snackBarRef = this.snackBar.open(message, action, {

      duration: 8000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
      panelClass: ['blue-snackbar']
    });
    snackBarRef.onAction().subscribe(() => {
      snackBarRef.dismiss();
    });
  }

  shoWTable:boolean =false
  cliq:boolean =false

  uploadFileSites(fileInput: HTMLInputElement): void {
    const file = fileInput.files ? fileInput.files[0] : null;
    if (file) {
      this.showSpinner = true;
      if (file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet') {
        console.log('Fichier sélectionné :', file);
        const reader = new FileReader();
        reader.onload = (e: any) => {
          const data = new Uint8Array(e.target.result);
          const workbook = XLSX.read(data, { type: 'array' });
          const firstSheetName = workbook.SheetNames[0];
          const worksheet = workbook.Sheets[firstSheetName];
          const sheetData = XLSX.utils.sheet_to_json(worksheet, { header: 1 });

          const headers = sheetData[0] as string[];
          const rows = sheetData.slice(1);

          let duplicates: string[] = [];
          let missingCenters: string[] = [];
          let importPromises: Promise<void>[] = [];

          rows.forEach((row: any) => {
            const siteName = row[headers.indexOf('name')];
            const codeSite = row[headers.indexOf('code')];
            const centerCode = row[headers.indexOf('Centre Technique')]; // Assume 'centerCode' is the header for the center technical code
            if (siteName && codeSite && centerCode) {
              importPromises.push(
                this.planningService.checkSiteNameUnique1(codeSite, siteName).then((exists: boolean | undefined) => {
                  if (exists !== undefined && exists === true) {
                    duplicates.push(siteName);
                  }
                })
              );

              importPromises.push(
                this.planningService.checkCTExist1(centerCode).then((exists: boolean | undefined) => {
                  if (exists !== undefined && exists === false) {
                    missingCenters.push(centerCode);
                  }
                })
              );
            }
          });

          Promise.all(importPromises).then(() => {
            if (duplicates.length > 0) {
              this.showSpinner = false;
              fileInput.value = '';
              console.log('Les sites suivants existent déjà :', duplicates);
              this.snackBar.open(`Importation échouée, Le(s) site(s) suivant(s) exist(ent) déjà : ${duplicates.join(', ')}`, 'OK', {
                duration: 8000,
                horizontalPosition: 'right',
                verticalPosition: 'top',
                panelClass: ['blue-snackbar']
              });
            } else if (missingCenters.length > 0) {
              this.showSpinner = false;
              fileInput.value = '';
              console.log('Les centres techniques suivants sont manquants :', missingCenters);
              this.snackBar.open(`Importation échouée, le(s) centre(s) technique(s) suivant(s) sont manquant(s) : ${missingCenters.join(', ')}`, 'OK', {
                duration: 8000,
                horizontalPosition: 'right',
                verticalPosition: 'top',
                panelClass: ['blue-snackbar']
              });
            } else {
              // Aucun doublon détecté et tous les centres techniques existent, importer les sites
              this.planningService.importSite(file).subscribe(
                response => {
                  this.showSpinner = false;
                  console.log('Import successful: ', response);
                  fileInput.value = '';
                  this.cliq = true;
                  this.siteSucc = true;
                  this.showImprtEquip = false;
                  this.affiche = true;
                  this.snackBar.open('Les sites sont bien importés', '', {
                    duration: 6000,
                    horizontalPosition: 'right',
                    verticalPosition: 'top',
                    panelClass: ['blue-snackbar']
                  });
                },
                error => {
                  console.log('Import failed: ', error);
                  this.showSpinner = false;
                  fileInput.value = '';
                  this.snackBar.open('Erreur lors de l\'importation des données!!!', 'OK', {
                    duration: 8000,
                    horizontalPosition: 'right',
                    verticalPosition: 'top',
                    panelClass: ['blue-snackbar']
                  });
                }
              );
            }
          });
        };
        reader.readAsArrayBuffer(file);
      } else {
        this.showSpinner = false;
        fileInput.value = '';
        console.log('Le fichier sélectionné n\'est pas au format Excel.');
        this.snackBar.open('Le fichier sélectionné n\'est pas au format Excel.', 'OK', {
          duration: 8000,
          horizontalPosition: 'right',
          verticalPosition: 'top',
          panelClass: ['blue-snackbar']
        });
      }
    } else {
      this.showSpinner = false;
      fileInput.value = '';
      console.log('Aucun fichier sélectionné.');
      this.snackBar.open('Aucun fichier sélectionné.', 'OK', {
        duration: 8000,
        horizontalPosition: 'right',
        verticalPosition: 'top',
        panelClass: ['blue-snackbar']
      });
    }
  }





  uploadFileCT(fileInput: HTMLInputElement): void {
    const file = fileInput.files ? fileInput.files[0] : null;
    if (file) {
      this.showSpinner = true
      if (file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet') {
        console.log('Fichier sélectionné :', file);
        const reader = new FileReader();
        reader.onload = (e: any) => {
          const data = new Uint8Array(e.target.result);
          const workbook = XLSX.read(data, { type: 'array' });
          const firstSheetName = workbook.SheetNames[0];
          const worksheet = workbook.Sheets[firstSheetName];
          const sheetData = XLSX.utils.sheet_to_json(worksheet, { header: 1 });

          const headers = sheetData[0] as string[];
          const rows = sheetData.slice(1);

          let duplicates: string[] = [];
          let importPromises: Promise<void>[] = [];

          rows.forEach((row: any) => {
            const siteName = row[headers.indexOf('Nom du groupe de support L1')];
            console.log('Checking for duplicate:', siteName);
            if (siteName) {
              importPromises.push(
                this.planningService.checkCTExsit(siteName).then((exists: boolean | undefined) => {
                  if (exists !== undefined && exists === true) {
                    duplicates.push(siteName);
                    console.log('Duplicate check result:', exists);
                  }
                }).catch(error => {
                  console.error('Error while checking CT exist:', error);
                })
              );
            }
          });console.log('Duplicates:', duplicates);

          Promise.all(importPromises).then(() => {
            if (duplicates.length > 0) {
              this.showSpinner=false
              fileInput.value = '';
              console.log('Duplicates:', duplicates);
              console.log('Importation échouée, les sites suivants existent déjà :', duplicates);
              this.snackBar.open(`Importation échouée, le(s) centres(s) technique(s) suivant(s) exist(ent) déjà : ${duplicates.join(', ')}`, 'OK', {
                duration: 8000,
                horizontalPosition: 'right',
                verticalPosition: 'top',
                panelClass: ['blue-snackbar']
              });
            } else {
              // Aucun doublon détecté, importer les sites
              this.planningService.importCentreTechnique(file).subscribe(
                response => {
                  this.showSpinner=false
                  console.log('Import successful: ', response);
                  fileInput.value = '';
                  this.cliq = true;
                  this.snackBar.open('Les centres techniques sont bien importés', '', {
                    duration: 6000,
                    horizontalPosition: 'right',
                    verticalPosition: 'top',
                    panelClass: ['blue-snackbar']
                  });
                },
                error => {
                  console.log('Import failed: ', error);
                  this.showSpinner=false
                  fileInput.value = '';
                  this.snackBar.open('Erreur lors de l\'importation des données!!!', 'OK', {
                    duration: 8000,
                    horizontalPosition: 'right',
                    verticalPosition: 'top',
                    panelClass: ['blue-snackbar']
                  });
                }
              );
            }
          });
        };
        reader.readAsArrayBuffer(file);
      } else {
        console.log('Le fichier sélectionné n\'est pas au format Excel.');
        this.showSpinner=false
        fileInput.value = '';
        this.snackBar.open('Le fichier sélectionné n\'est pas au format Excel.', 'OK', {
          duration: 8000,
          horizontalPosition: 'right',
          verticalPosition: 'top',
          panelClass: ['blue-snackbar']
        });
      }
    } else {
      console.log('Aucun fichier sélectionné.');
      this.showSpinner=false
      fileInput.value = '';
      this.snackBar.open('Aucun fichier sélectionné.', 'OK', {
        duration: 8000,
        horizontalPosition: 'right',
        verticalPosition: 'top',
        panelClass: ['blue-snackbar']
      });
    }
  }


  goto(){
    this.router.navigateByUrl('/Equipements');
  }
  ct: CentreTechnique[] = [];

  getAllCentreTechnqiue() {
    this.planningService.getAllCT().subscribe(data => {
      this.ct = data;
      this.totalItems= this.ct.length;
      if (this.ct.length !== 0) {
        this.cliq = true;
        this.ctNotFound = false
      }
      else {
        this.cliq = false
        this.ctNotFound = true
      }
      this.dataSource.data = data;
      console.log(this.ct);
      console.log(this.cliq);
      // Définir la pagination ici
      this.dataSource.paginator = this.paginator;
      // Forcer la mise à jour de la vue
      this.changeDetectorRef.detectChanges();
    });

    this.planningService._refreshNeeded$.subscribe(() => {
      this.getAllCentreTechnqiue();
    });
  }

  showTable() {
    this.getAllCentreTechnqiue();
    this.shoWTable = true;
  }

  deleteCT(cet:CentreTechnique) {
    this.dialogService.openConfirmDialof('Voulez vous supprimer ce centre ?').afterClosed()
      .subscribe(res => {
        if (res) {
          this.planningService.deleteCentreTechnique(cet).subscribe(
            () => {
              console.log('ct deleted successfully');
              this.getAllCentreTechnqiue()
              // Rediriger vers la page des plannings après la suppression
              const snackBarRef: MatSnackBarRef<any> = this.snackBar.open('Le centre technique a été supprimé', '', {
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


  deleteChecklist(cet:Checklist) {
    this.dialogService.openConfirmDialof('Voulez vous supprimer ce point de mesure ?').afterClosed()
      .subscribe(res => {
        if (res) {
          this.planningService.deletePointMeusre(cet).subscribe(
            () => {
              console.log('ct deleted successfully');
              this.getChecklists()
              // Rediriger vers la page des plannings après la suppression
              const snackBarRef: MatSnackBarRef<any> = this.snackBar.open('Le point de mesure a été supprimé', '', {
                duration: 8000,
                horizontalPosition: 'right',
                verticalPosition: 'top',
              });

              snackBarRef.onAction().subscribe(() => {
                snackBarRef.dismiss();
              });
            },
            error => {
              console.error('Error deleting point de mesure:', error);
            }
          );
        }
      });
  }

  edit(row:any){
    const popup = this.dialog.open(UpdateCentreTechniqueComponent, {
      width: '30%', height: '378px',
      exitAnimationDuration: '0',
      data: {  centre: row }
    });

  }

  editPointMesure(row:any){
    const popup = this.dialog.open(UpdatePointMesureComponent, {
      width: '30%', height: '378px',
      exitAnimationDuration: '0',
      data: {  pointMesure: row }
    });

  }


  sites!:Site[]
  getSite(){
    this.planningService.getAllSites().subscribe((data)=>{this.sites=data;
      if(this.sites.length!==0){
        this.siteSucc=true
        this.showImprtEquip=false
      }
      else {
        this.showImprtEquip=true
      }
    console.log(this.sites.length)})
  }
  equi!:Equipement[]
  affiche:boolean=false
  getAllEquipemenet(){
     this.planningService.getEquipements().subscribe((data)=>{
        this.equi = data;
        if(this.equi.length!==0){
          this.affichEqui=true
          this.affiche=true
          this.showImprtEquip = false
        }
         console.log("length equi",this.equi.length);
     })
  }

  afficheChecklist:Boolean = false
  checklist!:Checklist[]


  getChecklists() {


    this.planningService._refreshNeeded$.subscribe(()=>{
      this.getChecklists();
    })
    this.planningService.getChecklist().subscribe(data => {
      this.checklist = data;
      console.log("dd",this.checklist)
      if(this.checklist.length!==0){
        this.afficheTableChecklist=true;
        this.affiche=true
        this.checklistNotFoudn=false;
        this.checkliSuce = true;
      }
      if(this.checklist.length===0){
        this.checklistNotFoudn=true
        this.affiche=false
        this.siteSucc = false
        this.checkliSuce = false;
      }

      this.dataSource1.data = data;
      this.dataSource1.paginator = this.paginator;
      console.log(this.ct)
      console.log(this.cliq)
    });
  }
  showCheck:Boolean=false

  showTableChecklist(){
this.getChecklists();
    this.showchek=true
  }


  uploadChecklist(fileInput: HTMLInputElement): void {
    const file = fileInput.files ? fileInput.files[0] : null;
    if (file) {
      this.showSpinner=true
      if (file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet') {
        console.log('Fichier sélectionné :', file);
        const reader = new FileReader();
        reader.onload = (e: any) => {
          const data = new Uint8Array(e.target.result);
          const workbook = XLSX.read(data, { type: 'array' });
          const firstSheetName = workbook.SheetNames[0];
          const worksheet = workbook.Sheets[firstSheetName];
          const sheetData = XLSX.utils.sheet_to_json(worksheet, { header: 1 });

          const headers = sheetData[0] as string[];
          const rows = sheetData.slice(1);

          let duplicates: string[] = [];
          let importPromises: Promise<void>[] = [];

          rows.forEach((row: any) => {
            const siteName = row[headers.indexOf('Point de mesure')];
            console.log('Checking for duplicate:', siteName);
            if (siteName) {
              importPromises.push(
                this.planningService.checkPointExist(siteName).then((exists: boolean | undefined) => {
                  if (exists !== undefined && exists === true) {
                    duplicates.push(siteName);
                    console.log('Duplicate check result:', exists);
                  }
                }).catch(error => {
                  console.log('Error while checking CT exist:', error);
                })
              );
            }
          });console.log('Duplicates:', duplicates);

          Promise.all(importPromises).then(() => {
            if (duplicates.length > 0) {
              console.log('Duplicates:', duplicates);
              this.showSpinner=false
              fileInput.value = '';
              console.log('Importation échouée, les sites suivants existent déjà :', duplicates);
              this.snackBar.open(`Importation échouée, le(s) checklist(s) suivant(s) exist(ent) déjà : ${duplicates.join(', ')}`, 'OK', {
                duration: 8000,
                horizontalPosition: 'right',
                verticalPosition: 'top',
                panelClass: ['blue-snackbar']
              });
            } else {
              // Aucun doublon détecté, importer les sites
              this.planningService.importChecklist(file).subscribe(
                response => {
                  console.log('Import successful: ', response);
                  this.afficheTableChecklist=true;
                  fileInput.value = '';
                  this.showSpinner=false
                  this.cliq = true;
                  this.checkliSuce = true;
                  this.snackBar.open('Les checklists sont bien importés', '', {
                    duration: 6000,
                    horizontalPosition: 'right',
                    verticalPosition: 'top',
                    panelClass: ['blue-snackbar']
                  });
                },
                error => {
                  console.log('Import failed: ', error);
                  this.showSpinner=false
                  fileInput.value = '';
                  this.snackBar.open('Erreur lors de l\'importation des données!!!', 'OK', {
                    duration: 8000,
                    horizontalPosition: 'right',
                    verticalPosition: 'top',
                    panelClass: ['blue-snackbar']
                  });
                }
              );
            }
          });
        };
        reader.readAsArrayBuffer(file);
      } else {
        this.showSpinner=false
        fileInput.value = '';
        console.log('Le fichier sélectionné n\'est pas au format Excel.');
        this.snackBar.open('Le fichier sélectionné n\'est pas au format Excel.', 'OK', {
          duration: 8000,
          horizontalPosition: 'right',
          verticalPosition: 'top',
          panelClass: ['blue-snackbar']
        });
      }
    } else {
      this.showSpinner=false
      fileInput.value = '';
      console.log('Aucun fichier sélectionné.');
      this.snackBar.open('Aucun fichier sélectionné.', 'OK', {
        duration: 8000,
        horizontalPosition: 'right',
        verticalPosition: 'top',
        panelClass: ['blue-snackbar']
      });
    }
  }

  addChecklist(){
    const popup = this.dialog.open(AddChecklistComponent, {
      width: '35%', height: '426px',
      exitAnimationDuration: '0'
    });

  }


    addCT(){
      const popup = this.dialog.open(AddCTComponent, {
        width: '35%', height: '380px',
        exitAnimationDuration: '0'
      });
    }
  totalItems:number=0

  DRS!: Dr[]
  getAllDRS(){
    this.planningService.getDR().subscribe((data)=>{
      this.DRS= data;
    })
  }
  search(): void {
    if (this.searchInput.trim() !== '') {
      this.planningService.getCTByKeyword(this.searchInput).subscribe(
        (data: CentreTechnique[]) => {
          this.ct = data;
        //  this.noResultsFound = this.sites.length === 0;
        },
        (error) => {
          console.error('Erreur lors de la recherche :', error);
        }
      );
    } else {
      this.getAllCentreTechnqiue();
    }
  }
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
  applyFilter1(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value.toLowerCase();
    this.dataSource1.filter = filterValue.trim().toLowerCase();

    // Recherche sur toutes les propriétés des données
    this.dataSource1.filterPredicate = (data: any, filter: string) => {
      const transformedFilter = filter.trim().toLowerCase();
      return Object.keys(data).some(key => {
        const value = data[key];
        if (value !== null && value !== undefined && key !== 'action') {
          if (key === 'typeEquipent' && value && value.name) {
            // Recherche spécifique pour la colonne "Groupe"
            return value.name.toLowerCase().includes(transformedFilter);
          }
          return value.toString().toLowerCase().includes(transformedFilter);
        }
        return false;
      });
    };

    if (this.dataSource1.paginator) {
      this.dataSource1.paginator.firstPage(); // Revenir à la première page après le filtrage
    }
  }



}
