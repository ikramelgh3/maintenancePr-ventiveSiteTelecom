import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddEquipemntToSiteComponent } from './add-equipemnt-to-site.component';

describe('AddEquipemntToSiteComponent', () => {
  let component: AddEquipemntToSiteComponent;
  let fixture: ComponentFixture<AddEquipemntToSiteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddEquipemntToSiteComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddEquipemntToSiteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
