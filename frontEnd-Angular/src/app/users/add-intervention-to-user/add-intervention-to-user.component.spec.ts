import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddInterventionToUserComponent } from './add-intervention-to-user.component';

describe('AddInterventionToUserComponent', () => {
  let component: AddInterventionToUserComponent;
  let fixture: ComponentFixture<AddInterventionToUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddInterventionToUserComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddInterventionToUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
