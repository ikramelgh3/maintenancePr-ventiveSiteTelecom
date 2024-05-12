import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";
import {PlanningServiceService} from "../../../service/planning-service.service";
import {MatSnackBar, MatSnackBarRef} from "@angular/material/snack-bar";

@Component({
  selector: 'app-import-dialog',
  templateUrl: './import-dialog.component.html',
  styleUrl: './import-dialog.component.css'
})
export class ImportDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<ImportDialogComponent>,
    private planningService: PlanningServiceService,private snackBar: MatSnackBar // Injectez le service PlanningService
  ) { }

  uploadFile(fileInput: HTMLInputElement): void {
    const file = fileInput.files ? fileInput.files[0] : null;
    if (file) {
      console.log('Fichier sélectionné :', file);
      // Utilisez le service pour télécharger le fichier
      this.planningService.uploadFile(file).subscribe(
        response => {
          // Gérer la réponse réussie ici
          console.log('Import successful: ', response);

          this.closePopup();
          const snackBarRef3: MatSnackBarRef<any> = this.snackBar.open('Les plannings sont bien importés', '', {
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

  closePopup(){

    this.dialogRef.close();
  }
  cancel(): void {
    this.dialogRef.close();
  }
}
