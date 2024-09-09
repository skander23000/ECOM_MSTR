import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { OrderStatus } from 'app/entities/enumerations/order-status.model';
import { PaymentMethod } from 'app/entities/enumerations/payment-method.model';
import { PaymentStatus } from 'app/entities/enumerations/payment-status.model';
import { CustomerOrderService } from '../service/customer-order.service';
import { ICustomerOrder } from '../customer-order.model';
import { CustomerOrderFormGroup, CustomerOrderFormService } from './customer-order-form.service';

@Component({
  standalone: true,
  selector: 'jhi-customer-order-update',
  templateUrl: './customer-order-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CustomerOrderUpdateComponent implements OnInit {
  isSaving = false;
  customerOrder: ICustomerOrder | null = null;
  orderStatusValues = Object.keys(OrderStatus);
  paymentMethodValues = Object.keys(PaymentMethod);
  paymentStatusValues = Object.keys(PaymentStatus);

  protected customerOrderService = inject(CustomerOrderService);
  protected customerOrderFormService = inject(CustomerOrderFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CustomerOrderFormGroup = this.customerOrderFormService.createCustomerOrderFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customerOrder }) => {
      this.customerOrder = customerOrder;
      if (customerOrder) {
        this.updateForm(customerOrder);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customerOrder = this.customerOrderFormService.getCustomerOrder(this.editForm);
    if (customerOrder.id !== null) {
      this.subscribeToSaveResponse(this.customerOrderService.update(customerOrder));
    } else {
      this.subscribeToSaveResponse(this.customerOrderService.create(customerOrder));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomerOrder>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(customerOrder: ICustomerOrder): void {
    this.customerOrder = customerOrder;
    this.customerOrderFormService.resetForm(this.editForm, customerOrder);
  }
}
