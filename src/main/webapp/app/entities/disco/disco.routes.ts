import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DiscoResolve from './route/disco-routing-resolve.service';

const discoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/disco.component').then(m => m.DiscoComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/disco-detail.component').then(m => m.DiscoDetailComponent),
    resolve: {
      disco: DiscoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/disco-update.component').then(m => m.DiscoUpdateComponent),
    resolve: {
      disco: DiscoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/disco-update.component').then(m => m.DiscoUpdateComponent),
    resolve: {
      disco: DiscoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default discoRoute;
