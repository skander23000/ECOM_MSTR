import { TestBed } from '@angular/core/testing';

import { SharedUserDataService } from './shared-user-data.service';

describe('SharedUserDataService', () => {
  let service: SharedUserDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SharedUserDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
