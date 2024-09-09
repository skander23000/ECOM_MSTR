import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITireBrand } from '../tire-brand.model';
import { TireBrandService } from '../service/tire-brand.service';

const tireBrandResolve = (route: ActivatedRouteSnapshot): Observable<null | ITireBrand> => {
  const id = route.params.id;
  if (id) {
    return inject(TireBrandService)
      .find(id)
      .pipe(
        mergeMap((tireBrand: HttpResponse<ITireBrand>) => {
          if (tireBrand.body) {
            return of(tireBrand.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default tireBrandResolve;
