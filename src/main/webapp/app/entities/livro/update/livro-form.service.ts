import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ILivro, NewLivro } from '../livro.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILivro for edit and NewLivroFormGroupInput for create.
 */
type LivroFormGroupInput = ILivro | PartialWithRequiredKeyOf<NewLivro>;

type LivroFormDefaults = Pick<NewLivro, 'id'>;

type LivroFormGroupContent = {
  id: FormControl<ILivro['id'] | NewLivro['id']>;
  isbn: FormControl<ILivro['isbn']>;
  editora: FormControl<ILivro['editora']>;
  numeroPaginas: FormControl<ILivro['numeroPaginas']>;
  titulo: FormControl<ILivro['titulo']>;
  ano: FormControl<ILivro['ano']>;
  genero: FormControl<ILivro['genero']>;
  autorArtista: FormControl<ILivro['autorArtista']>;
  disponibilidade: FormControl<ILivro['disponibilidade']>;
};

export type LivroFormGroup = FormGroup<LivroFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LivroFormService {
  createLivroFormGroup(livro: LivroFormGroupInput = { id: null }): LivroFormGroup {
    const livroRawValue = {
      ...this.getFormDefaults(),
      ...livro,
    };
    return new FormGroup<LivroFormGroupContent>({
      id: new FormControl(
        { value: livroRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      isbn: new FormControl(livroRawValue.isbn, {
        validators: [Validators.required],
      }),
      editora: new FormControl(livroRawValue.editora),
      numeroPaginas: new FormControl(livroRawValue.numeroPaginas),
      titulo: new FormControl(livroRawValue.titulo, { validators: [Validators.required] }),
      ano: new FormControl(livroRawValue.ano),
      genero: new FormControl(livroRawValue.genero),
      autorArtista: new FormControl(livroRawValue.autorArtista),
      disponibilidade: new FormControl(livroRawValue.disponibilidade ?? true, { validators: [Validators.required] }),
    });
  }

  getLivro(form: LivroFormGroup): ILivro | NewLivro {
    return form.getRawValue() as ILivro | NewLivro;
  }

  resetForm(form: LivroFormGroup, livro: LivroFormGroupInput): void {
    const livroRawValue = { ...this.getFormDefaults(), ...livro };
    form.reset(
      {
        ...livroRawValue,
        id: { value: livroRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LivroFormDefaults {
    return {
      id: null,
    };
  }
}
