import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailOfSousLiieuxComponent } from './detail-of-sous-liieux.component';

describe('DetailOfSousLiieuxComponent', () => {
  let component: DetailOfSousLiieuxComponent;
  let fixture: ComponentFixture<DetailOfSousLiieuxComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DetailOfSousLiieuxComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DetailOfSousLiieuxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
