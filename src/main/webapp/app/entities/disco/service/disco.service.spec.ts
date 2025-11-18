import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDisco } from '../disco.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../disco.test-samples';

import { DiscoService } from './disco.service';

const requireRestSample: IDisco = {
  ...sampleWithRequiredData,
};

describe('Disco Service', () => {
  let service: DiscoService;
  let httpMock: HttpTestingController;
  let expectedResult: IDisco | IDisco[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DiscoService);
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

    it('should create a Disco', () => {
      const disco = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(disco).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Disco', () => {
      const disco = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(disco).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Disco', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Disco', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Disco', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDiscoToCollectionIfMissing', () => {
      it('should add a Disco to an empty array', () => {
        const disco: IDisco = sampleWithRequiredData;
        expectedResult = service.addDiscoToCollectionIfMissing([], disco);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(disco);
      });

      it('should not add a Disco to an array that contains it', () => {
        const disco: IDisco = sampleWithRequiredData;
        const discoCollection: IDisco[] = [
          {
            ...disco,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDiscoToCollectionIfMissing(discoCollection, disco);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Disco to an array that doesn't contain it", () => {
        const disco: IDisco = sampleWithRequiredData;
        const discoCollection: IDisco[] = [sampleWithPartialData];
        expectedResult = service.addDiscoToCollectionIfMissing(discoCollection, disco);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(disco);
      });

      it('should add only unique Disco to an array', () => {
        const discoArray: IDisco[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const discoCollection: IDisco[] = [sampleWithRequiredData];
        expectedResult = service.addDiscoToCollectionIfMissing(discoCollection, ...discoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const disco: IDisco = sampleWithRequiredData;
        const disco2: IDisco = sampleWithPartialData;
        expectedResult = service.addDiscoToCollectionIfMissing([], disco, disco2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(disco);
        expect(expectedResult).toContain(disco2);
      });

      it('should accept null and undefined values', () => {
        const disco: IDisco = sampleWithRequiredData;
        expectedResult = service.addDiscoToCollectionIfMissing([], null, disco, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(disco);
      });

      it('should return initial array if no Disco is added', () => {
        const discoCollection: IDisco[] = [sampleWithRequiredData];
        expectedResult = service.addDiscoToCollectionIfMissing(discoCollection, undefined, null);
        expect(expectedResult).toEqual(discoCollection);
      });
    });

    describe('compareDisco', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDisco(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 9789 };
        const entity2 = null;

        const compareResult1 = service.compareDisco(entity1, entity2);
        const compareResult2 = service.compareDisco(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 9789 };
        const entity2 = { id: 3929 };

        const compareResult1 = service.compareDisco(entity1, entity2);
        const compareResult2 = service.compareDisco(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 9789 };
        const entity2 = { id: 9789 };

        const compareResult1 = service.compareDisco(entity1, entity2);
        const compareResult2 = service.compareDisco(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
