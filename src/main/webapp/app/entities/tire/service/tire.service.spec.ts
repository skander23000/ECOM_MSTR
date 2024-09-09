import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITire } from '../tire.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../tire.test-samples';

import { TireService } from './tire.service';

const requireRestSample: ITire = {
  ...sampleWithRequiredData,
};

describe('Tire Service', () => {
  let service: TireService;
  let httpMock: HttpTestingController;
  let expectedResult: ITire | ITire[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TireService);
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

    it('should create a Tire', () => {
      const tire = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tire).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Tire', () => {
      const tire = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tire).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Tire', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Tire', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Tire', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTireToCollectionIfMissing', () => {
      it('should add a Tire to an empty array', () => {
        const tire: ITire = sampleWithRequiredData;
        expectedResult = service.addTireToCollectionIfMissing([], tire);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tire);
      });

      it('should not add a Tire to an array that contains it', () => {
        const tire: ITire = sampleWithRequiredData;
        const tireCollection: ITire[] = [
          {
            ...tire,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTireToCollectionIfMissing(tireCollection, tire);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Tire to an array that doesn't contain it", () => {
        const tire: ITire = sampleWithRequiredData;
        const tireCollection: ITire[] = [sampleWithPartialData];
        expectedResult = service.addTireToCollectionIfMissing(tireCollection, tire);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tire);
      });

      it('should add only unique Tire to an array', () => {
        const tireArray: ITire[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tireCollection: ITire[] = [sampleWithRequiredData];
        expectedResult = service.addTireToCollectionIfMissing(tireCollection, ...tireArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tire: ITire = sampleWithRequiredData;
        const tire2: ITire = sampleWithPartialData;
        expectedResult = service.addTireToCollectionIfMissing([], tire, tire2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tire);
        expect(expectedResult).toContain(tire2);
      });

      it('should accept null and undefined values', () => {
        const tire: ITire = sampleWithRequiredData;
        expectedResult = service.addTireToCollectionIfMissing([], null, tire, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tire);
      });

      it('should return initial array if no Tire is added', () => {
        const tireCollection: ITire[] = [sampleWithRequiredData];
        expectedResult = service.addTireToCollectionIfMissing(tireCollection, undefined, null);
        expect(expectedResult).toEqual(tireCollection);
      });
    });

    describe('compareTire', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTire(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTire(entity1, entity2);
        const compareResult2 = service.compareTire(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTire(entity1, entity2);
        const compareResult2 = service.compareTire(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTire(entity1, entity2);
        const compareResult2 = service.compareTire(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
