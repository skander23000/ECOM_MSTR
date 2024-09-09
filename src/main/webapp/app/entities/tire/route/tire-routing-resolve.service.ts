import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITire } from '../tire.model';
import { TireService } from '../service/tire.service';

const tireResolve = (route: ActivatedRouteSnapshot): Observable<null | ITire> => {
  const id = route.params.id;
  if (id) {
    return inject(TireService)
      .find(id)
      .pipe(
        mergeMap((tire: HttpResponse<ITire>) => {
          if (tire.body) {
            return of(tire.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default tireResolve;
