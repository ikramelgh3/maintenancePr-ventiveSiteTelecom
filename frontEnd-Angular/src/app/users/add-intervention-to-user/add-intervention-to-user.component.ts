import { Component, Inject, OnInit } from '@angular/core';
import { PlanningServiceService } from "../../../../service/planning-service.service";
import { MatSnackBar, MatSnackBarRef } from "@angular/material/snack-bar";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from "@angular/material/dialog";
import { User } from "../../models/user";
import { KeycloakProfile } from "keycloak-js";
import { KeycloakService } from "keycloak-angular";
import { Equipement } from "../../models/equipement";
import { PlanningMaintenanceDTO } from "../../models/PlanningMaintenanceDTO";
import { Intervention } from "../../models/intervention";
import { ArboInterveEquiComponent } from "../../interventions/arbo-interve-equi/arbo-interve-equi.component";

@Component({
  selector: 'app-add-intervention-to-user',
  templateUrl: './add-intervention-to-user.component.html',
  styleUrls: ['./add-intervention-to-user.component.css']
})
export class AddInterventionToUserComponent implements OnInit {
  public profil: KeycloakProfile | null = null;
  addForm!: FormGroup;
  user1!: User;
  hours: string[] = [];
  tech!: User[];
  equipements!: Equipement[];
  type: string[] = ['Preventive', 'Corrective'];
  priorites: string[] = ['Faible', 'Normal', 'Urgent'];
  techniciensAvailable!: User[];
  plannings!: PlanningMaintenanceDTO[];
  equipement!: Equipement;
  equipement1!: Equipement;
  respo!: User;
  intervention!: Intervention;

  constructor(
    private ser: PlanningServiceService,
    private fb: FormBuilder,
    private dialog: MatDialog,
    private keycloakService: KeycloakService,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialogRef<AddInterventionToUserComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    dialogRef.disableClose = true;
    this.addForm = this.fb.group({
      name: ['', Validators.required],
      dateDebut: ['', Validators.required],
      priorite: ['', Validators.required],
      type: ['', Validators.required],
      equipement: ['', Validators.required],
      heureDebut: ['', Validators.required],
      planning: [''],
    });
    this.generateHours();
  }

  ngOnInit() {
    this.keycloakService.loadUserProfile().then(
      (profile: KeycloakProfile) => {
        this.profil = profile;
        console.log('Profil chargé :', this.profil);
        this.getPlannings();
        this.getTechnicienDisponible();
      },
      (error) => {
        console.error('Erreur lors du chargement du profil utilisateur :', error);
      }
    );
  }

  generateHours(): void {
    for (let hour = 0; hour < 24; hour++) {
      for (let minute = 0; minute < 60; minute += 5) {
        const formattedHour = hour.toString().padStart(2, '0');
        const formattedMinute = minute.toString().padStart(2, '0');
        this.hours.push(`${formattedHour}:${formattedMinute}`);
      }
    }
  }

  getPlannings() {
    this.ser.getPlannings().subscribe((data) => {
      this.plannings = data;
    });
  }

  getTechnicienDisponible() {
    this.ser.getUsersAvaible().subscribe((data) => {
      this.techniciensAvailable = data;
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
      const idRespo = this.profil.id;

      if (this.equipement && this.equipement.id) {
        this.intervention.equipement = this.equipement;

        // Vérifier l'unicité du nom de l'intervention
        this.ser.checkIntervention(this.intervention.name).subscribe((exists) => {
          if (exists) {
            const snackBarRef1: MatSnackBarRef<any> = this.snackBar.open('Cette intervention existe déjà.', 'Modifier', {
              duration: 5000,
              horizontalPosition: 'right',
              verticalPosition: 'top',
              panelClass: ['blue-snackbar']
            });
          } else {
            const idTech = this.data.user2.id;

            if (this.intervention.type === 'Preventive') {
              const idPl = this.intervention.planning?.id;
              // Enregistrer l'intervention si le nom n'existe pas déjà
              this.ser.addIntervention(this.intervention, idTech, idPl, this.equipement.id, idRespo).subscribe((data) => {
                this.handleSuccessfulIntervention(data, idTech, idRespo);
              }, (error) => {
                this.handleError('Erreur lors de l\'ajout de l\'intervention', error);
              });
            } else {
              const idPl = null;
              this.addForm.patchValue({ planning: null });
              this.ser.addInterventionC(this.addForm.value, idTech, this.equipement.id, idRespo).subscribe((data) => {
                this.handleSuccessfulIntervention(data, idTech, idRespo);
              }, (error) => {
                this.handleError('Erreur lors de l\'ajout de l\'intervention', error);
              });
            }
          }
        });
      } else {
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

  handleSuccessfulIntervention(data: any, idTech: string, idRespo: string) {
    this.closePopup();

    this.snackBar.open('L\'intervention a bien été ajoutée.', 'OK', {
      duration: 3000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
      panelClass: ['blue-snackbar']
    });

    const interventionId = data.id;
    const sendingEmailSnackBar = this.snackBar.open('Intervention ajoutée, envoi de l\'email en cours au technicien concerné...', '', {
      duration: -1, // Durée illimitée
      horizontalPosition: 'right',
      verticalPosition: 'top',
      panelClass: ['blue-snackbar']
    });

    this.ser.sendEmail(idTech, interventionId, idRespo).subscribe(() => {
      sendingEmailSnackBar.dismiss();
      this.snackBar.open('Email envoyé avec succès.', 'OK', {
        duration: 5000,
        horizontalPosition: 'right',
        verticalPosition: 'top',
        panelClass: ['blue-snackbar']
      });
    }, (error) => {
      this.handleError('Erreur lors de l\'envoi de l\'email', error);
    });
  }

  handleError(message: string, error: any) {
    console.error(message, error);
    this.snackBar.open(message, 'Fermer', {
      duration: 5000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
      panelClass: ['blue-snackbar']
    });
  }

  closePopup() {
    this.dialogRef.close();
  }
}
