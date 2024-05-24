import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddCTComponent } from './add-ct.component';

describe('AddCTComponent', () => {
  let component: AddCTComponent;
  let fixture: ComponentFixture<AddCTComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddCTComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddCTComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
