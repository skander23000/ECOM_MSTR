import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IItemListLock } from '../item-list-lock.model';
import { ItemListLockService } from '../service/item-list-lock.service';

const itemListLockResolve = (route: ActivatedRouteSnapshot): Observable<null | IItemListLock> => {
  const id = route.params.id;
  if (id) {
    return inject(ItemListLockService)
      .find(id)
      .pipe(
        mergeMap((itemListLock: HttpResponse<IItemListLock>) => {
          if (itemListLock.body) {
            return of(itemListLock.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default itemListLockResolve;
