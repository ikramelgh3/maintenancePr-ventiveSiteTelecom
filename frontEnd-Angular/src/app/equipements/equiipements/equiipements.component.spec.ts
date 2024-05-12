import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EquiipementsComponent } from './equiipements.component';

describe('EquiipementsComponent', () => {
  let component: EquiipementsComponent;
  let fixture: ComponentFixture<EquiipementsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EquiipementsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EquiipementsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
