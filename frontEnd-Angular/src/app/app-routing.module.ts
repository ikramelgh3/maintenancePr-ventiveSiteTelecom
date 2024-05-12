import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {FirstTemplateComponent} from "./first-template/first-template.component";
import {PlanningMaintenanceComponent} from "./planning-maintenance/planning-maintenance.component";
import {NewPlanningComponent} from "./new-planning/new-planning.component";
import {UpdatePlanningComponent} from "./update-planning/update-planning.component";
import {SiteComponent} from "./site/site.component";
import {ParametrageComponent} from "./parametrage/parametrage.component";
import {NewSiteComponent} from "./new/new-site/new-site.component";
import {EquiipementsComponent} from "./equipements/equiipements/equiipements.component";

const routes: Routes = [
  {path: "Home" ,component:FirstTemplateComponent},
  {path: "PlanningsMaintenance" ,component:PlanningMaintenanceComponent},
  {path: "Sites" ,component:SiteComponent},
  {path: "Parametrages" ,component:ParametrageComponent},
  {path: "Equipements" ,component:EquiipementsComponent}



];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
