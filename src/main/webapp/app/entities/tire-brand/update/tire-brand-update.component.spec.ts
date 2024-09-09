import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { TireBrandService } from '../service/tire-brand.service';
import { ITireBrand } from '../tire-brand.model';
import { TireBrandFormService } from './tire-brand-form.service';

import { TireBrandUpdateComponent } from './tire-brand-update.component';

describe('TireBrand Management Update Component', () => {
  let comp: TireBrandUpdateComponent;
  let fixture: ComponentFixture<TireBrandUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tireBrandFormService: TireBrandFormService;
  let tireBrandService: TireBrandService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TireBrandUpdateComponent],
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
      .overrideTemplate(TireBrandUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TireBrandUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tireBrandFormService = TestBed.inject(TireBrandFormService);
    tireBrandService = TestBed.inject(TireBrandService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tireBrand: ITireBrand = { id: 456 };

      activatedRoute.data = of({ tireBrand });
      comp.ngOnInit();

      expect(comp.tireBrand).toEqual(tireBrand);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITireBrand>>();
      const tireBrand = { id: 123 };
      jest.spyOn(tireBrandFormService, 'getTireBrand').mockReturnValue(tireBrand);
      jest.spyOn(tireBrandService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tireBrand });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tireBrand }));
      saveSubject.complete();

      // THEN
      expect(tireBrandFormService.getTireBrand).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tireBrandService.update).toHaveBeenCalledWith(expect.objectContaining(tireBrand));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITireBrand>>();
      const tireBrand = { id: 123 };
      jest.spyOn(tireBrandFormService, 'getTireBrand').mockReturnValue({ id: null });
      jest.spyOn(tireBrandService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tireBrand: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tireBrand }));
      saveSubject.complete();

      // THEN
      expect(tireBrandFormService.getTireBrand).toHaveBeenCalled();
      expect(tireBrandService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITireBrand>>();
      const tireBrand = { id: 123 };
      jest.spyOn(tireBrandService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tireBrand });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tireBrandService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
