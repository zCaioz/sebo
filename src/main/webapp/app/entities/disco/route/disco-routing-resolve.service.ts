import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDisco } from '../disco.model';
import { DiscoService } from '../service/disco.service';

const discoResolve = (route: ActivatedRouteSnapshot): Observable<null | IDisco> => {
  const id = route.params.id;
  if (id) {
    return inject(DiscoService)
      .find(id)
      .pipe(
        mergeMap((disco: HttpResponse<IDisco>) => {
          if (disco.body) {
            return of(disco.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default discoResolve;
