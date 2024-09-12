import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { ICustomer } from './entities/customer/customer.model';
import { PaymentInfo } from './entities/entity.payment-info';

@Injectable({
  providedIn: 'root',
})
export class SharedUserDataService {
  userInfoSubject = new BehaviorSubject<ICustomer | null>(null);
  paymentInfoSubject = new BehaviorSubject<PaymentInfo | null>(null);
  userIdSubject = new BehaviorSubject<number | null>(null);
  userInfo$ = this.userInfoSubject.asObservable();
  paymentInfo$ = this.paymentInfoSubject.asObservable();
  userId$ = this.userIdSubject.asObservable();

  setUserInfo(userInfo: ICustomer): void {
    this.userInfoSubject.next(userInfo);
  }

  setPaymentInfo(paymentInfo: PaymentInfo): void {
    this.paymentInfoSubject.next(paymentInfo);
  }

  setUserId(userId: number): void {
    this.userIdSubject.next(userId);
  }
}
