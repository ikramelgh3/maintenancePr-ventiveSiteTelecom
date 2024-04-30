import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdatePlanningComponent } from './update-planning.component';

describe('UpdatePlanningComponent', () => {
  let component: UpdatePlanningComponent;
  let fixture: ComponentFixture<UpdatePlanningComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UpdatePlanningComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UpdatePlanningComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
