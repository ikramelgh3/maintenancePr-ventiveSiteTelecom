import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { PlanningServiceService } from '../../../../service/planning-service.service';
import { Site } from '../../models/Site';
import { Immuble } from '../../models/immuble';
import { Etage } from '../../models/etage';
import { Salle } from '../../models/salle';

@Component({
  selector: 'app-arborescence',
  templateUrl: './arborescence.component.html',
  styleUrls: ['./arborescence.component.css']
})
export class ArborescenceComponent implements OnInit {
  panelOpenState = false;
  sites: Site[] = [];
  immubles: Immuble[] = [];
  etage: Etage[] = [];
  salle: Salle[] = [];
  clic: boolean = false;
  clicImmuble: boolean = false;
  clicEtage: boolean = false;
  clicSalle: boolean = false;

  constructor(
    private ser: PlanningServiceService,
    public dialogRef: MatDialogRef<ArborescenceComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  ngOnInit() {
    this.getAllSite();
  }

  getAllSite() {
    this.ser.getAllSites().subscribe((data) => {
      this.sites = data;
      this.clic = true;
    });
  }

  toggleBuilding(id: number) {
    this.ser.getImmubleOfSite(id).subscribe((data) => {
      this.immubles = data;
      this.clicImmuble = true;
    });
  }

  toggleFloor(id: number) {
    this.ser.getEtageOfImmuble(id).subscribe((data) => {
      this.etage = data;
      this.clicEtage = true;
    });
  }

  toggleRoom(id: number) {
    this.ser.getSalleOfEtage(id).subscribe((data) => {
      this.salle = data;
      this.clicSalle = true;
    });
  }

  selectSalle(salle: Salle) {
    this.dialogRef.close(salle);
  }

}
