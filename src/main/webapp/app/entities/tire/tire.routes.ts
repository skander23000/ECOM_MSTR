import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TireResolve from './route/tire-routing-resolve.service';

const tireRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/tire.component').then(m => m.TireComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/tire-detail.component').then(m => m.TireDetailComponent),
    resolve: {
      tire: TireResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/tire-update.component').then(m => m.TireUpdateComponent),
    resolve: {
      tire: TireResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/tire-update.component').then(m => m.TireUpdateComponent),
    resolve: {
      tire: TireResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tireRoute;
