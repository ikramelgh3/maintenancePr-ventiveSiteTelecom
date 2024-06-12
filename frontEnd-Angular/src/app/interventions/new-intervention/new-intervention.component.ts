import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { User } from "../../models/user";
import { Equipement } from "../../models/equipement";
import { PlanningMaintenanceDTO } from "../../models/PlanningMaintenanceDTO";
import { PlanningServiceService } from "../../../../service/planning-service.service";
import { MatDialog, MatDialogRef } from "@angular/material/dialog";
import { MatSnackBar, MatSnackBarRef } from "@angular/material/snack-bar";
import { ArborescenceComponent } from "../../equipements/arborescence/arborescence.component";
import { ArboInterveEquiComponent } from "../arbo-interve-equi/arbo-interve-equi.component";
import { Intervention } from "../../models/intervention";
import { KeycloakProfile } from "keycloak-js";
import { KeycloakService } from "keycloak-angular";
import { AuthServiceService } from "../../users/auth-service.service";

@Component({
  selector: 'app-new-intervention',
  templateUrl: './new-intervention.component.html',
  styleUrls: ['./new-intervention.component.css']
})
export class NewInterventionComponent implements OnInit {
  public profil: KeycloakProfile | null = null;

  constructor(
    private ser: PlanningServiceService,
    private fb: FormBuilder,
    private keycloakService: KeycloakService,
    private userProfileService: AuthServiceService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<NewInterventionComponent>,
  ) {
    dialogRef.disableClose = true;

    this.addForm = this.fb.group({
      name: ['', Validators.required],
      dateDebut: ['', Validators.required],
      priorite: ['', Validators.required],
      technicien: ['', Validators.required],
      type: ['', Validators.required],
      equipement: [this.equipement1, Validators.required],
      heureDebut: ['', Validators.required],
      planning: [''],
    });
    this.generateHours();
  }
  hours: string[] = [];
  generateHours(): void {
    for (let hour = 0; hour < 24; hour++) {
      for (let minute = 0; minute < 60; minute += 5) {
        const formattedHour = hour.toString().padStart(2, '0');
        const formattedMinute = minute.toString().padStart(2, '0');
        this.hours.push(`${formattedHour}:${formattedMinute}`);
      }
    }
  }
  addForm!: FormGroup;
  tech!: User[];
  equipements!: Equipement[];
  type: string[] = ['Preventive', 'Corrective'];
  priorites: String[] = ['Faible', 'Normal', 'Urgent'];
  techniciensAvailable!: User[];
  plannings!: PlanningMaintenanceDTO[];
  equipement!: Equipement;
  equipement1!: Equipement;
  respo!: User;
  intervention!: Intervention;

  ngOnInit() {


    this.keycloakService.loadUserProfile().then(
      (profile: KeycloakProfile) => {
        this.profil = profile;
        console.log('Profil chargé :', this.profil);
        this.getPlannings();
        this.getTechnicienDisponible();
        this.addForm.get('type')?.valueChanges.subscribe(value => {
          if (value === 'Preventive') {
            this.addForm.patchValue({ priorite: 'Normal' });
          }
        });
      },
      (error) => {
        console.error('Erreur lors du chargement du profil utilisateur :', error);
      }
    );
  }

  getPlannings() {
    this.ser.getPlannings().subscribe((data) => {
      this.plannings = data;
    });
  }

  getTechnicienDisponible() {
    this.ser.getUsersAvaible().subscribe((data) => {
      this.techniciensAvailable = data;
      console.log("tehnicien" , this.techniciensAvailable)
    });
  }

  arbo() {
    const dialogRef = this.dialog.open(ArboInterveEquiComponent, {
      width: '30%',
      height: '300px',
      exitAnimationDuration: '500ms',
      data: { title: 'Equipement' }
    });

    dialogRef.afterClosed().subscribe((selectedEquipement: Equipement) => {
      if (selectedEquipement) {
        this.equipement = selectedEquipement;
        this.addForm.patchValue({
          equipement: this.equipement // Mettre à jour le champ d'équipement dans le formulaire
        });
      }
    });
  }
  add() {
    if (this.addForm.valid) {
      if (!this.profil || !this.profil.id) {
        console.error('Profil non défini ou ID manquant.');
        return;
      }


      this.intervention = this.addForm.value;

      console.log("zz",this.intervention)
      const idRespo = this.profil.id;
      this.intervention = this.addForm.value;

      if (this.equipement && this.equipement.id) {
        const idTech = this.intervention.technicien.id;


        // Vérifier l'unicité du nom de l'intervention
        this.ser.checkIntervention(this.intervention.name).subscribe((exists) => {
          if (exists) {
            console.log(exists);
            const snackBarRef1: MatSnackBarRef<any> = this.snackBar.open('Cette intervention existe déjà.', 'Modifier', {
              duration: 5000,
              horizontalPosition: 'right',
              verticalPosition: 'top',
              panelClass: ['blue-snackbar']
            });
          } else {


            if (this.intervention.type === 'Preventive') {
              const idPl = this.intervention.planning.id;
              // Enregistrer l'intervention si le nom n'existe pas déjà
              this.ser.addIntervention(this.intervention, idTech, idPl, this.equipement.id, idRespo).subscribe((data) => {
                console.log(data);
                this.closePopup();

                const interventionAddedSnackBar = this.snackBar.open('L\'intervention a bien été ajoutée.', 'OK', {
                  duration: 3000,
                  horizontalPosition: 'right',
                  verticalPosition: 'top',
                  panelClass: ['blue-snackbar']
                });

                // Utiliser l'ID de l'intervention retourné par l'API
                const interventionId = data.id; // Assurez-vous que `data` contient l'ID de l'intervention

                // Afficher la notification que l'email est en train d'être envoyé
                const sendingEmailSnackBar = this.snackBar.open('Intervention ajoutéé,envoi de l\'email en cours au technicien concerné...', '', {
                  duration: -1, // Durée illimitée
                  horizontalPosition: 'right',
                  verticalPosition: 'top',
                  panelClass: ['blue-snackbar']
                });

                this.ser.sendEmail(idTech, interventionId, idRespo).subscribe(() => {
                  console.log("Envoie d'email à", idTech, "intervention", interventionId);

                  // Fermer la notification d'envoi de l'email
                  sendingEmailSnackBar.dismiss();

                  // Afficher la notification que l'email a été envoyé avec succès pendant une courte durée
                  const emailSentSnackBar = this.snackBar.open('Email envoyé avec succès.', 'OK', {
                    duration: 5000, // Durée courte
                    horizontalPosition: 'right',
                    verticalPosition: 'top',
                    panelClass: ['blue-snackbar']
                  });
                }, error => {
                  console.error('Erreur lors de l\'envoi de l\'email', error);
                  this.snackBar.open('Erreur lors de l\'envoi de l\'email', 'Fermer', {
                    duration: 5000,
                    horizontalPosition: 'right',
                    verticalPosition: 'top',
                    panelClass: ['blue-snackbar']
                  });
                });
              }, error => {
                console.error('Erreur lors de l\'ajout de l\'intervention', error);
                this.snackBar.open('Erreur lors de l\'ajout de l\'intervention', 'Fermer', {
                  duration: 5000,
                  horizontalPosition: 'right',
                  verticalPosition: 'top',
                  panelClass: ['blue-snackbar']
                });
              });

            }
            else {
              const idPl = null;
              this.addForm.patchValue({
                planning: null
              });

              console.log("ds<",this.addForm.value)
              // Enregistrer l'intervention si le nom n'existe pas déjà
              this.ser.addInterventionC(this.addForm.value, idTech, this.equipement.id, idRespo).subscribe((data) => {
                console.log(data);
                this.closePopup();
console.log( "ikd", this.intervention)
                const interventionAddedSnackBar = this.snackBar.open('L\'intervention a bien été ajoutée.', 'OK', {
                  duration: 3000,
                  horizontalPosition: 'right',
                  verticalPosition: 'top',
                  panelClass: ['blue-snackbar']
                });

                // Utiliser l'ID de l'intervention retourné par l'API
                const interventionId = data.id; // Assurez-vous que `data` contient l'ID de l'intervention

                // Afficher la notification que l'email est en train d'être envoyé
                const sendingEmailSnackBar = this.snackBar.open('Intervention ajoutéé,envoi de l\'email en cours au technicien concerné...', '', {
                  duration: -1, // Durée illimitée
                  horizontalPosition: 'right',
                  verticalPosition: 'top',
                  panelClass: ['blue-snackbar']
                });

                this.ser.sendEmail(idTech, interventionId, idRespo).subscribe(() => {
                  console.log("Envoie d'email à", idTech, "intervention", interventionId);

                  // Fermer la notification d'envoi de l'email
                  sendingEmailSnackBar.dismiss();

                  // Afficher la notification que l'email a été envoyé avec succès pendant une courte durée
                  const emailSentSnackBar = this.snackBar.open('Email envoyé avec succès.', 'OK', {
                    duration: 5000, // Durée courte
                    horizontalPosition: 'right',
                    verticalPosition: 'top',
                    panelClass: ['blue-snackbar']
                  });
                }, error => {
                  console.error('Erreur lors de l\'envoi de l\'email', error);
                  this.snackBar.open('Erreur lors de l\'envoi de l\'email', 'Fermer', {
                    duration: 5000,
                    horizontalPosition: 'right',
                    verticalPosition: 'top',
                    panelClass: ['blue-snackbar']
                  });
                });
              }, error => {
                console.error('Erreur lors de l\'ajout de l\'intervention', error);
                this.snackBar.open('Erreur lors de l\'ajout de l\'intervention', 'Fermer', {
                  duration: 5000,
                  horizontalPosition: 'right',
                  verticalPosition: 'top',
                  panelClass: ['blue-snackbar']
                });
              });
            }


          }
        });
      } else {
        // Gérer le cas où this.equipement est indéfini ou sa propriété id est indéfinie
        console.error("Erreur : L'équipement sélectionné est invalide.");
      }
    } else {
      this.snackBar.open('Veuillez remplir correctement tous les champs du formulaire', 'Fermer', {
        duration: 3000,
        horizontalPosition: 'end',
        verticalPosition: 'top'
      });
    }
  }


  closePopup() {this.dialogRef.close() // Implémentez la logique de fermeture de la popup ici
  }
}
