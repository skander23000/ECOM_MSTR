import { ICustomerOrder } from 'app/entities/customer-order/customer-order.model';

export interface ICustomer {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  address?: string | null;
  city?: string | null;
  zipCode?: string | null;
  country?: string | null;
  phoneNumber?: string | null;
  customerOrder?: Pick<ICustomerOrder, 'id'> | null;
}

export type NewCustomer = Omit<ICustomer, 'id'> & { id: null };
