import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import VendaResolve from './route/venda-routing-resolve.service';

const vendaRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/venda.component').then(m => m.VendaComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/venda-detail.component').then(m => m.VendaDetailComponent),
    resolve: {
      venda: VendaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/venda-update.component').then(m => m.VendaUpdateComponent),
    resolve: {
      venda: VendaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/venda-update.component').then(m => m.VendaUpdateComponent),
    resolve: {
      venda: VendaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default vendaRoute;
