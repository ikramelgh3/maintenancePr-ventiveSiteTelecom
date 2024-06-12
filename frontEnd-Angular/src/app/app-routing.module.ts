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
import {AuthGuard} from "./guards/auth.guard";
import {UsersComponent} from "./users/users/users.component";
import {InterventionsComponent} from "./interventions/interventions/interventions.component";
import {InetrvenantsComponent} from "./interventions/inetrvenants/inetrvenants.component";
import {MesInterventionsComponent} from "./MesInterventions/mes-interventions/mes-interventions.component";

const routes: Routes = [

  {path: "Home" ,component:FirstTemplateComponent, canActivate : [AuthGuard], data : {roles :['ADMIN']}},
  {path: "PlanningsMaintenance" ,component:PlanningMaintenanceComponent , canActivate : [AuthGuard], data: { roles: ['ADMIN','TECHNICIEN','RESPONSABLE DE MAINTENANCE'] } },
  {path: "Sites" ,component:SiteComponent , canActivate : [AuthGuard], data : {roles :['ADMIN' ,'INJECTEUR' ,'RESPONSABLE DE MAINTENANCE']}},
  {path: "Parametrages" ,component:ParametrageComponent, canActivate : [AuthGuard], data : {roles :['ADMIN','INJECTEUR']}},
  {path: "Equipements" ,component:EquiipementsComponent , canActivate : [AuthGuard], data : {roles :['ADMIN','TECHNICIEN','INJECTEUR', 'RESPONSABLE DE MAINTENANCE']}},
  {path: "users" ,component:UsersComponent, canActivate : [AuthGuard], data : {roles :['ADMIN']}},
  {path: "Intervention" ,component:InterventionsComponent , canActivate : [AuthGuard], data : {roles :['ADMIN','TECHNICIEN', 'RESPONSABLE DE MAINTENANCE']}},
  {path: "MesInterventions" ,component:MesInterventionsComponent, canActivate : [AuthGuard], data : {roles :['TECHNICIEN']}},
  {path: "Intervenants" ,component:InetrvenantsComponent , canActivate : [AuthGuard], data : {roles :[ 'RESPONSABLE DE MAINTENANCE','ADMIN']}}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
