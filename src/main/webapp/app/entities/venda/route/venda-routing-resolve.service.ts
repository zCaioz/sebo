import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVenda } from '../venda.model';
import { VendaService } from '../service/venda.service';

const vendaResolve = (route: ActivatedRouteSnapshot): Observable<null | IVenda> => {
  const id = route.params.id;
  if (id) {
    return inject(VendaService)
      .find(id)
      .pipe(
        mergeMap((venda: HttpResponse<IVenda>) => {
          if (venda.body) {
            return of(venda.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default vendaResolve;
