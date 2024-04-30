import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlanningMaintenanceComponent } from './planning-maintenance.component';

describe('PlanningMaintenanceComponent', () => {
  let component: PlanningMaintenanceComponent;
  let fixture: ComponentFixture<PlanningMaintenanceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PlanningMaintenanceComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PlanningMaintenanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
