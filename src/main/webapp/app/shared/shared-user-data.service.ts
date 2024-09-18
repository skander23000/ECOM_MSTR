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
  successInfoProductSubject = new BehaviorSubject<boolean | null>(null);
  successInfoErrorSubject = new BehaviorSubject<boolean | null>(null);
  errorPaiementSubject = new BehaviorSubject<boolean | null>(null);
  // Observables to expose the data as streams
  successInfo$ = this.successInfoSubject.asObservable();
  successInfoProduct$ = this.successInfoProductSubject.asObservable();
  errorInfo$ = this.successInfoErrorSubject.asObservable();
  errorPaiement = this.errorPaiementSubject.asObservable();
  setUserInfo(userInfo: ICustomer): void {
    localStorage.setItem('userinfo', JSON.stringify(userInfo));
  }

  getUserInfo(): ICustomer {
    return JSON.parse(localStorage.getItem('userinfo') ?? '') as ICustomer;
  }

  setPaymentInfo(paymentInfo: PaymentInfo): void {
    localStorage.setItem('paymentinfo', JSON.stringify(paymentInfo));
  }

  getPaymentInfo(): PaymentInfo {
    return JSON.parse(localStorage.getItem('paymentinfo') ?? '') as PaymentInfo;
  }

  generateUserId(): void {
    if (!localStorage.getItem('uuserid')) localStorage.setItem('uuserid', uuidv4());
  }

  getUserId(): string {
    return localStorage.getItem('uuserid') ?? '0';
  }
  setSuccessMessage(isSuccessMessage: boolean): void {
    this.successInfoSubject.next(isSuccessMessage);
  }
  setSuccessMessageProduct(isSuccessMessage: boolean): void {
    this.successInfoProductSubject.next(isSuccessMessage);
  }
  setErrorMessage(isErrorMessage: boolean): void {
    this.successInfoErrorSubject.next(isErrorMessage);
  }

  setErrorPaiementMessage(isErrorMessage: boolean): void {
    this.errorPaiementSubject.next(isErrorMessage);
  }
}
