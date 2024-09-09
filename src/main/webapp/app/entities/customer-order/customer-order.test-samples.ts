import dayjs from 'dayjs/esm';

import { ICustomerOrder, NewCustomerOrder } from './customer-order.model';

export const sampleWithRequiredData: ICustomerOrder = {
  id: 27109,
  orderDate: dayjs('2024-09-08T19:50'),
  status: 'CANCELLED',
  totalAmount: 21340.01,
  paymentDate: dayjs('2024-09-09T05:44'),
  paymentMethod: 'BANK_TRANSFER',
  paymentStatus: 'COMPLETED',
};

export const sampleWithPartialData: ICustomerOrder = {
  id: 28606,
  orderDate: dayjs('2024-09-09T00:09'),
  status: 'CANCELLED',
  totalAmount: 32615.97,
  paymentDate: dayjs('2024-09-09T09:16'),
  paymentMethod: 'BANK_TRANSFER',
  paymentStatus: 'FAILED',
};

export const sampleWithFullData: ICustomerOrder = {
  id: 1295,
  orderDate: dayjs('2024-09-08T20:42'),
  status: 'SHIPPED',
  totalAmount: 25301.14,
  paymentDate: dayjs('2024-09-08T17:35'),
  paymentMethod: 'CASH_ON_DELIVERY',
  paymentStatus: 'FAILED',
};

export const sampleWithNewData: NewCustomerOrder = {
  orderDate: dayjs('2024-09-09T07:09'),
  status: 'COMPLETED',
  totalAmount: 10545.42,
  paymentDate: dayjs('2024-09-08T20:08'),
  paymentMethod: 'BANK_TRANSFER',
  paymentStatus: 'PENDING',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
