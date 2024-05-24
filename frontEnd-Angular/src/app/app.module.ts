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
import { NewEquipementComponent } from './equipements/new-equipement/new-equipement.component';
import { ArborescenceComponent } from './equipements/arborescence/arborescence.component';
import {FilterBySitePipe} from "./equipements/arborescence/filter-by-site.pipe";
import {MatExpansionModule} from "@angular/material/expansion";
import { UpdateEquipementComponent } from './equipements/update-equipement/update-equipement.component';
import { OpenImgaeComponent } from './equipements/open-imgae/open-imgae.component';
import { AddPicComponent } from './equipements/add-pic/add-pic.component';
import { AddEquipemntToSiteComponent } from './site/add-equipemnt-to-site/add-equipemnt-to-site.component';
import { AddPlanningtoSiteComponent } from './site/add-planningto-site/add-planningto-site.component';
import { DetailOfSousLiieuxComponent } from './site/detail-of-sous-liieux/detail-of-sous-liieux.component';
import { GestionApplComponent } from './parametres/gestion-appl/gestion-appl.component';
import { UpdateCentreTechniqueComponent } from './parametres/update-centre-technique/update-centre-technique.component';

import {MatProgressBarModule} from '@angular/material/progress-bar';
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import { UpdatePointMesureComponent } from './parametres/update-point-mesure/update-point-mesure.component';
import { AddChecklistComponent } from './parametres/add-checklist/add-checklist.component';
import { AddCTComponent } from './parametres/add-ct/add-ct.component';
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
    NewEquipementComponent,
    ArborescenceComponent,
    FilterBySitePipe,
    UpdateEquipementComponent,
    OpenImgaeComponent,
    AddPicComponent,
    AddEquipemntToSiteComponent,
    AddPlanningtoSiteComponent,
    DetailOfSousLiieuxComponent,
    GestionApplComponent,
    UpdateCentreTechniqueComponent,
    UpdatePointMesureComponent,
    AddChecklistComponent,
    AddCTComponent,




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
    MatRadioModule,
    MatExpansionModule,
    MatProgressBarModule,
    MatProgressSpinner
  ],
  providers: [
    provideAnimationsAsync()
  ],
  bootstrap: [AppComponent],

})
export class AppModule { }
