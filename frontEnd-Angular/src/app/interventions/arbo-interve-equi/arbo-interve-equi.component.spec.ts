import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArboInterveEquiComponent } from './arbo-interve-equi.component';

describe('ArboInterveEquiComponent', () => {
  let component: ArboInterveEquiComponent;
  let fixture: ComponentFixture<ArboInterveEquiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ArboInterveEquiComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ArboInterveEquiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
