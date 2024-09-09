import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITireBrand } from '../tire-brand.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../tire-brand.test-samples';

import { TireBrandService } from './tire-brand.service';

const requireRestSample: ITireBrand = {
  ...sampleWithRequiredData,
};

describe('TireBrand Service', () => {
  let service: TireBrandService;
  let httpMock: HttpTestingController;
  let expectedResult: ITireBrand | ITireBrand[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TireBrandService);
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

    it('should create a TireBrand', () => {
      const tireBrand = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tireBrand).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TireBrand', () => {
      const tireBrand = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tireBrand).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TireBrand', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TireBrand', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TireBrand', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTireBrandToCollectionIfMissing', () => {
      it('should add a TireBrand to an empty array', () => {
        const tireBrand: ITireBrand = sampleWithRequiredData;
        expectedResult = service.addTireBrandToCollectionIfMissing([], tireBrand);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tireBrand);
      });

      it('should not add a TireBrand to an array that contains it', () => {
        const tireBrand: ITireBrand = sampleWithRequiredData;
        const tireBrandCollection: ITireBrand[] = [
          {
            ...tireBrand,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTireBrandToCollectionIfMissing(tireBrandCollection, tireBrand);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TireBrand to an array that doesn't contain it", () => {
        const tireBrand: ITireBrand = sampleWithRequiredData;
        const tireBrandCollection: ITireBrand[] = [sampleWithPartialData];
        expectedResult = service.addTireBrandToCollectionIfMissing(tireBrandCollection, tireBrand);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tireBrand);
      });

      it('should add only unique TireBrand to an array', () => {
        const tireBrandArray: ITireBrand[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tireBrandCollection: ITireBrand[] = [sampleWithRequiredData];
        expectedResult = service.addTireBrandToCollectionIfMissing(tireBrandCollection, ...tireBrandArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tireBrand: ITireBrand = sampleWithRequiredData;
        const tireBrand2: ITireBrand = sampleWithPartialData;
        expectedResult = service.addTireBrandToCollectionIfMissing([], tireBrand, tireBrand2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tireBrand);
        expect(expectedResult).toContain(tireBrand2);
      });

      it('should accept null and undefined values', () => {
        const tireBrand: ITireBrand = sampleWithRequiredData;
        expectedResult = service.addTireBrandToCollectionIfMissing([], null, tireBrand, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tireBrand);
      });

      it('should return initial array if no TireBrand is added', () => {
        const tireBrandCollection: ITireBrand[] = [sampleWithRequiredData];
        expectedResult = service.addTireBrandToCollectionIfMissing(tireBrandCollection, undefined, null);
        expect(expectedResult).toEqual(tireBrandCollection);
      });
    });

    describe('compareTireBrand', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTireBrand(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTireBrand(entity1, entity2);
        const compareResult2 = service.compareTireBrand(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTireBrand(entity1, entity2);
        const compareResult2 = service.compareTireBrand(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTireBrand(entity1, entity2);
        const compareResult2 = service.compareTireBrand(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
