import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../tire-brand.test-samples';

import { TireBrandFormService } from './tire-brand-form.service';

describe('TireBrand Form Service', () => {
  let service: TireBrandFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TireBrandFormService);
  });

  describe('Service methods', () => {
    describe('createTireBrandFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTireBrandFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            logoUrl: expect.any(Object),
          }),
        );
      });

      it('passing ITireBrand should create a new form with FormGroup', () => {
        const formGroup = service.createTireBrandFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            logoUrl: expect.any(Object),
          }),
        );
      });
    });

    describe('getTireBrand', () => {
      it('should return NewTireBrand for default TireBrand initial value', () => {
        const formGroup = service.createTireBrandFormGroup(sampleWithNewData);

        const tireBrand = service.getTireBrand(formGroup) as any;

        expect(tireBrand).toMatchObject(sampleWithNewData);
      });

      it('should return NewTireBrand for empty TireBrand initial value', () => {
        const formGroup = service.createTireBrandFormGroup();

        const tireBrand = service.getTireBrand(formGroup) as any;

        expect(tireBrand).toMatchObject({});
      });

      it('should return ITireBrand', () => {
        const formGroup = service.createTireBrandFormGroup(sampleWithRequiredData);

        const tireBrand = service.getTireBrand(formGroup) as any;

        expect(tireBrand).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITireBrand should not enable id FormControl', () => {
        const formGroup = service.createTireBrandFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTireBrand should disable id FormControl', () => {
        const formGroup = service.createTireBrandFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
