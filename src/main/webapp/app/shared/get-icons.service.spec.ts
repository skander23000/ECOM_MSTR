import { TestBed } from '@angular/core/testing';

import { GetIconsService } from './get-icons.service';

describe('GetIconsService', () => {
  let service: GetIconsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GetIconsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
