import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateSiteComponent } from './update-site.component';

describe('UpdateSiteComponent', () => {
  let component: UpdateSiteComponent;
  let fixture: ComponentFixture<UpdateSiteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UpdateSiteComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UpdateSiteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
