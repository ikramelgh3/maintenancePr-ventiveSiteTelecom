import { Component } from '@angular/core';
import {PlanningServiceService} from "../../../service/planning-service.service";
import {MatSnackBar, MatSnackBarRef} from "@angular/material/snack-bar";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-import-data',
  templateUrl: './import-data.component.html',
  styleUrl: './import-data.component.css'
})
export class ImportDataComponent {

  siteSucc:Boolean = false;
  eqSucc:Boolean= false;
  constructor(

    private snackBar: MatSnackBar, private planningService: PlanningServiceService
  ) { }


  uploadFileSites(fileInput: HTMLInputElement): void {
    const file = fileInput.files ? fileInput.files[0] : null;
    if (file) {
      if (file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet') {
        console.log('Fichier sélectionné :', file);
        // Utilisez le service pour télécharger le fichier
        this.planningService.importSite(file).subscribe(
          response => {
            // Gérer la réponse réussie ici
            console.log('Import successful: ', response);
            this.siteSucc= true;
            fileInput.value = '';

            const snackBarRef3: MatSnackBarRef<any> = this.snackBar.open('Les sites sont bien importés', '', {

              duration: 8000,
              horizontalPosition: 'right',
              verticalPosition: 'top',
              panelClass: ['blue-snackbar']
            });
            snackBarRef3.onAction().subscribe(() => {
              snackBarRef3.dismiss();
            });
          },
          error => {
            // Gérer l'erreur ici
            console.error('Import failed: ', error);
            const snackBarRef4: MatSnackBarRef<any> = this.snackBar.open('Erreur lors de l\'importation des données!!!', 'OK', {
              duration: 8000,
              horizontalPosition: 'right',
              verticalPosition: 'top',
              panelClass: ['blue-snackbar']
            });
            snackBarRef4.onAction().subscribe(() => {
              snackBarRef4.dismiss();
            });
          }
        );
      } else {
        console.error('Le fichier sélectionné n\'est pas au format Excel.');
        const snackBarRef2: MatSnackBarRef<any> = this.snackBar.open('Le fichier sélectionné n\'est pas au format Excel.', 'OK', {
          duration: 8000,
          horizontalPosition: 'right',
          verticalPosition: 'top',
          panelClass: ['blue-snackbar']
        });
        snackBarRef2.onAction().subscribe(() => {
          snackBarRef2.dismiss();
        });
      }
    } else {
      console.error('Aucun fichier sélectionné.');
      const snackBarRef1: MatSnackBarRef<any> = this.snackBar.open('Aucun fichier sélectionné.', 'OK', {
        duration: 8000,
        horizontalPosition: 'right',
        verticalPosition: 'top',
        panelClass: ['blue-snackbar']
      });
      snackBarRef1.onAction().subscribe(() => {
        snackBarRef1.dismiss();
      });
    }
  }





  uploadFileEqui(fileInput: HTMLInputElement): void {
    const file = fileInput.files ? fileInput.files[0] : null;
    if (file) {
      if (file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet') {
        console.log('Fichier sélectionné :', file);
        // Utilisez le service pour télécharger le fichier
        this.planningService.importEquip(file).subscribe(
          response => {
            // Gérer la réponse réussie ici
            console.log('Import successful: ', response);
            this.eqSucc= true;
            fileInput.value = '';

            const snackBarRef3: MatSnackBarRef<any> = this.snackBar.open('Les équipements sont bien importés', '', {

              duration: 8000,
              horizontalPosition: 'right',
              verticalPosition: 'top',
              panelClass: ['blue-snackbar']
            });
            snackBarRef3.onAction().subscribe(() => {
              snackBarRef3.dismiss();
            });
          },
          error => {
            // Gérer l'erreur ici
            console.error('Import failed: ', error);
            const snackBarRef4: MatSnackBarRef<any> = this.snackBar.open('Erreur lors de l\'importation des données!!!', 'OK', {
              duration: 8000,
              horizontalPosition: 'right',
              verticalPosition: 'top',
              panelClass: ['blue-snackbar']
            });
            snackBarRef4.onAction().subscribe(() => {
              snackBarRef4.dismiss();
            });
          }
        );
      } else {
        console.error('Le fichier sélectionné n\'est pas au format Excel.');
        const snackBarRef2: MatSnackBarRef<any> = this.snackBar.open('Le fichier sélectionné n\'est pas au format Excel.', 'OK', {
          duration: 8000,
          horizontalPosition: 'right',
          verticalPosition: 'top',
          panelClass: ['blue-snackbar']
        });
        snackBarRef2.onAction().subscribe(() => {
          snackBarRef2.dismiss();
        });
      }
    } else {
      console.error('Aucun fichier sélectionné.');
      const snackBarRef1: MatSnackBarRef<any> = this.snackBar.open('Aucun fichier sélectionné.', 'OK', {
        duration: 8000,
        horizontalPosition: 'right',
        verticalPosition: 'top',
        panelClass: ['blue-snackbar']
      });
      snackBarRef1.onAction().subscribe(() => {
        snackBarRef1.dismiss();
      });
    }
  }




}
