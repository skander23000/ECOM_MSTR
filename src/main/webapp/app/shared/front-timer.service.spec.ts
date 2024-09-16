import { TestBed } from '@angular/core/testing';

import { FrontTimerService } from './front-timer.service';

describe('FrontTimerService', () => {
  let service: FrontTimerService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FrontTimerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
