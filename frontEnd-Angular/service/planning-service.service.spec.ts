import { TestBed } from '@angular/core/testing';

import { PlanningServiceService } from './planning-service.service';

describe('PlanningServiceService', () => {
  let service: PlanningServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PlanningServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
