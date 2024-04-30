import { TestBed } from '@angular/core/testing';

import { RefrechSerService } from './refrech-ser.service';

describe('RefrechSerService', () => {
  let service: RefrechSerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RefrechSerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
