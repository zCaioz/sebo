import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IVenda } from '../venda.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../venda.test-samples';

import { RestVenda, VendaService } from './venda.service';

const requireRestSample: RestVenda = {
  ...sampleWithRequiredData,
  dataVenda: sampleWithRequiredData.dataVenda?.format(DATE_FORMAT),
};

describe('Venda Service', () => {
  let service: VendaService;
  let httpMock: HttpTestingController;
  let expectedResult: IVenda | IVenda[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(VendaService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Venda', () => {
      const venda = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(venda).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Venda', () => {
      const venda = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(venda).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Venda', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Venda', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Venda', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVendaToCollectionIfMissing', () => {
      it('should add a Venda to an empty array', () => {
        const venda: IVenda = sampleWithRequiredData;
        expectedResult = service.addVendaToCollectionIfMissing([], venda);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(venda);
      });

      it('should not add a Venda to an array that contains it', () => {
        const venda: IVenda = sampleWithRequiredData;
        const vendaCollection: IVenda[] = [
          {
            ...venda,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVendaToCollectionIfMissing(vendaCollection, venda);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Venda to an array that doesn't contain it", () => {
        const venda: IVenda = sampleWithRequiredData;
        const vendaCollection: IVenda[] = [sampleWithPartialData];
        expectedResult = service.addVendaToCollectionIfMissing(vendaCollection, venda);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(venda);
      });

      it('should add only unique Venda to an array', () => {
        const vendaArray: IVenda[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const vendaCollection: IVenda[] = [sampleWithRequiredData];
        expectedResult = service.addVendaToCollectionIfMissing(vendaCollection, ...vendaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const venda: IVenda = sampleWithRequiredData;
        const venda2: IVenda = sampleWithPartialData;
        expectedResult = service.addVendaToCollectionIfMissing([], venda, venda2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(venda);
        expect(expectedResult).toContain(venda2);
      });

      it('should accept null and undefined values', () => {
        const venda: IVenda = sampleWithRequiredData;
        expectedResult = service.addVendaToCollectionIfMissing([], null, venda, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(venda);
      });

      it('should return initial array if no Venda is added', () => {
        const vendaCollection: IVenda[] = [sampleWithRequiredData];
        expectedResult = service.addVendaToCollectionIfMissing(vendaCollection, undefined, null);
        expect(expectedResult).toEqual(vendaCollection);
      });
    });

    describe('compareVenda', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVenda(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 31753 };
        const entity2 = null;

        const compareResult1 = service.compareVenda(entity1, entity2);
        const compareResult2 = service.compareVenda(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 31753 };
        const entity2 = { id: 27942 };

        const compareResult1 = service.compareVenda(entity1, entity2);
        const compareResult2 = service.compareVenda(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 31753 };
        const entity2 = { id: 31753 };

        const compareResult1 = service.compareVenda(entity1, entity2);
        const compareResult2 = service.compareVenda(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
