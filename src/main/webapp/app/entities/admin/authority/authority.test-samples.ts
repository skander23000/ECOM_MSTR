import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '7a7d07ca-e7b5-4e4e-8c00-54ab31b47d73',
};

export const sampleWithPartialData: IAuthority = {
  name: '482fa03a-2ae9-457a-a5a3-f33c32d4272a',
};

export const sampleWithFullData: IAuthority = {
  name: 'fd1c847c-07fb-4340-a800-17a4e719468f',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
