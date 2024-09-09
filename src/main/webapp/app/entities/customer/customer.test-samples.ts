import { ICustomer, NewCustomer } from './customer.model';

export const sampleWithRequiredData: ICustomer = {
  id: 1917,
  firstName: 'Avigaëlle',
  lastName: 'Muller',
  email: 'Brunehaut.Brun@hotmail.fr',
  address: 'autour administration',
  city: 'Lyon',
  zipCode: '99334',
  country: 'Ghana',
};

export const sampleWithPartialData: ICustomer = {
  id: 5802,
  firstName: 'Adalbéron',
  lastName: 'Le gall',
  email: 'Flavien_Aubry33@yahoo.fr',
  address: 'approfondir alors',
  city: 'Dunkerque',
  zipCode: '18464',
  country: 'Norvège',
};

export const sampleWithFullData: ICustomer = {
  id: 7640,
  firstName: 'Émérance',
  lastName: 'Gonzalez',
  email: 'Bohemond7@hotmail.fr',
  address: 'toc délectable snif',
  city: 'Mulhouse',
  zipCode: '73798',
  country: 'Turkménistan',
  phoneNumber: 'de sorte que',
};

export const sampleWithNewData: NewCustomer = {
  firstName: 'Dorine',
  lastName: 'Roy',
  email: 'Claudien48@gmail.com',
  address: 'conseil municipal',
  city: 'Calais',
  zipCode: '40030',
  country: 'Oman',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
