import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../item-list-lock.test-samples';

import { ItemListLockFormService } from './item-list-lock-form.service';

describe('ItemListLock Form Service', () => {
  let service: ItemListLockFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ItemListLockFormService);
  });

  describe('Service methods', () => {
    describe('createItemListLockFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createItemListLockFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            userUuid: expect.any(Object),
            quantity: expect.any(Object),
            lockTime: expect.any(Object),
            tire: expect.any(Object),
          }),
        );
      });

      it('passing IItemListLock should create a new form with FormGroup', () => {
        const formGroup = service.createItemListLockFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            userUuid: expect.any(Object),
            quantity: expect.any(Object),
            lockTime: expect.any(Object),
            tire: expect.any(Object),
          }),
        );
      });
    });

    describe('getItemListLock', () => {
      it('should return NewItemListLock for default ItemListLock initial value', () => {
        const formGroup = service.createItemListLockFormGroup(sampleWithNewData);

        const itemListLock = service.getItemListLock(formGroup) as any;

        expect(itemListLock).toMatchObject(sampleWithNewData);
      });

      it('should return NewItemListLock for empty ItemListLock initial value', () => {
        const formGroup = service.createItemListLockFormGroup();

        const itemListLock = service.getItemListLock(formGroup) as any;

        expect(itemListLock).toMatchObject({});
      });

      it('should return IItemListLock', () => {
        const formGroup = service.createItemListLockFormGroup(sampleWithRequiredData);

        const itemListLock = service.getItemListLock(formGroup) as any;

        expect(itemListLock).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IItemListLock should not enable id FormControl', () => {
        const formGroup = service.createItemListLockFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewItemListLock should disable id FormControl', () => {
        const formGroup = service.createItemListLockFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
