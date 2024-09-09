import { ICustomerOrder } from 'app/entities/customer-order/customer-order.model';
import { ITire } from 'app/entities/tire/tire.model';

export interface IOrderItem {
  id: number;
  quantity?: number | null;
  price?: number | null;
  customerOrder?: Pick<ICustomerOrder, 'id'> | null;
  tire?: Pick<ITire, 'id'> | null;
}

export type NewOrderItem = Omit<IOrderItem, 'id'> & { id: null };
