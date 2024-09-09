import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IItemListLock } from '../item-list-lock.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../item-list-lock.test-samples';

import { ItemListLockService, RestItemListLock } from './item-list-lock.service';

const requireRestSample: RestItemListLock = {
  ...sampleWithRequiredData,
  lockTime: sampleWithRequiredData.lockTime?.toJSON(),
};

describe('ItemListLock Service', () => {
  let service: ItemListLockService;
  let httpMock: HttpTestingController;
  let expectedResult: IItemListLock | IItemListLock[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ItemListLockService);
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

    it('should create a ItemListLock', () => {
      const itemListLock = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(itemListLock).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ItemListLock', () => {
      const itemListLock = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(itemListLock).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ItemListLock', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ItemListLock', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ItemListLock', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addItemListLockToCollectionIfMissing', () => {
      it('should add a ItemListLock to an empty array', () => {
        const itemListLock: IItemListLock = sampleWithRequiredData;
        expectedResult = service.addItemListLockToCollectionIfMissing([], itemListLock);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(itemListLock);
      });

      it('should not add a ItemListLock to an array that contains it', () => {
        const itemListLock: IItemListLock = sampleWithRequiredData;
        const itemListLockCollection: IItemListLock[] = [
          {
            ...itemListLock,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addItemListLockToCollectionIfMissing(itemListLockCollection, itemListLock);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ItemListLock to an array that doesn't contain it", () => {
        const itemListLock: IItemListLock = sampleWithRequiredData;
        const itemListLockCollection: IItemListLock[] = [sampleWithPartialData];
        expectedResult = service.addItemListLockToCollectionIfMissing(itemListLockCollection, itemListLock);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(itemListLock);
      });

      it('should add only unique ItemListLock to an array', () => {
        const itemListLockArray: IItemListLock[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const itemListLockCollection: IItemListLock[] = [sampleWithRequiredData];
        expectedResult = service.addItemListLockToCollectionIfMissing(itemListLockCollection, ...itemListLockArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const itemListLock: IItemListLock = sampleWithRequiredData;
        const itemListLock2: IItemListLock = sampleWithPartialData;
        expectedResult = service.addItemListLockToCollectionIfMissing([], itemListLock, itemListLock2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(itemListLock);
        expect(expectedResult).toContain(itemListLock2);
      });

      it('should accept null and undefined values', () => {
        const itemListLock: IItemListLock = sampleWithRequiredData;
        expectedResult = service.addItemListLockToCollectionIfMissing([], null, itemListLock, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(itemListLock);
      });

      it('should return initial array if no ItemListLock is added', () => {
        const itemListLockCollection: IItemListLock[] = [sampleWithRequiredData];
        expectedResult = service.addItemListLockToCollectionIfMissing(itemListLockCollection, undefined, null);
        expect(expectedResult).toEqual(itemListLockCollection);
      });
    });

    describe('compareItemListLock', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareItemListLock(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareItemListLock(entity1, entity2);
        const compareResult2 = service.compareItemListLock(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareItemListLock(entity1, entity2);
        const compareResult2 = service.compareItemListLock(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareItemListLock(entity1, entity2);
        const compareResult2 = service.compareItemListLock(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
