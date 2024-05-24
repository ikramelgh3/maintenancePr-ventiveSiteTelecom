import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { PlanningServiceService } from "../../../../service/planning-service.service";
import { CentreTechnique } from "../../models/centreTechnique";
import { MatSnackBar } from '@angular/material/snack-bar';
import { Site } from "../../models/Site";
import {PlanningdataserviceService} from "../../planningdataservice.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-new-site',
  templateUrl: './new-site.component.html',
  styleUrls: ['./new-site.component.css']
})
export class NewSiteComponent implements OnInit {
  inputdata: any;
  public addSite!: FormGroup;
  type: string[] = ['Fixe', 'Mobile'];
  ct: CentreTechnique[] = [];
  inst: string[] = ['Indoor', 'Outdoor'];
  supportAntenne: string[] = ['Potelet', 'Pylône', 'Haubané', 'Pylône esthétique', 'Monopode', 'Poteau métallique', 'Poteau béton', 'Poteau bois', 'Monotube', 'Façade', 'Arbre mort', 'Tour/Château', 'Poteau métallique 2 op', 'Poteau métallique 3 op'];
  lieuInsatallationBTS: string[] = ['Terrasse', 'Trottoir', 'Sol', 'Local', 'Façade'];
  typeAlimentation: string[] = ['électrifié MT', 'électrifié BT', 'solaire', 'GE', 'hybride (solaire+ GE)'];
  typeTransmission: string[] = ['FO', 'FH'];
  presenceGESecours: boolean[] = [true, false];

  presenceGESecoursTranslations: { [key: string]: boolean } = {
    'Oui': true,
    'Non': false
  };

  presenceGESecoursOptions: string[] = Object.keys(this.presenceGESecoursTranslations);

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
   private dialogRef: MatDialogRef<NewSiteComponent>,
    private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private planningService: PlanningServiceService,
    private dataser:PlanningdataserviceService,
    private route:Router
  ) {

    dialogRef.disableClose = true;
  }

  ngOnInit() {
    this.inputdata = this.data;
    this.addSite = this.fb.group({
      name: ['', Validators.required],
      code: ['', Validators.required],
      typeSite: ['', Validators.required],
      addresse: ['', Validators.required],
      centreTechnique: ['', Validators.required],
      typeInstallation: ['', Validators.required],
      typeAlimentation: ['', Validators.required],
      typeTransmission: ['', Validators.required],
      presenceGESecours: [null, Validators.required], // Définition de la propriété disabled à true par défaut
      lieuInsatallationBTS: ['', Validators.required],
      hauteurSupportAntenne: ['', Validators.required],// Définition de la propriété disabled à true par défaut
      supportAntennes: [''],
    });
    this.getAllCt();
  }
  isMobileSite(): boolean {
    const typeSiteControl = this.addSite.get('typeSite');
    return typeSiteControl?.value === 'Mobile';
  }
  getAllCt() {
    this.planningService.getAllCT().subscribe(data => {
      this.ct = data;
    });
  }

/*  closePopup() {
    this.dialogRef.close();
  }
*/
  isTypeFixed(): boolean {
    return this.addSite.get('typeSite')?.value === 'fixe';
  }
  convertirOuiNonEnBoolean(valeur: string): boolean {
    return valeur.toLowerCase() === 'oui';
  }


  id!:number;
  add() {
    const site: Site = this.addSite.value;
    const idCt = site.centreTechnique.id;
    this.id = site.id;
    this.planningService.checkSiteNameUnique(site.code,site.name).subscribe(existsName => {
      if (existsName) {
        // Gérer le cas où un site avec le même nom existe déjà
        this.showSnackBar('Ce site existe déjà. Veuillez choisir un autre nom.');
      } else {
        // Vérifier si un site avec le même code existe déjà
        this.planningService.checkSiteCodeUnique(site.code).subscribe(existsCode => {
          if (existsCode) {
            // Gérer le cas où un site avec le même code existe déjà
            this.showSnackBar('Ce code de site existe déjà. Veuillez choisir un autre code.');
          } else {
            // Ajouter le site
            if (this.isMobileSite()) {
              // Ajouter le site mobile
              this.addMobileSite(site, idCt);
              this.dataser.refreshSite();
              this.close();
            } else {
              // Ajouter le site fixe
              this.addFixeSite(site, idCt);
              this.dataser.refreshSite();
              this.close();
            }
          }
        });
      }
    });
  }


// Méthode pour afficher une snackbar
  showSnackBar(message: string) {
    this.snackBar.open(message, 'Ok', {
      duration: 0,
      horizontalPosition: 'right',
      verticalPosition: 'top',
      panelClass: ['blue-snackbar']
    });
  }

// Méthode pour ajouter un site mobile
  addMobileSite(site: Site, idCt: number) {
    this.planningService.addSiteMobile(site, idCt).subscribe(
      data => {
        // Traitement après l'ajout réussi
        this.showSnackBar('Le site a été ajouté avec succès');

      },

      error => {
        console.error(error);
        this.showSnackBar('Une erreur s\'est produite lors de l\'ajout du site.');
      }
    );
  }

// Méthode pour ajouter un site fixe
  addFixeSite(site: Site, idCt: number) {
    this.planningService.addSiteFixe(site, idCt).subscribe(
      data => {
        // Traitement après l'ajout réussi
        this.showSnackBar('Le site a été ajouté avec succès');
      },
      error => {
        console.error(error);
        this.showSnackBar('Une erreur s\'est produite lors de l\'ajout du site.');
      }
    );
  }

// Méthode pour vérifier si tous les champs sont remplis pour le type de site mobile
  isMobileSiteIncomplete(site: Site): boolean {
    return !site.name || !site.code || !site.addresse || !site.centreTechnique || !site.typeInstallation || !site.typeAlimentation || !site.typeTransmission || !site.presenceGESecours || !site.supportAntennes || !site.hauteurSupportAntenne || !site.lieuInsatallationBTS;
  }

  isTypeNotFound(site:Site):Boolean{
    return !site.typeSite;
  }

  isFixeSiteIncomplete(site: Site): boolean {
    return !site.name || !site.code || !site.addresse || !site.centreTechnique || !site.typeInstallation || !site.typeAlimentation || !site.typeTransmission || !site.presenceGESecours;
  }


  selectedFile!: File;
  uploadProgress!: number;

  onFileSelected(event: any) {

    const fileList: FileList = event.target.files;
    if (fileList && fileList.length > 0) {
      this.selectedFile = fileList[0];
    }

  }



  close(){
    this.dialogRef.close();
  }
}

