import {Component, Inject, OnInit} from '@angular/core';
import {PlanningServiceService} from "../../../../service/planning-service.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-open-pic-site',
  templateUrl: './open-pic-site.component.html',
  styleUrl: './open-pic-site.component.css'
})
export class OpenPicSiteComponent implements  OnInit{


  ngOnInit() {
  }



  constructor(@Inject(MAT_DIALOG_DATA) public data: any) {}

}
