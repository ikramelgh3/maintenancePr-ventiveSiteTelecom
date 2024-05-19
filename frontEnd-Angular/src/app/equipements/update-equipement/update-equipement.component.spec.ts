import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateEquipementComponent } from './update-equipement.component';

describe('UpdateEquipementComponent', () => {
  let component: UpdateEquipementComponent;
  let fixture: ComponentFixture<UpdateEquipementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UpdateEquipementComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UpdateEquipementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
