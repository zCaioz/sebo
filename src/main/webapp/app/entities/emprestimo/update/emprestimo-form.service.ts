import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IEmprestimo, NewEmprestimo } from '../emprestimo.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmprestimo for edit and NewEmprestimoFormGroupInput for create.
 */
type EmprestimoFormGroupInput = IEmprestimo | PartialWithRequiredKeyOf<NewEmprestimo>;

type EmprestimoFormDefaults = Pick<NewEmprestimo, 'id' | 'itens'>;

type EmprestimoFormGroupContent = {
  id: FormControl<IEmprestimo['id'] | NewEmprestimo['id']>;
  dataEmprestimo: FormControl<IEmprestimo['dataEmprestimo']>;
  dataPrevistaDevolucao: FormControl<IEmprestimo['dataPrevistaDevolucao']>;
  dataDevolucao: FormControl<IEmprestimo['dataDevolucao']>;
  status: FormControl<IEmprestimo['status']>;
  usuario: FormControl<IEmprestimo['usuario']>;
  itens: FormControl<IEmprestimo['itens']>;
};

export type EmprestimoFormGroup = FormGroup<EmprestimoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmprestimoFormService {
  createEmprestimoFormGroup(emprestimo: EmprestimoFormGroupInput = { id: null }): EmprestimoFormGroup {
    const emprestimoRawValue = {
      ...this.getFormDefaults(),
      ...emprestimo,
    };
    return new FormGroup<EmprestimoFormGroupContent>({
      id: new FormControl(
        { value: emprestimoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dataEmprestimo: new FormControl(emprestimoRawValue.dataEmprestimo, {
        validators: [Validators.required],
      }),
      dataPrevistaDevolucao: new FormControl(emprestimoRawValue.dataPrevistaDevolucao, {
        validators: [Validators.required],
      }),
      dataDevolucao: new FormControl(emprestimoRawValue.dataDevolucao),
      status: new FormControl(emprestimoRawValue.status, {
        validators: [Validators.required],
      }),
      usuario: new FormControl(emprestimoRawValue.usuario, { validators: [Validators.required] }),
      itens: new FormControl(emprestimoRawValue.itens ?? []),
    });
  }

  getEmprestimo(form: EmprestimoFormGroup): IEmprestimo | NewEmprestimo {
    return form.getRawValue() as IEmprestimo | NewEmprestimo;
  }

  resetForm(form: EmprestimoFormGroup, emprestimo: EmprestimoFormGroupInput): void {
    const emprestimoRawValue = { ...this.getFormDefaults(), ...emprestimo };
    form.reset(
      {
        ...emprestimoRawValue,
        id: { value: emprestimoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EmprestimoFormDefaults {
    return {
      id: null,
      itens: [],
    };
  }
}
