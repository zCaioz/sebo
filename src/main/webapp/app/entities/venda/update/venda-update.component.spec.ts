import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { VendaService } from '../service/venda.service';
import { IVenda } from '../venda.model';
import { VendaFormService } from './venda-form.service';

import { VendaUpdateComponent } from './venda-update.component';

describe('Venda Management Update Component', () => {
  let comp: VendaUpdateComponent;
  let fixture: ComponentFixture<VendaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vendaFormService: VendaFormService;
  let vendaService: VendaService;
  let usuarioService: UsuarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [VendaUpdateComponent],
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
      .overrideTemplate(VendaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VendaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vendaFormService = TestBed.inject(VendaFormService);
    vendaService = TestBed.inject(VendaService);
    usuarioService = TestBed.inject(UsuarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Usuario query and add missing value', () => {
      const venda: IVenda = { id: 27942 };
      const usuario: IUsuario = { id: 544 };
      venda.usuario = usuario;

      const usuarioCollection: IUsuario[] = [{ id: 544 }];
      jest.spyOn(usuarioService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioCollection })));
      const additionalUsuarios = [usuario];
      const expectedCollection: IUsuario[] = [...additionalUsuarios, ...usuarioCollection];
      jest.spyOn(usuarioService, 'addUsuarioToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ venda });
      comp.ngOnInit();

      expect(usuarioService.query).toHaveBeenCalled();
      expect(usuarioService.addUsuarioToCollectionIfMissing).toHaveBeenCalledWith(
        usuarioCollection,
        ...additionalUsuarios.map(expect.objectContaining),
      );
      expect(comp.usuariosSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const venda: IVenda = { id: 27942 };
      const usuario: IUsuario = { id: 544 };
      venda.usuario = usuario;

      activatedRoute.data = of({ venda });
      comp.ngOnInit();

      expect(comp.usuariosSharedCollection).toContainEqual(usuario);
      expect(comp.venda).toEqual(venda);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVenda>>();
      const venda = { id: 31753 };
      jest.spyOn(vendaFormService, 'getVenda').mockReturnValue(venda);
      jest.spyOn(vendaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ venda });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: venda }));
      saveSubject.complete();

      // THEN
      expect(vendaFormService.getVenda).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(vendaService.update).toHaveBeenCalledWith(expect.objectContaining(venda));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVenda>>();
      const venda = { id: 31753 };
      jest.spyOn(vendaFormService, 'getVenda').mockReturnValue({ id: null });
      jest.spyOn(vendaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ venda: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: venda }));
      saveSubject.complete();

      // THEN
      expect(vendaFormService.getVenda).toHaveBeenCalled();
      expect(vendaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVenda>>();
      const venda = { id: 31753 };
      jest.spyOn(vendaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ venda });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vendaService.update).toHaveBeenCalled();
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
  });
});
