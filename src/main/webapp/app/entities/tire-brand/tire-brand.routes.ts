import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TireBrandResolve from './route/tire-brand-routing-resolve.service';

const tireBrandRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/tire-brand.component').then(m => m.TireBrandComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/tire-brand-detail.component').then(m => m.TireBrandDetailComponent),
    resolve: {
      tireBrand: TireBrandResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/tire-brand-update.component').then(m => m.TireBrandUpdateComponent),
    resolve: {
      tireBrand: TireBrandResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/tire-brand-update.component').then(m => m.TireBrandUpdateComponent),
    resolve: {
      tireBrand: TireBrandResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tireBrandRoute;
