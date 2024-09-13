import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { ICustomer } from '../entities/customer/customer.model';
import { PaymentInfo } from '../entities/entity.payment-info';

@Injectable({
  providedIn: 'root',
})
export class SharedUserDataService {
  // Subjects to manage the data
  userInfoSubject = new BehaviorSubject<ICustomer | null>(null);
  paymentInfoSubject = new BehaviorSubject<PaymentInfo | null>(null);
  userIdSubject = new BehaviorSubject<number | null>(null);
  successInfoSubject = new BehaviorSubject<boolean | null>(null);
  successInfoProductSubject = new BehaviorSubject<boolean | null>(null);
  // Observables to expose the data as streams
  userInfo$ = this.userInfoSubject.asObservable();
  paymentInfo$ = this.paymentInfoSubject.asObservable();
  userId$ = this.userIdSubject.asObservable();
  successInfo$ = this.successInfoSubject.asObservable();
  successInfoProduct$ = this.successInfoProductSubject.asObservable();
  setUserInfo(userInfo: ICustomer): void {
    this.userInfoSubject.next(userInfo);
  }

  setPaymentInfo(paymentInfo: PaymentInfo): void {
    this.paymentInfoSubject.next(paymentInfo);
  }
  setUserId(userId: number): void {
    this.userIdSubject.next(userId);
  }
  setSuccessMessage(isSuccessMessage: boolean): void {
    this.successInfoSubject.next(isSuccessMessage);
  }
  setSuccessMessageProduct(isSuccessMessage: boolean): void {
    this.successInfoProductSubject.next(isSuccessMessage);
  }
}
