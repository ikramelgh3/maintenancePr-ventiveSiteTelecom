import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {PlanningServiceService} from "../../../../service/planning-service.service";
import {MatSnackBar, MatSnackBarRef} from "@angular/material/snack-bar";
import {Dc} from "../../models/dc";
import {Dr} from "../../models/dr";
import {CentreTechnique} from "../../models/centreTechnique";

@Component({
  selector: 'app-add-ct',
  templateUrl: './add-ct.component.html',
  styleUrl: './add-ct.component.css'
})
export class AddCTComponent  implements  OnInit{


  addForm!:FormGroup;
  dc!: Dc[];
  dr!: Dr[];
  CentreTche!: CentreTechnique;
  id!: number;
  exist!: boolean;
  constructor(

    private ref: MatDialogRef<AddCTComponent>,
    private fb: FormBuilder,
    private ser: PlanningServiceService,
    private snackBar: MatSnackBar
  ) {
    ref.disableClose = true;
  }
  ngOnInit() {
    this.initForm();
    this.loadAllDr();
  }


  initForm() {
    this.addForm = this.fb.group({
      name: ['', Validators.required],
      dcName: ['', Validators.required],
      dRName: ['', Validators.required]
    });
  }




  loadAllDr() {
    this.ser.getDR().subscribe((data) => {
      this.dr = data;

    });
  }
  onDrSelectionChange(event: any) {
    const drId = event.value;
    this.loadDcByDrId(drId);
  }
  loadDcByDrId(drId: number) {
    this.ser.getDCsbyDR(drId).subscribe((data) => {
      this.dc = data;
      console.log("dc" , this.dc)
    });
  }


  onCancel() {
    this.ref.close();
  }
  selectedDc: any;
  add() {
    const ct = this.addForm.value;
    this.selectedDc = this.dc.find(dc => dc.id === ct.dcName);

    console.log("dc", ct.dcName);

    // Vérifier si un planning avec le même nom existe déjà
    this.ser.checkCTExist(ct.name).subscribe(
      (exists) => {
        if (exists) {
          const snackBarRef1: MatSnackBarRef<any> = this.snackBar.open('Ce centre technique existe déjà. Veuillez choisir un autre nom.', 'Modifier', {
            duration: 0,
            horizontalPosition: 'right',
            verticalPosition: 'top',
            panelClass: ['blue-snackbar']
          });
        } else {
          // Ajouter le planning
          this.ser.addCT(ct , ct.dcName).subscribe(
            (data) => {
              this.onCancel();
              const snackBarRef3: MatSnackBarRef<any> = this.snackBar.open('Le centre technique est bien ajouté', '', {
                duration: 8000,
                horizontalPosition: 'right',
                verticalPosition: 'top',
                panelClass: ['blue-snackbar']
              });
              snackBarRef3.onAction().subscribe(() => {
                snackBarRef3.dismiss();
              });
            },
            (error) => {
              console.error(error);
              const snackBarRef4: MatSnackBarRef<any> = this.snackBar.open('Une erreur s\'est produite lors de l\'ajout du planning.', 'Ok', {
                duration: 0,
                horizontalPosition: 'right',
                verticalPosition: 'top',
                panelClass: ['blue-snackbar']
              });
            }
          );
        }
      },
      (error) => {
        console.error(error);
        const snackBarRef5: MatSnackBarRef<any> = this.snackBar.open('Une erreur s\'est produite lors de la vérification de l\'existence du planning.', 'Ok', {
          duration: 0,
          horizontalPosition: 'right',
          verticalPosition: 'top',
        });
      }
    );
  }

}
