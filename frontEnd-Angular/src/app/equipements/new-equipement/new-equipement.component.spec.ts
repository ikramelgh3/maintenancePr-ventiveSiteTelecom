import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewEquipementComponent } from './new-equipement.component';

describe('NewEquipementComponent', () => {
  let component: NewEquipementComponent;
  let fixture: ComponentFixture<NewEquipementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NewEquipementComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NewEquipementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
