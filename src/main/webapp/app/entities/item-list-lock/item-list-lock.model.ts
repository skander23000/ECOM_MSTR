import dayjs from 'dayjs/esm';
import { ITire } from 'app/entities/tire/tire.model';

export interface IItemListLock {
  id: number;
  userUuid?: string | null;
  quantity?: number | null;
  lockTime?: dayjs.Dayjs | null;
  tire?: Pick<ITire, 'id'> | null;
}

export type NewItemListLock = Omit<IItemListLock, 'id'> & { id: null };
