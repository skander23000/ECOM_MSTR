import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../tire.test-samples';

import { TireFormService } from './tire-form.service';

describe('Tire Form Service', () => {
  let service: TireFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TireFormService);
  });

  describe('Service methods', () => {
    describe('createTireFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTireFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            reference: expect.any(Object),
            name: expect.any(Object),
            price: expect.any(Object),
            tireWidth: expect.any(Object),
            tireHeight: expect.any(Object),
            tireDiameter: expect.any(Object),
            tireType: expect.any(Object),
            imageUrl: expect.any(Object),
            speedIndex: expect.any(Object),
            weightIndex: expect.any(Object),
            quantity: expect.any(Object),
            disable: expect.any(Object),
            disableReason: expect.any(Object),
            description: expect.any(Object),
            tireBrand: expect.any(Object),
          }),
        );
      });

      it('passing ITire should create a new form with FormGroup', () => {
        const formGroup = service.createTireFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            reference: expect.any(Object),
            name: expect.any(Object),
            price: expect.any(Object),
            tireWidth: expect.any(Object),
            tireHeight: expect.any(Object),
            tireDiameter: expect.any(Object),
            tireType: expect.any(Object),
            imageUrl: expect.any(Object),
            speedIndex: expect.any(Object),
            weightIndex: expect.any(Object),
            quantity: expect.any(Object),
            disable: expect.any(Object),
            disableReason: expect.any(Object),
            description: expect.any(Object),
            tireBrand: expect.any(Object),
          }),
        );
      });
    });

    describe('getTire', () => {
      it('should return NewTire for default Tire initial value', () => {
        const formGroup = service.createTireFormGroup(sampleWithNewData);

        const tire = service.getTire(formGroup) as any;

        expect(tire).toMatchObject(sampleWithNewData);
      });

      it('should return NewTire for empty Tire initial value', () => {
        const formGroup = service.createTireFormGroup();

        const tire = service.getTire(formGroup) as any;

        expect(tire).toMatchObject({});
      });

      it('should return ITire', () => {
        const formGroup = service.createTireFormGroup(sampleWithRequiredData);

        const tire = service.getTire(formGroup) as any;

        expect(tire).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITire should not enable id FormControl', () => {
        const formGroup = service.createTireFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTire should disable id FormControl', () => {
        const formGroup = service.createTireFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
