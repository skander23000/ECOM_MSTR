import dayjs from 'dayjs/esm';

import { IItemListLock, NewItemListLock } from './item-list-lock.model';

export const sampleWithRequiredData: IItemListLock = {
  id: 15174,
  userUuid: 'dc25dd62-385e-40e6-8f93-2fde3da13ba2',
  quantity: 11208,
  lockTime: dayjs('2024-09-08T18:51'),
};

export const sampleWithPartialData: IItemListLock = {
  id: 30433,
  userUuid: '39222bc6-d9ab-4f81-88a6-85b2070543bd',
  quantity: 18784,
  lockTime: dayjs('2024-09-08T18:01'),
};

export const sampleWithFullData: IItemListLock = {
  id: 31517,
  userUuid: '90f61867-c70e-4206-a76c-f56e13ff074f',
  quantity: 22749,
  lockTime: dayjs('2024-09-08T16:05'),
};

export const sampleWithNewData: NewItemListLock = {
  userUuid: '3030f41b-2a62-4fb3-90cb-83ad4aedd34d',
  quantity: 26477,
  lockTime: dayjs('2024-09-09T07:41'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
