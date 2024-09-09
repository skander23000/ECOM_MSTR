import dayjs from 'dayjs/esm';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';
import { PaymentMethod } from 'app/entities/enumerations/payment-method.model';
import { PaymentStatus } from 'app/entities/enumerations/payment-status.model';

export interface ICustomerOrder {
  id: number;
  orderDate?: dayjs.Dayjs | null;
  status?: keyof typeof OrderStatus | null;
  totalAmount?: number | null;
  paymentDate?: dayjs.Dayjs | null;
  paymentMethod?: keyof typeof PaymentMethod | null;
  paymentStatus?: keyof typeof PaymentStatus | null;
}

export type NewCustomerOrder = Omit<ICustomerOrder, 'id'> & { id: null };
