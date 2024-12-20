import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-mat-confirm-dialog',
  templateUrl: './mat-confirm-dialog.component.html',
  styleUrl: './mat-confirm-dialog.component.css'
})
export class MatConfirmDialogComponent implements  OnInit{

  constructor(@Inject(MAT_DIALOG_DATA) public data:any, public dialogRef:MatDialogRef<MatConfirmDialogComponent>) {


  }

  ngOnInit() {
  }

  close(){
    this.dialogRef.close(false);
  }
}
