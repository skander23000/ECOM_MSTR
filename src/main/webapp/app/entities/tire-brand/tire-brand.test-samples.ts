import { ITireBrand, NewTireBrand } from './tire-brand.model';

export const sampleWithRequiredData: ITireBrand = {
  id: 12942,
  name: 'sitôt que chef smack',
  logoUrl: 'sitôt contempler envers',
};

export const sampleWithPartialData: ITireBrand = {
  id: 29651,
  name: 'debout miam du fait que',
  logoUrl: 'après prestataire de services',
};

export const sampleWithFullData: ITireBrand = {
  id: 23666,
  name: 'clac quand',
  logoUrl: 'après-demain tchou tchouu sur',
};

export const sampleWithNewData: NewTireBrand = {
  name: 'si de façon à',
  logoUrl: 'calme',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
