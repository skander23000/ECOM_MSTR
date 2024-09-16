import { ITire } from './tire/tire.model';

export interface TireContainer {
  tire: ITire | null;
  count?: number;
}
