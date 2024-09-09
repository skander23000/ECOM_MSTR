import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ITire } from 'app/entities/tire/tire.model';
import { TireService } from 'app/entities/tire/service/tire.service';
import { ItemListLockService } from '../service/item-list-lock.service';
import { IItemListLock } from '../item-list-lock.model';
import { ItemListLockFormService } from './item-list-lock-form.service';

import { ItemListLockUpdateComponent } from './item-list-lock-update.component';

describe('ItemListLock Management Update Component', () => {
  let comp: ItemListLockUpdateComponent;
  let fixture: ComponentFixture<ItemListLockUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let itemListLockFormService: ItemListLockFormService;
  let itemListLockService: ItemListLockService;
  let tireService: TireService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ItemListLockUpdateComponent],
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
      .overrideTemplate(ItemListLockUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ItemListLockUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    itemListLockFormService = TestBed.inject(ItemListLockFormService);
    itemListLockService = TestBed.inject(ItemListLockService);
    tireService = TestBed.inject(TireService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Tire query and add missing value', () => {
      const itemListLock: IItemListLock = { id: 456 };
      const tire: ITire = { id: 25808 };
      itemListLock.tire = tire;

      const tireCollection: ITire[] = [{ id: 29039 }];
      jest.spyOn(tireService, 'query').mockReturnValue(of(new HttpResponse({ body: tireCollection })));
      const additionalTires = [tire];
      const expectedCollection: ITire[] = [...additionalTires, ...tireCollection];
      jest.spyOn(tireService, 'addTireToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ itemListLock });
      comp.ngOnInit();

      expect(tireService.query).toHaveBeenCalled();
      expect(tireService.addTireToCollectionIfMissing).toHaveBeenCalledWith(
        tireCollection,
        ...additionalTires.map(expect.objectContaining),
      );
      expect(comp.tiresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const itemListLock: IItemListLock = { id: 456 };
      const tire: ITire = { id: 18075 };
      itemListLock.tire = tire;

      activatedRoute.data = of({ itemListLock });
      comp.ngOnInit();

      expect(comp.tiresSharedCollection).toContain(tire);
      expect(comp.itemListLock).toEqual(itemListLock);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItemListLock>>();
      const itemListLock = { id: 123 };
      jest.spyOn(itemListLockFormService, 'getItemListLock').mockReturnValue(itemListLock);
      jest.spyOn(itemListLockService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ itemListLock });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: itemListLock }));
      saveSubject.complete();

      // THEN
      expect(itemListLockFormService.getItemListLock).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(itemListLockService.update).toHaveBeenCalledWith(expect.objectContaining(itemListLock));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItemListLock>>();
      const itemListLock = { id: 123 };
      jest.spyOn(itemListLockFormService, 'getItemListLock').mockReturnValue({ id: null });
      jest.spyOn(itemListLockService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ itemListLock: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: itemListLock }));
      saveSubject.complete();

      // THEN
      expect(itemListLockFormService.getItemListLock).toHaveBeenCalled();
      expect(itemListLockService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItemListLock>>();
      const itemListLock = { id: 123 };
      jest.spyOn(itemListLockService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ itemListLock });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(itemListLockService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTire', () => {
      it('Should forward to tireService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(tireService, 'compareTire');
        comp.compareTire(entity, entity2);
        expect(tireService.compareTire).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
