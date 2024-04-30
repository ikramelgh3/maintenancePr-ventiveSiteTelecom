import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {FirstTemplateComponent} from "./first-template/first-template.component";
import {PlanningMaintenanceComponent} from "./planning-maintenance/planning-maintenance.component";
import {NewPlanningComponent} from "./new-planning/new-planning.component";
import {UpdatePlanningComponent} from "./update-planning/update-planning.component";

const routes: Routes = [
  {path: "Home" ,component:FirstTemplateComponent},
  {path: "PlanningsMaintenance" ,component:PlanningMaintenanceComponent}


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
