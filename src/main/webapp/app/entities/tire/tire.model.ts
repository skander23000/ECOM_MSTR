import { ITireBrand } from 'app/entities/tire-brand/tire-brand.model';
import { TireType } from 'app/entities/enumerations/tire-type.model';
import { SpeedIndex } from 'app/entities/enumerations/speed-index.model';
import { ChargeIndex } from 'app/entities/enumerations/charge-index.model';

export interface ITire {
  id: number;
  reference?: string | null;
  name?: string | null;
  price?: number | null;
  tireWidth?: string | null;
  tireHeight?: string | null;
  tireDiameter?: string | null;
  tireType?: keyof typeof TireType | null;
  imageUrl?: string | null;
  speedIndex?: keyof typeof SpeedIndex | null;
  weightIndex?: keyof typeof ChargeIndex | null;
  quantity?: number | null;
  disable?: boolean | null;
  disableReason?: string | null;
  description?: string | null;
  tireBrand?: Pick<ITireBrand, 'id' | 'name'> | null;
}

export type NewTire = Omit<ITire, 'id'> & { id: null };
