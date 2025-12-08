import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmprestimo } from 'app/entities/emprestimo/emprestimo.model';
import { EmprestimoService } from 'app/entities/emprestimo/service/emprestimo.service';
import { IVenda } from 'app/entities/venda/venda.model';
import { VendaService } from 'app/entities/venda/service/venda.service';
import { ItemService } from '../service/item.service';
import { IItem } from '../item.model';
import { ItemFormGroup, ItemFormService } from './item-form.service';

@Component({
  selector: 'jhi-item-update',
  templateUrl: './item-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ItemUpdateComponent implements OnInit {
  isSaving = false;
  item: IItem | null = null;

  emprestimosSharedCollection: IEmprestimo[] = [];
  vendasSharedCollection: IVenda[] = [];

  protected itemService = inject(ItemService);
  protected itemFormService = inject(ItemFormService);
  protected emprestimoService = inject(EmprestimoService);
  protected vendaService = inject(VendaService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ItemFormGroup = this.itemFormService.createItemFormGroup();

  compareEmprestimo = (o1: IEmprestimo | null, o2: IEmprestimo | null): boolean => this.emprestimoService.compareEmprestimo(o1, o2);

  compareVenda = (o1: IVenda | null, o2: IVenda | null): boolean => this.vendaService.compareVenda(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ item }) => {
      this.item = item;
      if (item) {
        this.updateForm(item);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const item = this.itemFormService.getItem(this.editForm);
    if (item.id !== null) {
      this.subscribeToSaveResponse(this.itemService.update(item));
    } else {
      this.subscribeToSaveResponse(this.itemService.create(item));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItem>>): void {
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

  protected updateForm(item: IItem): void {
    this.item = item;
    this.itemFormService.resetForm(this.editForm, item);

    this.emprestimosSharedCollection = this.emprestimoService.addEmprestimoToCollectionIfMissing<IEmprestimo>(
      this.emprestimosSharedCollection,
      ...(item.emprestimos ?? []),
    );
    this.vendasSharedCollection = this.vendaService.addVendaToCollectionIfMissing<IVenda>(
      this.vendasSharedCollection,
      ...(item.vendas ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.emprestimoService
      .query()
      .pipe(map((res: HttpResponse<IEmprestimo[]>) => res.body ?? []))
      .pipe(
        map((emprestimos: IEmprestimo[]) =>
          this.emprestimoService.addEmprestimoToCollectionIfMissing<IEmprestimo>(emprestimos, ...(this.item?.emprestimos ?? [])),
        ),
      )
      .subscribe((emprestimos: IEmprestimo[]) => (this.emprestimosSharedCollection = emprestimos));

    this.vendaService
      .query()
      .pipe(map((res: HttpResponse<IVenda[]>) => res.body ?? []))
      .pipe(map((vendas: IVenda[]) => this.vendaService.addVendaToCollectionIfMissing<IVenda>(vendas, ...(this.item?.vendas ?? []))))
      .subscribe((vendas: IVenda[]) => (this.vendasSharedCollection = vendas));
  }
}
