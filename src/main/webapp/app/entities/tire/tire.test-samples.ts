import { ITire, NewTire } from './tire.model';

export const sampleWithRequiredData: ITire = {
  id: 15362,
  name: 'venir',
  price: 31043.24,
  tireWidth: 'gens',
  tireHeight: 'au cas où',
  tireDiameter: 'pourvu que poser',
  tireType: 'ALL_SEASON',
  imageUrl: 'vers',
  speedIndex: 'A_ONE',
  weightIndex: 'TWENTY',
  quantity: 9990,
  disable: true,
};

export const sampleWithPartialData: ITire = {
  id: 23950,
  name: 'quand alors présidence',
  price: 8631.33,
  tireWidth: "zzzz à l'entour de prétendre",
  tireHeight: 'broum',
  tireDiameter: 'diplomate hôte',
  tireType: 'WINTER',
  imageUrl: 'près smack',
  speedIndex: 'A_EIGHT',
  weightIndex: 'TWENTY',
  quantity: 6122,
  disable: true,
  description: 'lectorat hé jeune enfant',
};

export const sampleWithFullData: ITire = {
  id: 30321,
  reference: 'quelque ouf',
  name: 'tsoin-tsoin conseil municipal aux alentours de',
  price: 1050.12,
  tireWidth: 'aimable depuis personnel',
  tireHeight: 'clac camarade',
  tireDiameter: 'de la part de désigner',
  tireType: 'WINTER',
  imageUrl: 'si bien que à seule fin de',
  speedIndex: 'N',
  weightIndex: 'ONE_HUNDRED_TWENTY',
  quantity: 9106,
  disable: true,
  disableReason: 'après que membre titulaire',
  description: 'séculaire camarade',
};

export const sampleWithNewData: NewTire = {
  name: 'prout',
  price: 4011.41,
  tireWidth: 'dring personnel délégation',
  tireHeight: 'crac sans',
  tireDiameter: 'spécialiste attarder',
  tireType: 'WINTER',
  imageUrl: 'apparemment émérite',
  speedIndex: 'N',
  weightIndex: 'TWENTY',
  quantity: 10430,
  disable: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
