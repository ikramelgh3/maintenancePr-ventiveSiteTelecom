import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdatePointMesureComponent } from './update-point-mesure.component';

describe('UpdatePointMesureComponent', () => {
  let component: UpdatePointMesureComponent;
  let fixture: ComponentFixture<UpdatePointMesureComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UpdatePointMesureComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UpdatePointMesureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
