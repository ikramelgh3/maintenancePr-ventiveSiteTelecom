import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OpenImgaeComponent } from './open-imgae.component';

describe('OpenImgaeComponent', () => {
  let component: OpenImgaeComponent;
  let fixture: ComponentFixture<OpenImgaeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [OpenImgaeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(OpenImgaeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
