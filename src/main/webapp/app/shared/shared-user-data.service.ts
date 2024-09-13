import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { ICustomer } from '../entities/customer/customer.model';
import { PaymentInfo } from '../entities/entity.payment-info';
import { v4 as uuidv4 } from 'uuid';

@Injectable({
  providedIn: 'root',
})
export class SharedUserDataService {
  // Subjects to manage the data
  successInfoSubject = new BehaviorSubject<boolean | null>(null);
  // Observables to expose the data as streams
  successInfo$ = this.successInfoSubject.asObservable();

  setUserInfo(userInfo: ICustomer): void {
    localStorage.setItem('userinfo', JSON.stringify(userInfo));
  }

  getUserInfo(): ICustomer {
    const tmp: ICustomer = JSON.parse(localStorage.getItem('userinfo') ?? '');
    return tmp;
  }

  setPaymentInfo(paymentInfo: PaymentInfo): void {
    localStorage.setItem('paymentinfo', JSON.stringify(paymentInfo));
  }

  getPaymentInfo(): PaymentInfo {
    const tmp: PaymentInfo = JSON.parse(localStorage.getItem('paymentinfo') ?? '');
    return tmp;
  }

  generateUserId(): void {
    localStorage.setItem('uuserid', uuidv4());
  }

  getUserId(): string {
    return localStorage.getItem('uuserid') ?? '0';
  }
  setSuccessMessage(isSuccessMessage: boolean): void {
    this.successInfoSubject.next(isSuccessMessage);
  }
}
