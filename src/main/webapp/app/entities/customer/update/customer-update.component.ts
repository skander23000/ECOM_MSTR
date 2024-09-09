import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICustomerOrder } from 'app/entities/customer-order/customer-order.model';
import { CustomerOrderService } from 'app/entities/customer-order/service/customer-order.service';
import { ICustomer } from '../customer.model';
import { CustomerService } from '../service/customer.service';
import { CustomerFormGroup, CustomerFormService } from './customer-form.service';

@Component({
  standalone: true,
  selector: 'jhi-customer-update',
  templateUrl: './customer-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CustomerUpdateComponent implements OnInit {
  isSaving = false;
  customer: ICustomer | null = null;

  customerOrdersCollection: ICustomerOrder[] = [];

  protected customerService = inject(CustomerService);
  protected customerFormService = inject(CustomerFormService);
  protected customerOrderService = inject(CustomerOrderService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CustomerFormGroup = this.customerFormService.createCustomerFormGroup();

  compareCustomerOrder = (o1: ICustomerOrder | null, o2: ICustomerOrder | null): boolean =>
    this.customerOrderService.compareCustomerOrder(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customer }) => {
      this.customer = customer;
      if (customer) {
        this.updateForm(customer);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customer = this.customerFormService.getCustomer(this.editForm);
    if (customer.id !== null) {
      this.subscribeToSaveResponse(this.customerService.update(customer));
    } else {
      this.subscribeToSaveResponse(this.customerService.create(customer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomer>>): void {
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

  protected updateForm(customer: ICustomer): void {
    this.customer = customer;
    this.customerFormService.resetForm(this.editForm, customer);

    this.customerOrdersCollection = this.customerOrderService.addCustomerOrderToCollectionIfMissing<ICustomerOrder>(
      this.customerOrdersCollection,
      customer.customerOrder,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.customerOrderService
      .query({ filter: 'customer-is-null' })
      .pipe(map((res: HttpResponse<ICustomerOrder[]>) => res.body ?? []))
      .pipe(
        map((customerOrders: ICustomerOrder[]) =>
          this.customerOrderService.addCustomerOrderToCollectionIfMissing<ICustomerOrder>(customerOrders, this.customer?.customerOrder),
        ),
      )
      .subscribe((customerOrders: ICustomerOrder[]) => (this.customerOrdersCollection = customerOrders));
  }
}
