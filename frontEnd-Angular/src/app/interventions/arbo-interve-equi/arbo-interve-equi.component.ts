import {Component, Inject, OnInit} from '@angular/core';
import {Site} from "../../models/Site";
import {Immuble} from "../../models/immuble";
import {Etage} from "../../models/etage";
import {Salle} from "../../models/salle";
import {PlanningServiceService} from "../../../../service/planning-service.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Equipement} from "../../models/equipement";
import {KeycloakProfile} from "keycloak-js";
import {KeycloakService} from "keycloak-angular";

@Component({
  selector: 'app-arbo-interve-equi',
  templateUrl: './arbo-interve-equi.component.html',
  styleUrl: './arbo-interve-equi.component.css'
})
export class ArboInterveEquiComponent implements OnInit {
  panelOpenState = false;
  sites: Site[] = [];
  immubles: Immuble[] = [];
  etage: Etage[] = [];
  salle: Salle[] = [];
  clic: boolean = false;
  clicImmuble: boolean = false;
  clicEtage: boolean = false;
  clicSalle: boolean = false;
  public  profil!:KeycloakProfile
  public userId: string | null = null;
  centre!: String;
  sitebyCt!: Site[];

  constructor(
    private ser: PlanningServiceService,
    public dialogRef: MatDialogRef<ArboInterveEquiComponent>,public  ky :KeycloakService,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}
  ngOnInit() {
  this.getAllSite()
  }

  getAllSite() {


      this.ser.getAllSites().subscribe((data) => {
        this.sites = data;
        this.clic = true;
      });
    }



  getCentreTechniqueOfProf() {
    // Vérifier si userId est défini
    if (this.userId !== null) {
      // Appeler la fonction getCentreTechniqueOfRespo avec userId
      this.ser.getCentreTechniqueOfRespo(this.userId).subscribe((data) => {
        this.centre = data;
        this.getSiteOfCentr();

        this.getSiteByCt(this.centre);
        console.log("centre", this.centre);
      });
    } else {
      console.error('UserId est null. Impossible d\'appeler getCentreTechniqueOfRespo.');
    }
  }

  getSiteOfCentr() {
    console.log("le centre shhbce", this.centre);
    this.ser.getAllSites().subscribe((data) => {
      console.log("site", data);
      this.sites = data;
    });
  }

  getSiteByCt(ct: String) {
    this.ser.getSiteByCentre(ct).subscribe((data) => {
      this.sitebyCt = data;
      console.log("les sites", this.sitebyCt);
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


  selectEqui(equipement: Equipement) {
    this.dialogRef.close(equipement);
  }



  equipements!:Equipement[];
  clicEq:boolean =false
  toggleEquipemnts(code: string) {
    this.ser.getAllEquipementsOfSallle(code).subscribe((data) => {
      this.equipements = data;
      this.clicEq = true;
    });
  }
}
