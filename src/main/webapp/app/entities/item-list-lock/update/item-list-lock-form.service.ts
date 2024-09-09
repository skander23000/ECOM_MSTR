import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IItemListLock, NewItemListLock } from '../item-list-lock.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IItemListLock for edit and NewItemListLockFormGroupInput for create.
 */
type ItemListLockFormGroupInput = IItemListLock | PartialWithRequiredKeyOf<NewItemListLock>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IItemListLock | NewItemListLock> = Omit<T, 'lockTime'> & {
  lockTime?: string | null;
};

type ItemListLockFormRawValue = FormValueOf<IItemListLock>;

type NewItemListLockFormRawValue = FormValueOf<NewItemListLock>;

type ItemListLockFormDefaults = Pick<NewItemListLock, 'id' | 'lockTime'>;

type ItemListLockFormGroupContent = {
  id: FormControl<ItemListLockFormRawValue['id'] | NewItemListLock['id']>;
  userUuid: FormControl<ItemListLockFormRawValue['userUuid']>;
  quantity: FormControl<ItemListLockFormRawValue['quantity']>;
  lockTime: FormControl<ItemListLockFormRawValue['lockTime']>;
  tire: FormControl<ItemListLockFormRawValue['tire']>;
};

export type ItemListLockFormGroup = FormGroup<ItemListLockFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ItemListLockFormService {
  createItemListLockFormGroup(itemListLock: ItemListLockFormGroupInput = { id: null }): ItemListLockFormGroup {
    const itemListLockRawValue = this.convertItemListLockToItemListLockRawValue({
      ...this.getFormDefaults(),
      ...itemListLock,
    });
    return new FormGroup<ItemListLockFormGroupContent>({
      id: new FormControl(
        { value: itemListLockRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      userUuid: new FormControl(itemListLockRawValue.userUuid, {
        validators: [Validators.required],
      }),
      quantity: new FormControl(itemListLockRawValue.quantity, {
        validators: [Validators.required],
      }),
      lockTime: new FormControl(itemListLockRawValue.lockTime, {
        validators: [Validators.required],
      }),
      tire: new FormControl(itemListLockRawValue.tire),
    });
  }

  getItemListLock(form: ItemListLockFormGroup): IItemListLock | NewItemListLock {
    return this.convertItemListLockRawValueToItemListLock(form.getRawValue() as ItemListLockFormRawValue | NewItemListLockFormRawValue);
  }

  resetForm(form: ItemListLockFormGroup, itemListLock: ItemListLockFormGroupInput): void {
    const itemListLockRawValue = this.convertItemListLockToItemListLockRawValue({ ...this.getFormDefaults(), ...itemListLock });
    form.reset(
      {
        ...itemListLockRawValue,
        id: { value: itemListLockRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ItemListLockFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      lockTime: currentTime,
    };
  }

  private convertItemListLockRawValueToItemListLock(
    rawItemListLock: ItemListLockFormRawValue | NewItemListLockFormRawValue,
  ): IItemListLock | NewItemListLock {
    return {
      ...rawItemListLock,
      lockTime: dayjs(rawItemListLock.lockTime, DATE_TIME_FORMAT),
    };
  }

  private convertItemListLockToItemListLockRawValue(
    itemListLock: IItemListLock | (Partial<NewItemListLock> & ItemListLockFormDefaults),
  ): ItemListLockFormRawValue | PartialWithRequiredKeyOf<NewItemListLockFormRawValue> {
    return {
      ...itemListLock,
      lockTime: itemListLock.lockTime ? itemListLock.lockTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
