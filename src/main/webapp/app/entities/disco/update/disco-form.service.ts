import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDisco, NewDisco } from '../disco.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDisco for edit and NewDiscoFormGroupInput for create.
 */
type DiscoFormGroupInput = IDisco | PartialWithRequiredKeyOf<NewDisco>;

type DiscoFormDefaults = Pick<NewDisco, 'id'>;

type DiscoFormGroupContent = {
  id: FormControl<IDisco['id'] | NewDisco['id']>;
  tipoMidia: FormControl<IDisco['tipoMidia']>;
  duracaoMinutos: FormControl<IDisco['duracaoMinutos']>;
};

export type DiscoFormGroup = FormGroup<DiscoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DiscoFormService {
  createDiscoFormGroup(disco: DiscoFormGroupInput = { id: null }): DiscoFormGroup {
    const discoRawValue = {
      ...this.getFormDefaults(),
      ...disco,
    };
    return new FormGroup<DiscoFormGroupContent>({
      id: new FormControl(
        { value: discoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      tipoMidia: new FormControl(discoRawValue.tipoMidia, {
        validators: [Validators.required],
      }),
      duracaoMinutos: new FormControl(discoRawValue.duracaoMinutos),
    });
  }

  getDisco(form: DiscoFormGroup): IDisco | NewDisco {
    return form.getRawValue() as IDisco | NewDisco;
  }

  resetForm(form: DiscoFormGroup, disco: DiscoFormGroupInput): void {
    const discoRawValue = { ...this.getFormDefaults(), ...disco };
    form.reset(
      {
        ...discoRawValue,
        id: { value: discoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DiscoFormDefaults {
    return {
      id: null,
    };
  }
}
