import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddPtToChecklistComponent } from './add-pt-to-checklist.component';

describe('AddPtToChecklistComponent', () => {
  let component: AddPtToChecklistComponent;
  let fixture: ComponentFixture<AddPtToChecklistComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddPtToChecklistComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddPtToChecklistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
