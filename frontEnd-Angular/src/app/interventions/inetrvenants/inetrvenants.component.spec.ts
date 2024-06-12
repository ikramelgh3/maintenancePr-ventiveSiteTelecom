import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InetrvenantsComponent } from './inetrvenants.component';

describe('InetrvenantsComponent', () => {
  let component: InetrvenantsComponent;
  let fixture: ComponentFixture<InetrvenantsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [InetrvenantsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(InetrvenantsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
