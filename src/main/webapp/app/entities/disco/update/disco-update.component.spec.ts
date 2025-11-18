import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { DiscoService } from '../service/disco.service';
import { IDisco } from '../disco.model';
import { DiscoFormService } from './disco-form.service';

import { DiscoUpdateComponent } from './disco-update.component';

describe('Disco Management Update Component', () => {
  let comp: DiscoUpdateComponent;
  let fixture: ComponentFixture<DiscoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let discoFormService: DiscoFormService;
  let discoService: DiscoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DiscoUpdateComponent],
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
      .overrideTemplate(DiscoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DiscoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    discoFormService = TestBed.inject(DiscoFormService);
    discoService = TestBed.inject(DiscoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const disco: IDisco = { id: 3929 };

      activatedRoute.data = of({ disco });
      comp.ngOnInit();

      expect(comp.disco).toEqual(disco);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDisco>>();
      const disco = { id: 9789 };
      jest.spyOn(discoFormService, 'getDisco').mockReturnValue(disco);
      jest.spyOn(discoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ disco });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: disco }));
      saveSubject.complete();

      // THEN
      expect(discoFormService.getDisco).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(discoService.update).toHaveBeenCalledWith(expect.objectContaining(disco));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDisco>>();
      const disco = { id: 9789 };
      jest.spyOn(discoFormService, 'getDisco').mockReturnValue({ id: null });
      jest.spyOn(discoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ disco: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: disco }));
      saveSubject.complete();

      // THEN
      expect(discoFormService.getDisco).toHaveBeenCalled();
      expect(discoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDisco>>();
      const disco = { id: 9789 };
      jest.spyOn(discoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ disco });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(discoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
