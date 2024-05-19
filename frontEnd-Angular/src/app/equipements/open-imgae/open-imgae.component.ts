import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";

@Component({
  selector: 'app-open-imgae',
  templateUrl: './open-imgae.component.html',
  styleUrl: './open-imgae.component.css'
})
export class OpenImgaeComponent implements  OnInit{
  ngOnInit() {
  }

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) {}

}
