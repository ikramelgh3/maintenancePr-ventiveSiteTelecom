import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArborescenceComponent } from './arborescence.component';

describe('ArborescenceComponent', () => {
  let component: ArborescenceComponent;
  let fixture: ComponentFixture<ArborescenceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ArborescenceComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ArborescenceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
