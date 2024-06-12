import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddInterventionToEquipemntComponent } from './add-intervention-to-equipemnt.component';

describe('AddInterventionToEquipemntComponent', () => {
  let component: AddInterventionToEquipemntComponent;
  let fixture: ComponentFixture<AddInterventionToEquipemntComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddInterventionToEquipemntComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddInterventionToEquipemntComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
