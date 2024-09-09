import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { CustomerOrderService } from '../service/customer-order.service';
import { ICustomerOrder } from '../customer-order.model';
import { CustomerOrderFormService } from './customer-order-form.service';

import { CustomerOrderUpdateComponent } from './customer-order-update.component';

describe('CustomerOrder Management Update Component', () => {
  let comp: CustomerOrderUpdateComponent;
  let fixture: ComponentFixture<CustomerOrderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let customerOrderFormService: CustomerOrderFormService;
  let customerOrderService: CustomerOrderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CustomerOrderUpdateComponent],
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
      .overrideTemplate(CustomerOrderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CustomerOrderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    customerOrderFormService = TestBed.inject(CustomerOrderFormService);
    customerOrderService = TestBed.inject(CustomerOrderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const customerOrder: ICustomerOrder = { id: 456 };

      activatedRoute.data = of({ customerOrder });
      comp.ngOnInit();

      expect(comp.customerOrder).toEqual(customerOrder);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomerOrder>>();
      const customerOrder = { id: 123 };
      jest.spyOn(customerOrderFormService, 'getCustomerOrder').mockReturnValue(customerOrder);
      jest.spyOn(customerOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customerOrder }));
      saveSubject.complete();

      // THEN
      expect(customerOrderFormService.getCustomerOrder).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(customerOrderService.update).toHaveBeenCalledWith(expect.objectContaining(customerOrder));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomerOrder>>();
      const customerOrder = { id: 123 };
      jest.spyOn(customerOrderFormService, 'getCustomerOrder').mockReturnValue({ id: null });
      jest.spyOn(customerOrderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerOrder: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: customerOrder }));
      saveSubject.complete();

      // THEN
      expect(customerOrderFormService.getCustomerOrder).toHaveBeenCalled();
      expect(customerOrderService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICustomerOrder>>();
      const customerOrder = { id: 123 };
      jest.spyOn(customerOrderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ customerOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(customerOrderService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
