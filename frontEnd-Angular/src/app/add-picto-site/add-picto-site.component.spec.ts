import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddPictoSiteComponent } from './add-picto-site.component';

describe('AddPictoSiteComponent', () => {
  let component: AddPictoSiteComponent;
  let fixture: ComponentFixture<AddPictoSiteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddPictoSiteComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddPictoSiteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
