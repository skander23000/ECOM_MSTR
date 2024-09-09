import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICustomerOrder } from 'app/entities/customer-order/customer-order.model';
import { CustomerOrderService } from 'app/entities/customer-order/service/customer-order.service';
import { ITire } from 'app/entities/tire/tire.model';
import { TireService } from 'app/entities/tire/service/tire.service';
import { IOrderItem } from '../order-item.model';
import { OrderItemService } from '../service/order-item.service';
import { OrderItemFormService } from './order-item-form.service';

import { OrderItemUpdateComponent } from './order-item-update.component';

describe('OrderItem Management Update Component', () => {
  let comp: OrderItemUpdateComponent;
  let fixture: ComponentFixture<OrderItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orderItemFormService: OrderItemFormService;
  let orderItemService: OrderItemService;
  let customerOrderService: CustomerOrderService;
  let tireService: TireService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [OrderItemUpdateComponent],
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
      .overrideTemplate(OrderItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrderItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orderItemFormService = TestBed.inject(OrderItemFormService);
    orderItemService = TestBed.inject(OrderItemService);
    customerOrderService = TestBed.inject(CustomerOrderService);
    tireService = TestBed.inject(TireService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CustomerOrder query and add missing value', () => {
      const orderItem: IOrderItem = { id: 456 };
      const customerOrder: ICustomerOrder = { id: 386 };
      orderItem.customerOrder = customerOrder;

      const customerOrderCollection: ICustomerOrder[] = [{ id: 18799 }];
      jest.spyOn(customerOrderService, 'query').mockReturnValue(of(new HttpResponse({ body: customerOrderCollection })));
      const additionalCustomerOrders = [customerOrder];
      const expectedCollection: ICustomerOrder[] = [...additionalCustomerOrders, ...customerOrderCollection];
      jest.spyOn(customerOrderService, 'addCustomerOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderItem });
      comp.ngOnInit();

      expect(customerOrderService.query).toHaveBeenCalled();
      expect(customerOrderService.addCustomerOrderToCollectionIfMissing).toHaveBeenCalledWith(
        customerOrderCollection,
        ...additionalCustomerOrders.map(expect.objectContaining),
      );
      expect(comp.customerOrdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Tire query and add missing value', () => {
      const orderItem: IOrderItem = { id: 456 };
      const tire: ITire = { id: 32010 };
      orderItem.tire = tire;

      const tireCollection: ITire[] = [{ id: 22464 }];
      jest.spyOn(tireService, 'query').mockReturnValue(of(new HttpResponse({ body: tireCollection })));
      const additionalTires = [tire];
      const expectedCollection: ITire[] = [...additionalTires, ...tireCollection];
      jest.spyOn(tireService, 'addTireToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orderItem });
      comp.ngOnInit();

      expect(tireService.query).toHaveBeenCalled();
      expect(tireService.addTireToCollectionIfMissing).toHaveBeenCalledWith(
        tireCollection,
        ...additionalTires.map(expect.objectContaining),
      );
      expect(comp.tiresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const orderItem: IOrderItem = { id: 456 };
      const customerOrder: ICustomerOrder = { id: 2232 };
      orderItem.customerOrder = customerOrder;
      const tire: ITire = { id: 25539 };
      orderItem.tire = tire;

      activatedRoute.data = of({ orderItem });
      comp.ngOnInit();

      expect(comp.customerOrdersSharedCollection).toContain(customerOrder);
      expect(comp.tiresSharedCollection).toContain(tire);
      expect(comp.orderItem).toEqual(orderItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrderItem>>();
      const orderItem = { id: 123 };
      jest.spyOn(orderItemFormService, 'getOrderItem').mockReturnValue(orderItem);
      jest.spyOn(orderItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orderItem }));
      saveSubject.complete();

      // THEN
      expect(orderItemFormService.getOrderItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orderItemService.update).toHaveBeenCalledWith(expect.objectContaining(orderItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrderItem>>();
      const orderItem = { id: 123 };
      jest.spyOn(orderItemFormService, 'getOrderItem').mockReturnValue({ id: null });
      jest.spyOn(orderItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orderItem }));
      saveSubject.complete();

      // THEN
      expect(orderItemFormService.getOrderItem).toHaveBeenCalled();
      expect(orderItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrderItem>>();
      const orderItem = { id: 123 };
      jest.spyOn(orderItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orderItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orderItemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCustomerOrder', () => {
      it('Should forward to customerOrderService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(customerOrderService, 'compareCustomerOrder');
        comp.compareCustomerOrder(entity, entity2);
        expect(customerOrderService.compareCustomerOrder).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
