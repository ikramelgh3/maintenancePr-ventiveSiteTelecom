import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestionApplComponent } from './gestion-appl.component';

describe('GestionApplComponent', () => {
  let component: GestionApplComponent;
  let fixture: ComponentFixture<GestionApplComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [GestionApplComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GestionApplComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
