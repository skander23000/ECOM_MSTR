import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 11199,
  login: 'ET@-MCs6\\PyBBBOq\\^GW\\3bP',
};

export const sampleWithPartialData: IUser = {
  id: 2847,
  login: 'L.',
};

export const sampleWithFullData: IUser = {
  id: 28664,
  login: 'mN3-',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
