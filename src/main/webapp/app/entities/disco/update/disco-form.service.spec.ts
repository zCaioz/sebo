import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../disco.test-samples';

import { DiscoFormService } from './disco-form.service';

describe('Disco Form Service', () => {
  let service: DiscoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DiscoFormService);
  });

  describe('Service methods', () => {
    describe('createDiscoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDiscoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tipoMidia: expect.any(Object),
            duracaoMinutos: expect.any(Object),
          }),
        );
      });

      it('passing IDisco should create a new form with FormGroup', () => {
        const formGroup = service.createDiscoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            tipoMidia: expect.any(Object),
            duracaoMinutos: expect.any(Object),
          }),
        );
      });
    });

    describe('getDisco', () => {
      it('should return NewDisco for default Disco initial value', () => {
        const formGroup = service.createDiscoFormGroup(sampleWithNewData);

        const disco = service.getDisco(formGroup) as any;

        expect(disco).toMatchObject(sampleWithNewData);
      });

      it('should return NewDisco for empty Disco initial value', () => {
        const formGroup = service.createDiscoFormGroup();

        const disco = service.getDisco(formGroup) as any;

        expect(disco).toMatchObject({});
      });

      it('should return IDisco', () => {
        const formGroup = service.createDiscoFormGroup(sampleWithRequiredData);

        const disco = service.getDisco(formGroup) as any;

        expect(disco).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDisco should not enable id FormControl', () => {
        const formGroup = service.createDiscoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDisco should disable id FormControl', () => {
        const formGroup = service.createDiscoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
