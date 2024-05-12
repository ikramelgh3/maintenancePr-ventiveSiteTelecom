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
import {MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {MatSelectModule} from "@angular/material/select";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {MatNativeDateModule} from "@angular/material/core";
import { MatConfirmDialogComponent } from './mat-confirm-dialog/mat-confirm-dialog.component';
import {MatSnackBarModule} from "@angular/material/snack-bar";
import { UpdatePlanningComponent } from './update-planning/update-planning.component';
import { SiteComponent } from './site/site.component';
import { ImportDialogComponent } from './import-dialog/import-dialog.component';
import { ParametrageComponent } from './parametrage/parametrage.component';
import {MatTab, MatTabGroup, MatTabsModule} from "@angular/material/tabs";
import {MatTreeModule} from '@angular/material/tree';
import { MatStepperModule} from "@angular/material/stepper";
import { ImportDataComponent } from './import-data/import-data.component';
import { NewSiteComponent } from './new/new-site/new-site.component';
import {MatSliderModule} from "@angular/material/slider";
import { SlickCarouselModule } from 'ngx-slick-carousel';
import { OpenPicSiteComponent } from './openPic/open-pic-site/open-pic-site.component';
import {MatCheckbox, MatCheckboxModule} from "@angular/material/checkbox";
import {MatRadioButton, MatRadioModule} from "@angular/material/radio";
import { AddPictoSiteComponent } from './add-picto-site/add-picto-site.component';
import { UpdateSiteComponent } from './update-site/update-site.component';
import { EquiipementsComponent } from './equipements/equiipements/equiipements.component';


@NgModule({
  declarations: [
    AppComponent,
    FirstTemplateComponent,
    PlanningMaintenanceComponent,
    NewPlanningComponent,
    MatConfirmDialogComponent,
    UpdatePlanningComponent,
    SiteComponent,
    ImportDialogComponent,
    ParametrageComponent,
    ImportDataComponent,
    NewSiteComponent,
    OpenPicSiteComponent,
    AddPictoSiteComponent,
    UpdateSiteComponent,
    EquiipementsComponent,



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
    FormsModule,
    MatTableModule,
    MatTabGroup,
    MatTab,
    MatTreeModule,
    MatStepperModule,
    MatTreeModule,
    MatSliderModule,
    SlickCarouselModule,
    MatCheckboxModule,
    MatRadioModule

  ],
  providers: [
    provideAnimationsAsync()
  ],
  bootstrap: [AppComponent],

})
export class AppModule { }
