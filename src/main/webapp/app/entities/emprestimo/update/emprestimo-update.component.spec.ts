import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';
import { IEmprestimo } from '../emprestimo.model';
import { EmprestimoService } from '../service/emprestimo.service';
import { EmprestimoFormService } from './emprestimo-form.service';

import { EmprestimoUpdateComponent } from './emprestimo-update.component';

describe('Emprestimo Management Update Component', () => {
  let comp: EmprestimoUpdateComponent;
  let fixture: ComponentFixture<EmprestimoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let emprestimoFormService: EmprestimoFormService;
  let emprestimoService: EmprestimoService;
  let usuarioService: UsuarioService;
  let itemService: ItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EmprestimoUpdateComponent],
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
      .overrideTemplate(EmprestimoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmprestimoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    emprestimoFormService = TestBed.inject(EmprestimoFormService);
    emprestimoService = TestBed.inject(EmprestimoService);
    usuarioService = TestBed.inject(UsuarioService);
    itemService = TestBed.inject(ItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Usuario query and add missing value', () => {
      const emprestimo: IEmprestimo = { id: 1225 };
      const usuario: IUsuario = { id: 544 };
      emprestimo.usuario = usuario;

      const usuarioCollection: IUsuario[] = [{ id: 544 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [usuario];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ emprestimo });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(
        usuarioCollection,
        ...additionalUsuarios.map(expect.objectContaining),
      );
      expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
    });

    it('should call Item query and add missing value', () => {
      const emprestimo: IEmprestimo = { id: 1225 };
      const itens: IItem[] = [{ id: 10228 }];
      emprestimo.itens = itens;

      const itemCollection: IItem[] = [{ id: 10228 }];
      jest.spyOn(itemService, 'query').mockReturnValue(of(new HttpResponse({ body: itemCollection })));
      const additionalItems = [...itens];
      const expectedCollection: IItem[] = [...additionalItems, ...itemCollection];
      jest.spyOn(itemService, 'addItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ emprestimo });
      comp.ngOnInit();

      expect(itemService.query).toHaveBeenCalled();
      expect(itemService.addItemToCollectionIfMissing).toHaveBeenCalledWith(
        itemCollection,
        ...additionalItems.map(expect.objectContaining),
      );
      expect(comp.itemsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const emprestimo: IEmprestimo = { id: 1225 };
      const usuario: IUsuario = { id: 544 };
      emprestimo.usuario = usuario;
      const itens: IItem = { id: 10228 };
      emprestimo.itens = [itens];

      activatedRoute.data = of({ emprestimo });
      comp.ngOnInit();

      expect(comp.usuariosSharedCollection).toContainEqual(usuario);
      expect(comp.itemsSharedCollection).toContainEqual(itens);
      expect(comp.emprestimo).toEqual(emprestimo);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmprestimo>>();
      const emprestimo = { id: 30317 };
      jest.spyOn(emprestimoFormService, 'getEmprestimo').mockReturnValue(emprestimo);
      jest.spyOn(emprestimoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ emprestimo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: emprestimo }));
      saveSubject.complete();

      // THEN
      expect(emprestimoFormService.getEmprestimo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(emprestimoService.update).toHaveBeenCalledWith(expect.objectContaining(emprestimo));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmprestimo>>();
      const emprestimo = { id: 30317 };
      jest.spyOn(emprestimoFormService, 'getEmprestimo').mockReturnValue({ id: null });
      jest.spyOn(emprestimoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ emprestimo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: emprestimo }));
      saveSubject.complete();

      // THEN
      expect(emprestimoFormService.getEmprestimo).toHaveBeenCalled();
      expect(emprestimoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmprestimo>>();
      const emprestimo = { id: 30317 };
      jest.spyOn(emprestimoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ emprestimo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(emprestimoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUsuario', () => {
      it('should forward to usuarioService', () => {
        const entity = { id: 544 };
        const entity2 = { id: 13162 };
        jest.spyOn(usuarioService, 'compareUsuario');
        comp.compareUsuario(entity, entity2);
        expect(usuarioService.compareUsuario).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareItem', () => {
      it('should forward to itemService', () => {
        const entity = { id: 10228 };
        const entity2 = { id: 13382 };
        jest.spyOn(itemService, 'compareItem');
        comp.compareItem(entity, entity2);
        expect(itemService.compareItem).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
