import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OpenPicSiteComponent } from './open-pic-site.component';

describe('OpenPicSiteComponent', () => {
  let component: OpenPicSiteComponent;
  let fixture: ComponentFixture<OpenPicSiteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [OpenPicSiteComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(OpenPicSiteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
