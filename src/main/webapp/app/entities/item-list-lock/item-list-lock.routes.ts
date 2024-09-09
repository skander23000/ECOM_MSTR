import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ItemListLockResolve from './route/item-list-lock-routing-resolve.service';

const itemListLockRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/item-list-lock.component').then(m => m.ItemListLockComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/item-list-lock-detail.component').then(m => m.ItemListLockDetailComponent),
    resolve: {
      itemListLock: ItemListLockResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/item-list-lock-update.component').then(m => m.ItemListLockUpdateComponent),
    resolve: {
      itemListLock: ItemListLockResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/item-list-lock-update.component').then(m => m.ItemListLockUpdateComponent),
    resolve: {
      itemListLock: ItemListLockResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default itemListLockRoute;
