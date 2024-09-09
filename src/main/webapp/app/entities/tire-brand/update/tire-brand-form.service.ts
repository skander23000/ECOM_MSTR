import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITireBrand, NewTireBrand } from '../tire-brand.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITireBrand for edit and NewTireBrandFormGroupInput for create.
 */
type TireBrandFormGroupInput = ITireBrand | PartialWithRequiredKeyOf<NewTireBrand>;

type TireBrandFormDefaults = Pick<NewTireBrand, 'id'>;

type TireBrandFormGroupContent = {
  id: FormControl<ITireBrand['id'] | NewTireBrand['id']>;
  name: FormControl<ITireBrand['name']>;
  logoUrl: FormControl<ITireBrand['logoUrl']>;
};

export type TireBrandFormGroup = FormGroup<TireBrandFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TireBrandFormService {
  createTireBrandFormGroup(tireBrand: TireBrandFormGroupInput = { id: null }): TireBrandFormGroup {
    const tireBrandRawValue = {
      ...this.getFormDefaults(),
      ...tireBrand,
    };
    return new FormGroup<TireBrandFormGroupContent>({
      id: new FormControl(
        { value: tireBrandRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(tireBrandRawValue.name, {
        validators: [Validators.required],
      }),
      logoUrl: new FormControl(tireBrandRawValue.logoUrl, {
        validators: [Validators.required],
      }),
    });
  }

  getTireBrand(form: TireBrandFormGroup): ITireBrand | NewTireBrand {
    return form.getRawValue() as ITireBrand | NewTireBrand;
  }

  resetForm(form: TireBrandFormGroup, tireBrand: TireBrandFormGroupInput): void {
    const tireBrandRawValue = { ...this.getFormDefaults(), ...tireBrand };
    form.reset(
      {
        ...tireBrandRawValue,
        id: { value: tireBrandRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TireBrandFormDefaults {
    return {
      id: null,
    };
  }
}
