import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ITireBrand } from 'app/entities/tire-brand/tire-brand.model';
import { TireBrandService } from 'app/entities/tire-brand/service/tire-brand.service';
import { TireService } from '../service/tire.service';
import { ITire } from '../tire.model';
import { TireFormService } from './tire-form.service';

import { TireUpdateComponent } from './tire-update.component';

describe('Tire Management Update Component', () => {
  let comp: TireUpdateComponent;
  let fixture: ComponentFixture<TireUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tireFormService: TireFormService;
  let tireService: TireService;
  let tireBrandService: TireBrandService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TireUpdateComponent],
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
      .overrideTemplate(TireUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TireUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tireFormService = TestBed.inject(TireFormService);
    tireService = TestBed.inject(TireService);
    tireBrandService = TestBed.inject(TireBrandService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TireBrand query and add missing value', () => {
      const tire: ITire = { id: 456 };
      const tireBrand: ITireBrand = { id: 27469 };
      tire.tireBrand = tireBrand;

      const tireBrandCollection: ITireBrand[] = [{ id: 5294 }];
      jest.spyOn(tireBrandService, 'query').mockReturnValue(of(new HttpResponse({ body: tireBrandCollection })));
      const additionalTireBrands = [tireBrand];
      const expectedCollection: ITireBrand[] = [...additionalTireBrands, ...tireBrandCollection];
      jest.spyOn(tireBrandService, 'addTireBrandToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tire });
      comp.ngOnInit();

      expect(tireBrandService.query).toHaveBeenCalled();
      expect(tireBrandService.addTireBrandToCollectionIfMissing).toHaveBeenCalledWith(
        tireBrandCollection,
        ...additionalTireBrands.map(expect.objectContaining),
      );
      expect(comp.tireBrandsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const tire: ITire = { id: 456 };
      const tireBrand: ITireBrand = { id: 14997 };
      tire.tireBrand = tireBrand;

      activatedRoute.data = of({ tire });
      comp.ngOnInit();

      expect(comp.tireBrandsSharedCollection).toContain(tireBrand);
      expect(comp.tire).toEqual(tire);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITire>>();
      const tire = { id: 123 };
      jest.spyOn(tireFormService, 'getTire').mockReturnValue(tire);
      jest.spyOn(tireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tire }));
      saveSubject.complete();

      // THEN
      expect(tireFormService.getTire).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tireService.update).toHaveBeenCalledWith(expect.objectContaining(tire));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITire>>();
      const tire = { id: 123 };
      jest.spyOn(tireFormService, 'getTire').mockReturnValue({ id: null });
      jest.spyOn(tireService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tire: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tire }));
      saveSubject.complete();

      // THEN
      expect(tireFormService.getTire).toHaveBeenCalled();
      expect(tireService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITire>>();
      const tire = { id: 123 };
      jest.spyOn(tireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tireService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTireBrand', () => {
      it('Should forward to tireBrandService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(tireBrandService, 'compareTireBrand');
        comp.compareTireBrand(entity, entity2);
        expect(tireBrandService.compareTireBrand).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
