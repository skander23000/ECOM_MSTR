import { IOrderItem, NewOrderItem } from './order-item.model';

export const sampleWithRequiredData: IOrderItem = {
  id: 2964,
  quantity: 32572,
  price: 7058.32,
};

export const sampleWithPartialData: IOrderItem = {
  id: 6430,
  quantity: 7095,
  price: 2436.75,
};

export const sampleWithFullData: IOrderItem = {
  id: 9294,
  quantity: 25015,
  price: 31307.77,
};

export const sampleWithNewData: NewOrderItem = {
  quantity: 2628,
  price: 21110.35,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
