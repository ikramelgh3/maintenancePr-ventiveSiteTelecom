import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddPlanningtoSiteComponent } from './add-planningto-site.component';

describe('AddPlanningtoSiteComponent', () => {
  let component: AddPlanningtoSiteComponent;
  let fixture: ComponentFixture<AddPlanningtoSiteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddPlanningtoSiteComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddPlanningtoSiteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
