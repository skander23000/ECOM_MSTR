import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IItemListLock, NewItemListLock } from '../item-list-lock.model';

export type PartialUpdateItemListLock = Partial<IItemListLock> & Pick<IItemListLock, 'id'>;

type RestOf<T extends IItemListLock | NewItemListLock> = Omit<T, 'lockTime'> & {
  lockTime?: string | null;
};

export type RestItemListLock = RestOf<IItemListLock>;

export type NewRestItemListLock = RestOf<NewItemListLock>;

export type PartialUpdateRestItemListLock = RestOf<PartialUpdateItemListLock>;

export type EntityResponseType = HttpResponse<IItemListLock>;
export type EntityArrayResponseType = HttpResponse<IItemListLock[]>;

@Injectable({ providedIn: 'root' })
export class ItemListLockService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/item-list-locks');

  create(itemListLock: NewItemListLock): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemListLock);
    return this.http
      .post<RestItemListLock>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(itemListLock: IItemListLock): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemListLock);
    return this.http
      .put<RestItemListLock>(`${this.resourceUrl}/${this.getItemListLockIdentifier(itemListLock)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(itemListLock: PartialUpdateItemListLock): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(itemListLock);
    return this.http
      .patch<RestItemListLock>(`${this.resourceUrl}/${this.getItemListLockIdentifier(itemListLock)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestItemListLock>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestItemListLock[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getItemListLockIdentifier(itemListLock: Pick<IItemListLock, 'id'>): number {
    return itemListLock.id;
  }

  compareItemListLock(o1: Pick<IItemListLock, 'id'> | null, o2: Pick<IItemListLock, 'id'> | null): boolean {
    return o1 && o2 ? this.getItemListLockIdentifier(o1) === this.getItemListLockIdentifier(o2) : o1 === o2;
  }

  addItemListLockToCollectionIfMissing<Type extends Pick<IItemListLock, 'id'>>(
    itemListLockCollection: Type[],
    ...itemListLocksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const itemListLocks: Type[] = itemListLocksToCheck.filter(isPresent);
    if (itemListLocks.length > 0) {
      const itemListLockCollectionIdentifiers = itemListLockCollection.map(itemListLockItem =>
        this.getItemListLockIdentifier(itemListLockItem),
      );
      const itemListLocksToAdd = itemListLocks.filter(itemListLockItem => {
        const itemListLockIdentifier = this.getItemListLockIdentifier(itemListLockItem);
        if (itemListLockCollectionIdentifiers.includes(itemListLockIdentifier)) {
          return false;
        }
        itemListLockCollectionIdentifiers.push(itemListLockIdentifier);
        return true;
      });
      return [...itemListLocksToAdd, ...itemListLockCollection];
    }
    return itemListLockCollection;
  }

  protected convertDateFromClient<T extends IItemListLock | NewItemListLock | PartialUpdateItemListLock>(itemListLock: T): RestOf<T> {
    return {
      ...itemListLock,
      lockTime: itemListLock.lockTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restItemListLock: RestItemListLock): IItemListLock {
    return {
      ...restItemListLock,
      lockTime: restItemListLock.lockTime ? dayjs(restItemListLock.lockTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestItemListLock>): HttpResponse<IItemListLock> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestItemListLock[]>): HttpResponse<IItemListLock[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
