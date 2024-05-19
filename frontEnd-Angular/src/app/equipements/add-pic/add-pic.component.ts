import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {MatSnackBar} from "@angular/material/snack-bar";
import {PlanningServiceService} from "../../../../service/planning-service.service";

@Component({
  selector: 'app-add-pic',
  templateUrl: './add-pic.component.html',
  styleUrl: './add-pic.component.css'
})
export class AddPicComponent implements OnInit {

  id!:number
  previewImageUrl1!: string;

  selectedFiles: (File | null)[] = [null];


  defaultImageUrl: string = "assets/img/img1.png";
  additionalPhotos: { imageUrl: string }[] = [];
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialogRef: MatDialogRef<AddPicComponent>, private snackBar: MatSnackBar,private ser:PlanningServiceService){}

  ngOnInit() {
    this.previewImageUrl1 = this.defaultImageUrl;
  }

  onFileSelected(event: any, index: number) {
    const files: FileList = event.target.files;
    if( files.length==0){
      const files: FileList = event.target.files;
      if (files.length === 0) {
        this.selectedFiles[0] =null
        this.selectedFiles[index - 1] = null;
        return;
      }

    }
    if (files.length > 0) {
      const file = files[0];
      this.selectedFiles[index - 1] = file;
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        if (index === 1) {
          this.previewImageUrl1 = reader.result as string;
        } else {
          this.additionalPhotos[index - 2].imageUrl = reader.result as string;
        }
      };
    } else {
      if (index === 1) {
        this.previewImageUrl1 = this.defaultImageUrl;
      } else {
        this.additionalPhotos[index - 2].imageUrl = this.defaultImageUrl;
      }
    }
  }

  addAnotherPhoto() {
    this.selectedFiles.push(null);
    // Ajouter un nouvel objet dans le tableau additionalPhotos avec l'URL de l'image par défaut
    this.additionalPhotos.push({ imageUrl: this.defaultImageUrl });
  }


  uploadFiles(siteId: number) {
    console.log(this.data.id);
    console.log("file:", this.selectedFiles);

    // Vérifiez si aucun fichier n'a été sélectionné
    if (!this.selectedFiles.some(file => file !== null)) {
      console.log('Aucun fichier sélectionné.');
      // Afficher une notification ou un message d'erreur
      this.showSnackbar('Aucun fichier sélectionné.', 'Erreur');
      return;
    }

    // Si au moins un fichier a été sélectionné, procédez à l'envoi des fichiers
    this.ser.uploadFilesEq(siteId, this.selectedFiles as File[]).subscribe((data) => {
      this.showSnackbar('Les images ont été ajoutées avec succès.', 'Succès');
      this.cancel();
      console.log("Les images ont été ajoutées avec succès.");
    });

    console.log('Selected files:', this.selectedFiles);
  }


  showSnackbar(message: string, action: string) {
    this.snackBar.open(message, action, {
      duration: 8000, // Durée d'affichage de la notification en millisecondes
      horizontalPosition: 'right', // Position horizontale de la notification
      verticalPosition: 'top', // Position verticale de la notification
    });
  }
  removePhoto(index: number) {

    if (index !== -1) {
      this.additionalPhotos.splice(index, 1); // Supprimer la photo du tableau additionalPhotos
      this.selectedFiles.splice(index + 1, 1); // Supprimer le fichier correspondant du tableau selectedFiles
    }
  }
  removePhotoP(index: number) {
    if (index === 1) {
      // Pour l'image principale
      this.previewImageUrl1 = this.defaultImageUrl;
      this.selectedFiles[0] = null; // Réinitialiser le fichier sélectionné à null
      const fileInput = document.getElementById('fileInput1') as HTMLInputElement;
      if (fileInput) {
        fileInput.value = ''; // Réinitialiser la valeur de l'input file à une chaîne vide
      }
    }

  }


  cancel(){
    this.dialogRef.close();
  }

}
