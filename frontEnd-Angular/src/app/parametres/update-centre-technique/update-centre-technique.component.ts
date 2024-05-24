import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { PlanningServiceService } from '../../../../service/planning-service.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CentreTechnique } from '../../models/centreTechnique';
import { Dc } from '../../models/dc';
import { Dr } from '../../models/dr';

@Component({
  selector: 'app-update-centre-technique',
  templateUrl: './update-centre-technique.component.html',
  styleUrls: ['./update-centre-technique.component.css']
})
export class UpdateCentreTechniqueComponent implements OnInit {
  updateForm!: FormGroup;
  dc!: Dc[];
  dr!: Dr[];
  CentreTche!: CentreTechnique;
  id!: number;
  exist!: boolean;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: any,
    private ref: MatDialogRef<UpdateCentreTechniqueComponent>,
    private fb: FormBuilder,
    private ser: PlanningServiceService,
    private snackBar: MatSnackBar
  ) {
    ref.disableClose = true;
  }

  ngOnInit() {
    this.initForm();
    this.id = this.data.centre.id;
    this.loadCentreTechniqueData();
    this.loadAllDr();
  }

  initForm() {
    this.updateForm = this.fb.group({
      name: ['', Validators.required],
      dcName: ['', Validators.required],
      dRName: ['', Validators.required]
    });
  }

  loadCentreTechniqueData() {
    this.ser.getCentreTehniwueById(this.id).subscribe((data) => {
      this.CentreTche = data;
      this.updateForm.patchValue({
        name: this.CentreTche.name,
        dcName: this.CentreTche.dc.id,
        dRName: this.CentreTche.dc.dr.id
      });
      this.loadDcByDrId(this.CentreTche.dc.dr.id);
    });
  }

  loadAllDr() {
    this.ser.getDR().subscribe((data) => {
      this.dr = data;
    });
  }

  loadDcByDrId(drId: number) {
    this.ser.getDCsbyDR(drId).subscribe((data) => {
      this.dc = data;
    });
  }

  onDrSelectionChange(event: any) {
    const drId = event.value;
    this.loadDcByDrId(drId);
  }

  onCancel() {
    this.ref.close();
  }

  updateCT() {
    const updatedCentreTechnique: CentreTechnique = {
      id: this.CentreTche.id,
      name: this.updateForm.get('name')!.value,
      dc: {
        id: this.updateForm.get('dcName')!.value,
        name: '',
        dr: {
          id: this.updateForm.get('dRName')!.value,
          name: ''
        }
      }
    };

    this.checkIfCtExist(updatedCentreTechnique);

  }

  checkIfCtExist(updatedCentreTechnique: CentreTechnique) {
    this.ser.checkIfExisteCT(
      updatedCentreTechnique.name,
      updatedCentreTechnique.dc.id,
      updatedCentreTechnique.dc.dr.id
    ).subscribe((data) => {
      this.exist = data;
      if (this.exist) {
        this.snackBar.open('Centre Technique existe déjà.', 'Fermer', { duration: 3000,horizontalPosition: 'right',
          verticalPosition: 'top', });
      } else {
        this.saveUpdatedCentreTechnique(updatedCentreTechnique);
      }
    });
  }

  saveUpdatedCentreTechnique(updatedCentreTechnique: CentreTechnique) {
    this.ser.updateCentre(this.id,updatedCentreTechnique).subscribe(() => {
      this.snackBar.open('Centre Technique mis à jour avec succès.', 'Fermer', { duration: 3000 ,horizontalPosition: 'right',
        verticalPosition: 'top',});
      this.ref.close(true);
    }, error => {
      this.snackBar.open('Erreur lors de la mise à jour du Centre Technique.', 'Fermer', { duration: 3000,horizontalPosition: 'right',
        verticalPosition: 'top', });
    });
  }
}
