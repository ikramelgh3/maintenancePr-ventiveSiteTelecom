import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddInterventionToPlanningComponent } from './add-intervention-to-planning.component';

describe('AddInterventionToPlanningComponent', () => {
  let component: AddInterventionToPlanningComponent;
  let fixture: ComponentFixture<AddInterventionToPlanningComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddInterventionToPlanningComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddInterventionToPlanningComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
