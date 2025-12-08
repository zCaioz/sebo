import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IEmprestimo } from 'app/entities/emprestimo/emprestimo.model';
import { EmprestimoService } from 'app/entities/emprestimo/service/emprestimo.service';
import { IVenda } from 'app/entities/venda/venda.model';
import { VendaService } from 'app/entities/venda/service/venda.service';
import { IItem } from '../item.model';
import { ItemService } from '../service/item.service';
import { ItemFormService } from './item-form.service';

import { ItemUpdateComponent } from './item-update.component';

describe('Item Management Update Component', () => {
  let comp: ItemUpdateComponent;
  let fixture: ComponentFixture<ItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let itemFormService: ItemFormService;
  let itemService: ItemService;
  let emprestimoService: EmprestimoService;
  let vendaService: VendaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ItemUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    itemFormService = TestBed.inject(ItemFormService);
    itemService = TestBed.inject(ItemService);
    emprestimoService = TestBed.inject(EmprestimoService);
    vendaService = TestBed.inject(VendaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Emprestimo query and add missing value', () => {
      const item: IItem = { id: 13382 };
      const emprestimos: IEmprestimo[] = [{ id: 30317 }];
      item.emprestimos = emprestimos;

      const emprestimoCollection: IEmprestimo[] = [{ id: 30317 }];
      jest.spyOn(emprestimoService, 'query').mockReturnValue(of(new HttpResponse({ body: emprestimoCollection })));
      const additionalEmprestimos = [...emprestimos];
      const expectedCollection: IEmprestimo[] = [...additionalEmprestimos, ...emprestimoCollection];
      jest.spyOn(emprestimoService, 'addEmprestimoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ item });
      comp.ngOnInit();

      expect(emprestimoService.query).toHaveBeenCalled();
      expect(emprestimoService.addEmprestimoToCollectionIfMissing).toHaveBeenCalledWith(
        emprestimoCollection,
        ...additionalEmprestimos.map(expect.objectContaining),
      );
      expect(comp.emprestimosSharedCollection).toEqual(expectedCollection);
    });

    it('should call Venda query and add missing value', () => {
      const item: IItem = { id: 13382 };
      const vendas: IVenda[] = [{ id: 31753 }];
      item.vendas = vendas;

      const vendaCollection: IVenda[] = [{ id: 31753 }];
      jest.spyOn(vendaService, 'query').mockReturnValue(of(new HttpResponse({ body: vendaCollection })));
      const additionalVendas = [...vendas];
      const expectedCollection: IVenda[] = [...additionalVendas, ...vendaCollection];
      jest.spyOn(vendaService, 'addVendaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ item });
      comp.ngOnInit();

      expect(vendaService.query).toHaveBeenCalled();
      expect(vendaService.addVendaToCollectionIfMissing).toHaveBeenCalledWith(
        vendaCollection,
        ...additionalVendas.map(expect.objectContaining),
      );
      expect(comp.vendasSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const item: IItem = { id: 13382 };
      const emprestimo: IEmprestimo = { id: 30317 };
      item.emprestimos = [emprestimo];
      const venda: IVenda = { id: 31753 };
      item.vendas = [venda];

      activatedRoute.data = of({ item });
      comp.ngOnInit();

      expect(comp.emprestimosSharedCollection).toContainEqual(emprestimo);
      expect(comp.vendasSharedCollection).toContainEqual(venda);
      expect(comp.item).toEqual(item);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItem>>();
      const item = { id: 10228 };
      jest.spyOn(itemFormService, 'getItem').mockReturnValue(item);
      jest.spyOn(itemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ item });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: item }));
      saveSubject.complete();

      // THEN
      expect(itemFormService.getItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(itemService.update).toHaveBeenCalledWith(expect.objectContaining(item));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItem>>();
      const item = { id: 10228 };
      jest.spyOn(itemFormService, 'getItem').mockReturnValue({ id: null });
      jest.spyOn(itemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ item: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: item }));
      saveSubject.complete();

      // THEN
      expect(itemFormService.getItem).toHaveBeenCalled();
      expect(itemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItem>>();
      const item = { id: 10228 };
      jest.spyOn(itemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ item });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(itemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEmprestimo', () => {
      it('should forward to emprestimoService', () => {
        const entity = { id: 30317 };
        const entity2 = { id: 1225 };
        jest.spyOn(emprestimoService, 'compareEmprestimo');
        comp.compareEmprestimo(entity, entity2);
        expect(emprestimoService.compareEmprestimo).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareVenda', () => {
      it('should forward to vendaService', () => {
        const entity = { id: 31753 };
        const entity2 = { id: 27942 };
        jest.spyOn(vendaService, 'compareVenda');
        comp.compareVenda(entity, entity2);
        expect(vendaService.compareVenda).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
