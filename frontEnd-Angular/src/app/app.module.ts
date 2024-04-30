import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2SearchPipeModule } from 'ng2-search-filter';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { FirstTemplateComponent } from './first-template/first-template.component';
import {MatToolbar, MatToolbarModule} from "@angular/material/toolbar";
import {MatButton, MatButtonModule, MatIconButton} from "@angular/material/button";
import {MatIcon, MatIconModule} from "@angular/material/icon";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatList, MatListModule} from "@angular/material/list";
import {NgxPaginationModule} from 'ngx-pagination';
import { PlanningMaintenanceComponent } from './planning-maintenance/planning-maintenance.component';
import {MatCard, MatCardModule} from "@angular/material/card";
import {MatFormField, MatFormFieldModule} from "@angular/material/form-field";
import {HttpClientModule} from "@angular/common/http";
import {MatTableModule} from "@angular/material/table";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatSortModule} from '@angular/material/sort';

import {MatInput, MatInputModule} from "@angular/material/input";
import { NewPlanningComponent } from './new-planning/new-planning.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { MatDialogModule} from "@angular/material/dialog";
import {MatSelectModule} from "@angular/material/select";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule} from "@angular/material/core";
import { MatConfirmDialogComponent } from './mat-confirm-dialog/mat-confirm-dialog.component';
import {MatSnackBarModule} from "@angular/material/snack-bar";
import { UpdatePlanningComponent } from './update-planning/update-planning.component';


@NgModule({
  declarations: [
    AppComponent,
    FirstTemplateComponent,
    PlanningMaintenanceComponent,
    NewPlanningComponent,
    MatConfirmDialogComponent,
    UpdatePlanningComponent,


  ],
  imports: [
    BrowserModule,
    NgxPaginationModule,
    AppRoutingModule,
    MatToolbarModule,
    MatButtonModule,
    MatIcon,
    MatIconModule,
    MatMenu,
    MatMenuTrigger,
    MatMenuItem,
    MatSidenavModule,
    MatListModule,
    MatCardModule,
    MatFormFieldModule,
    HttpClientModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatInputModule,
    MatDialogModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    ReactiveFormsModule,
    MatSnackBarModule,



  ],
  providers: [
    provideAnimationsAsync()
  ],
  bootstrap: [AppComponent],

})
export class AppModule { }
