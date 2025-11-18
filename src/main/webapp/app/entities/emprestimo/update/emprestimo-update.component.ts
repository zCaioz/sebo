import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { StatusEmprestimo } from 'app/entities/enumerations/status-emprestimo.model';
import { EmprestimoService } from '../service/emprestimo.service';
import { IEmprestimo } from '../emprestimo.model';
import { EmprestimoFormGroup, EmprestimoFormService } from './emprestimo-form.service';

@Component({
  selector: 'jhi-emprestimo-update',
  templateUrl: './emprestimo-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EmprestimoUpdateComponent implements OnInit {
  isSaving = false;
  emprestimo: IEmprestimo | null = null;
  statusEmprestimoValues = Object.keys(StatusEmprestimo);

  itemsSharedCollection: IItem[] = [];
  usuariosSharedCollection: IUsuario[] = [];

  protected emprestimoService = inject(EmprestimoService);
  protected emprestimoFormService = inject(EmprestimoFormService);
  protected itemService = inject(ItemService);
  protected usuarioService = inject(UsuarioService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EmprestimoFormGroup = this.emprestimoFormService.createEmprestimoFormGroup();

  compareItem = (o1: IItem | null, o2: IItem | null): boolean => this.itemService.compareItem(o1, o2);

  compareUsuario = (o1: IUsuario | null, o2: IUsuario | null): boolean => this.usuarioService.compareUsuario(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ emprestimo }) => {
      this.emprestimo = emprestimo;
      if (emprestimo) {
        this.updateForm(emprestimo);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const emprestimo = this.emprestimoFormService.getEmprestimo(this.editForm);
    if (emprestimo.id !== null) {
      this.subscribeToSaveResponse(this.emprestimoService.update(emprestimo));
    } else {
      this.subscribeToSaveResponse(this.emprestimoService.create(emprestimo));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmprestimo>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(emprestimo: IEmprestimo): void {
    this.emprestimo = emprestimo;
    this.emprestimoFormService.resetForm(this.editForm, emprestimo);

    this.itemsSharedCollection = this.itemService.addItemToCollectionIfMissing<IItem>(this.itemsSharedCollection, emprestimo.item);
    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing<IUsuario>(
      this.usuariosSharedCollection,
      emprestimo.usuario,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.itemService
      .query()
      .pipe(map((res: HttpResponse<IItem[]>) => res.body ?? []))
      .pipe(map((items: IItem[]) => this.itemService.addItemToCollectionIfMissing<IItem>(items, this.emprestimo?.item)))
      .subscribe((items: IItem[]) => (this.itemsSharedCollection = items));

    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) => this.usuarioService.addUsuarioToCollectionIfMissing<IUsuario>(usuarios, this.emprestimo?.usuario)),
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));
  }
}
