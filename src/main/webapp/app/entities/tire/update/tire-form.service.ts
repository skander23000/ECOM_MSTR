import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITire, NewTire } from '../tire.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITire for edit and NewTireFormGroupInput for create.
 */
type TireFormGroupInput = ITire | PartialWithRequiredKeyOf<NewTire>;

type TireFormDefaults = Pick<NewTire, 'id' | 'disable'>;

type TireFormGroupContent = {
  id: FormControl<ITire['id'] | NewTire['id']>;
  reference: FormControl<ITire['reference']>;
  name: FormControl<ITire['name']>;
  price: FormControl<ITire['price']>;
  tireWidth: FormControl<ITire['tireWidth']>;
  tireHeight: FormControl<ITire['tireHeight']>;
  tireDiameter: FormControl<ITire['tireDiameter']>;
  tireType: FormControl<ITire['tireType']>;
  imageUrl: FormControl<ITire['imageUrl']>;
  speedIndex: FormControl<ITire['speedIndex']>;
  weightIndex: FormControl<ITire['weightIndex']>;
  quantity: FormControl<ITire['quantity']>;
  disable: FormControl<ITire['disable']>;
  disableReason: FormControl<ITire['disableReason']>;
  description: FormControl<ITire['description']>;
  tireBrand: FormControl<ITire['tireBrand']>;
};

export type TireFormGroup = FormGroup<TireFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TireFormService {
  createTireFormGroup(tire: TireFormGroupInput = { id: null }): TireFormGroup {
    const tireRawValue = {
      ...this.getFormDefaults(),
      ...tire,
    };
    return new FormGroup<TireFormGroupContent>({
      id: new FormControl(
        { value: tireRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      reference: new FormControl(tireRawValue.reference),
      name: new FormControl(tireRawValue.name, {
        validators: [Validators.required],
      }),
      price: new FormControl(tireRawValue.price, {
        validators: [Validators.required],
      }),
      tireWidth: new FormControl(tireRawValue.tireWidth, {
        validators: [Validators.required],
      }),
      tireHeight: new FormControl(tireRawValue.tireHeight, {
        validators: [Validators.required],
      }),
      tireDiameter: new FormControl(tireRawValue.tireDiameter, {
        validators: [Validators.required],
      }),
      tireType: new FormControl(tireRawValue.tireType, {
        validators: [Validators.required],
      }),
      imageUrl: new FormControl(tireRawValue.imageUrl, {
        validators: [Validators.required],
      }),
      speedIndex: new FormControl(tireRawValue.speedIndex, {
        validators: [Validators.required],
      }),
      weightIndex: new FormControl(tireRawValue.weightIndex, {
        validators: [Validators.required],
      }),
      quantity: new FormControl(tireRawValue.quantity, {
        validators: [Validators.required],
      }),
      disable: new FormControl(tireRawValue.disable, {
        validators: [Validators.required],
      }),
      disableReason: new FormControl(tireRawValue.disableReason),
      description: new FormControl(tireRawValue.description),
      tireBrand: new FormControl(tireRawValue.tireBrand),
    });
  }

  getTire(form: TireFormGroup): ITire | NewTire {
    return form.getRawValue() as ITire | NewTire;
  }

  resetForm(form: TireFormGroup, tire: TireFormGroupInput): void {
    const tireRawValue = { ...this.getFormDefaults(), ...tire };
    form.reset(
      {
        ...tireRawValue,
        id: { value: tireRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TireFormDefaults {
    return {
      id: null,
      disable: false,
    };
  }
}
