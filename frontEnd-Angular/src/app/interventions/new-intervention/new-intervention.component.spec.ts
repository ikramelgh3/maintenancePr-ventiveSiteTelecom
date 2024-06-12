import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewInterventionComponent } from './new-intervention.component';

describe('NewInterventionComponent', () => {
  let component: NewInterventionComponent;
  let fixture: ComponentFixture<NewInterventionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NewInterventionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NewInterventionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
