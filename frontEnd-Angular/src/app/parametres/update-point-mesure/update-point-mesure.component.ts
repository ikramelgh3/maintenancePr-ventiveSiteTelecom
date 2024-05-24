import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { PlanningServiceService } from "../../../../service/planning-service.service";
import { MatSnackBar } from "@angular/material/snack-bar";
import { Checklist } from "../../models/checklist";
import { TypeEquipement } from "../../models/typeEquipement";

@Component({
  selector: 'app-update-point-mesure',
  templateUrl: './update-point-mesure.component.html',
  styleUrls: ['./update-point-mesure.component.css']
})
export class UpdatePointMesureComponent implements OnInit {
  updateForm!: FormGroup;
  id!: number;
  exist!: boolean;
  pointMesurer!: Checklist;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private ref: MatDialogRef<UpdatePointMesureComponent>,
    private fb: FormBuilder,
    private ser: PlanningServiceService,
    private snackBar: MatSnackBar
  ) {
    ref.disableClose = true;
  }

  ngOnInit() {
    this.initForm();
    this.id = this.data.pointMesure.id;
    this.loadPointMesurerData();
    this.getTypeEquip(this.data.pointMesure.typeEquipent.id);
  }

  initForm() {
    this.updateForm = this.fb.group({
      attribut: ['', Validators.required],
      typeEquipent: ['', Validators.required],
      resultatsPossibles: ['', Validators.required]
    });
  }

  typeEuipement!: TypeEquipement;
  getTypeEquip(id: number) {
    this.ser.getTypeEQUIById(id).subscribe((data) => {
      this.typeEuipement = data;
      console.log(this.typeEuipement);
    });
  }

  loadPointMesurerData() {
    console.log("loadPoint");
    this.ser.getChecklistById(this.id).subscribe((data) => {
      this.pointMesurer = data;
      console.log(this.pointMesurer);
      this.updateForm.patchValue({
        attribut: this.pointMesurer.attribut,
        resultatsPossibles: this.pointMesurer.resultatsPossibles.join(','), // Join array into string for form input
        typeEquipent: this.pointMesurer.typeEquipent.name
      });
    });
  }

  updateCT() {
    const updatedCentreTechnique: Checklist = {
      id: this.pointMesurer.id,
      attribut: this.updateForm.get('attribut')!.value,
      resultatsPossibles: this.updateForm.get('resultatsPossibles')!.value.split(','), // Split string into array
      typeEquipent: { id: this.pointMesurer.typeEquipent.id, name: this.updateForm.get('typeEquipent')!.value },
      respoId: this.pointMesurer.respoId,
      respoMaint: this.pointMesurer.respoMaint,
      typeEquipementId: this.pointMesurer.typeEquipementId
    };

    this.checkIfCtExist(updatedCentreTechnique);
  }

  checkIfCtExist(updatedCentreTechnique: Checklist) {
    this.ser.checkPointExist2(updatedCentreTechnique.attribut, updatedCentreTechnique.id).subscribe((data) => {
      this.exist = data;
      if (this.exist) {
        this.snackBar.open('Ce point de mesure existe déjà.', 'Fermer', {
          duration: 3000,
          horizontalPosition: 'right',
          verticalPosition: 'top',
        });
      } else {
        this.saveUpdatedCentreTechnique(updatedCentreTechnique);
      }
    });
  }

  saveUpdatedCentreTechnique(updatedCentreTechnique: Checklist) {
    this.ser.updateChecklist(this.id, updatedCentreTechnique).subscribe(() => {
      this.snackBar.open('Point de mesure mis à jour avec succès.', 'Fermer', {
        duration: 3000,
        horizontalPosition: 'right',
        verticalPosition: 'top',
      });
      this.ref.close(true);
    }, error => {
      this.snackBar.open('Erreur lors de la mise à jour du Centre Technique.', 'Fermer', {
        duration: 3000,
        horizontalPosition: 'right',
        verticalPosition: 'top',
      });
    });
  }

  onCancel() {
    this.ref.close();
  }
}
