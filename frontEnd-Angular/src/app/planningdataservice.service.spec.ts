import { TestBed } from '@angular/core/testing';

import { PlanningdataserviceService } from './planningdataservice.service';

describe('PlanningdataserviceService', () => {
  let service: PlanningdataserviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PlanningdataserviceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
