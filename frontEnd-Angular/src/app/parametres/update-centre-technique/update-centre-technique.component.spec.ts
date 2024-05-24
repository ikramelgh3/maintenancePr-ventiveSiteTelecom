import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateCentreTechniqueComponent } from './update-centre-technique.component';

describe('UpdateCentreTechniqueComponent', () => {
  let component: UpdateCentreTechniqueComponent;
  let fixture: ComponentFixture<UpdateCentreTechniqueComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UpdateCentreTechniqueComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UpdateCentreTechniqueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
