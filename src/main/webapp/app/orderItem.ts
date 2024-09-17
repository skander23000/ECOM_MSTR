export type Customer = {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  address: string;
  city: string;
  zipCode: string;
  country: string;
  phoneNumber: string;
};

export type CustomerOrder = {
  id: number;
  orderDate: string;
  status: 'PENDING' | 'COMPLETED' | 'FAILED';
  totalAmount: number;
  paymentDate: string;
  paymentMethod: 'CREDIT_CARD' | 'PAYPAL' | 'BANK_TRANSFER' | 'CASH_ON_DELIVERY';
  paymentStatus: 'PENDING' | 'COMPLETED' | 'FAILED';
  customer: Customer;
};

export type Tire = {
  id: number;
  reference: string;
  name: string;
  price: number;
  tireWidth: number;
  tireHeight: number;
  tireDiameter: number;
  tireType: 'SUMMER' | 'WINTER' | 'ALL_SEASON';
  imageUrl: string;
  speedIndex: string;
  weightIndex: string;
  quantity: number;
  disable: boolean;
  disableReason: string;
  description: string;
  tireBrand: string | null;
  version: number;
};

export type OrderItem = {
  id: number;
  quantity: number;
  price: number;
  customerOrder: CustomerOrder;
  tire: Tire;
};
