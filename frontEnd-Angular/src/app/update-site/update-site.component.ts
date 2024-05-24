import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {CentreTechnique} from "../models/centreTechnique";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {MatSnackBar} from "@angular/material/snack-bar";
import {PlanningServiceService} from "../../../service/planning-service.service";
import {PlanningdataserviceService} from "../planningdataservice.service";
import {Router} from "@angular/router";
import {NewSiteComponent} from "../new/new-site/new-site.component";
import {Site} from "../models/Site";

@Component({
  selector: 'app-update-site',
  templateUrl: './update-site.component.html',
  styleUrl: './update-site.component.css'
})
export class UpdateSiteComponent implements OnInit{


  inputdata: any;

  updateForm!: FormGroup;
  type: string[] = ['Fixe', 'Mobile'];
  centreTechnique: CentreTechnique[] = [];
  inst: string[] = ['Indoor', 'Outdoor'];
  supportAntenne: string[] = ['Potelet', 'Pylône', 'Haubané', 'Pylône esthétique', 'Monopode', 'Poteau métallique', 'Poteau béton', 'Poteau bois', 'Monotube', 'Façade', 'Arbre mort', 'Tour/Château', 'Poteau métallique 2 op', 'Poteau métallique 3 op'];
  lieuInsatallationBTS: string[] = ['Terrasse', 'Trottoir', 'Sol', 'Local', 'Façade'];
  typeAlimentation: string[] = ['électrifié MT', 'électrifié BT', 'solaire', 'GE', 'hybride (solaire+ GE)'];
  typeTransmission: string[] = ['FO', 'FH'];
  presenceGESecours: boolean[] = [true, false];
typ!: String;
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
  ) {  dialogRef.disableClose = true;}
  ct!:CentreTechnique;
id!:number
  site!:Site
  st!:Site
  ngOnInit() {

    this.initForm();
    this.id = this.data.id;
    this.planningService.getSiteById(this.id).subscribe(data => {
      this.site = data;
      this.typ= this.site.typeSite
      this.st= data
      console.log(this.site);
      this.getCT(this.site.centreTechnique.id);

      this.updateForm.patchValue({
        code: this.site.code,
        name: this.site.name,
        typeSite: this.site.typeSite,
        // Sélectionnez le centre technique associé dans le formulaire
        centreTechnique: this.site.centreTechnique ? this.getCentreTechniqueById(this.site.centreTechnique.id) : null,
        addresse: this.site.addresse,
        typeInstallation: this.site.typeInstallation,
        supportAntennes: this.site.supportAntennes,
        hauteurSupportAntenne: this.site.hauteurSupportAntenne,
        lieuInsatallationBTS: this.site.lieuInsatallationBTS,
        typeAlimentation: this.site.typeAlimentation,
        typeTransmission: this.site.typeTransmission,
        presenceGESecours: this.site.presenceGESecours // Adapter selon le type de données
      });

    }, error => console.log(error));
  }

// Méthode pour récupérer le centre technique par ID
  getCentreTechniqueById(id: number): CentreTechnique | null {
    return this.centreTechnique.find(ct => ct.id === id) || null;
  }


  initForm(){
    this.getAllCt();
    this.updateForm = this.fb.group({
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
  }


  getAllCt() {
    this.planningService.getAllCT().subscribe(data => {
      this.centreTechnique = data;
    });
  }
ctname!:String
  getCT(id:number){
     this.planningService.getCentreTehniwueById(id).subscribe((data)=>{
     this.ct = data;
     this.ct.name= data.name;
     this.ct.id= data.id
       this.ctname = this.ct.name
       console.log("ctnmae:",this.ct.name)
     })

  }
idSite!:number
  updateSite(): void {
    console.log(this.typ)
    console.log(this.st)
    this.idSite = this.st.id
    console.log(this.idSite)
    if (this.typ === 'Fixe') {
      this.updateSiteFixe();
    } else if (this.typ === 'Mobile') {
      this.updateSiteMobile();
    } else {
      // Afficher un message d'erreur
      console.error('Type de site non valide');
    }
  }

  updateSiteFixe(): void {
    console.log(this.idSite);
    console.log("update site fixe");
    const updatedSiteFixeData = this.updateForm.value;
    console.log("nouveau donnee", updatedSiteFixeData);

    this.planningService.checkifSiteExisteByCode(updatedSiteFixeData.code, this.idSite).subscribe((data) => {
      if (!data) {
        console.log("bien code");

        this.planningService.checkifSiteExisteByName(updatedSiteFixeData.name, this.idSite).subscribe((data) => {
          if (!data) {
            console.log("name bien");

            this.planningService.updateSiteFixe(this.idSite, updatedSiteFixeData ,updatedSiteFixeData.centreTechnique.id).subscribe(
              (site) => {
                this.close();
                // Afficher une notification de succès
                this.snackBar.open('Le site est mis à jour avec succès', 'Fermer', {
                  duration: 8000,
                  horizontalPosition: 'end',
                  verticalPosition: 'top',
                  panelClass: ['error-snackbar']
                });
              },
              (error) => {
                this.snackBar.open('Erreur lors de la mise à jour du site fixe', 'Fermer', {
                  duration: 8000,
                  horizontalPosition: 'end',
                  verticalPosition: 'top',
                  panelClass: ['error-snackbar']
                });
                console.error(error);
              }
            );
          } else {
            this.snackBar.open('Un site existe deja avec ce nom', 'Fermer', {
              duration: 6000,
              horizontalPosition: 'end',
              verticalPosition: 'top',
              panelClass: ['error-snackbar']
            });
          }
        });
      } else {
        this.snackBar.open('Un site existe deja avec ce code', 'Fermer', {
          duration: 6000,
          horizontalPosition: 'end',
          verticalPosition: 'top',
          panelClass: ['error-snackbar']
        });
      }
    });
  }

  updateSiteMobile(): void {
    console.log(this.idSite)
    // Récupérer les données du formulaire pour le site mobile
    const updatedSiteMobileData = this.updateForm.value; // Modifier en fonction de vos champs

    this.planningService.updateSiteMobile(this.idSite, updatedSiteMobileData , updatedSiteMobileData.centreTechnique.id).subscribe(
      (site) => {
        this.close();

        // Afficher une notification de succès
        this.snackBar.open('Le site est mis à jour avec succès', 'Fermer', {
          duration: 8000,
          horizontalPosition: 'end',
          verticalPosition: 'top',
          panelClass: ['error-snackbar']
        });
      },
      (error) => {
        this.snackBar.open('Erreur lors de la mise à jour du site fixe', 'Fermer', {
          duration: 8000,
          horizontalPosition: 'end',
          verticalPosition: 'top',
          panelClass: ['error-snackbar']
        });
        console.error(error);
      }
    );
  }

close(){
   this.dialogRef.close();
}


}
