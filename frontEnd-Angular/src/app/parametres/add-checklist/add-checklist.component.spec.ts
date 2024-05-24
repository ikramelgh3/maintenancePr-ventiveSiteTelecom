import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddChecklistComponent } from './add-checklist.component';

describe('AddChecklistComponent', () => {
  let component: AddChecklistComponent;
  let fixture: ComponentFixture<AddChecklistComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddChecklistComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddChecklistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
