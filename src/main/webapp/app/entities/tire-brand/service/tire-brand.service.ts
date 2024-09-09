import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITireBrand, NewTireBrand } from '../tire-brand.model';

export type PartialUpdateTireBrand = Partial<ITireBrand> & Pick<ITireBrand, 'id'>;

export type EntityResponseType = HttpResponse<ITireBrand>;
export type EntityArrayResponseType = HttpResponse<ITireBrand[]>;

@Injectable({ providedIn: 'root' })
export class TireBrandService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tire-brands');

  create(tireBrand: NewTireBrand): Observable<EntityResponseType> {
    return this.http.post<ITireBrand>(this.resourceUrl, tireBrand, { observe: 'response' });
  }

  update(tireBrand: ITireBrand): Observable<EntityResponseType> {
    return this.http.put<ITireBrand>(`${this.resourceUrl}/${this.getTireBrandIdentifier(tireBrand)}`, tireBrand, { observe: 'response' });
  }

  partialUpdate(tireBrand: PartialUpdateTireBrand): Observable<EntityResponseType> {
    return this.http.patch<ITireBrand>(`${this.resourceUrl}/${this.getTireBrandIdentifier(tireBrand)}`, tireBrand, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITireBrand>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITireBrand[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTireBrandIdentifier(tireBrand: Pick<ITireBrand, 'id'>): number {
    return tireBrand.id;
  }

  compareTireBrand(o1: Pick<ITireBrand, 'id'> | null, o2: Pick<ITireBrand, 'id'> | null): boolean {
    return o1 && o2 ? this.getTireBrandIdentifier(o1) === this.getTireBrandIdentifier(o2) : o1 === o2;
  }

  addTireBrandToCollectionIfMissing<Type extends Pick<ITireBrand, 'id'>>(
    tireBrandCollection: Type[],
    ...tireBrandsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tireBrands: Type[] = tireBrandsToCheck.filter(isPresent);
    if (tireBrands.length > 0) {
      const tireBrandCollectionIdentifiers = tireBrandCollection.map(tireBrandItem => this.getTireBrandIdentifier(tireBrandItem));
      const tireBrandsToAdd = tireBrands.filter(tireBrandItem => {
        const tireBrandIdentifier = this.getTireBrandIdentifier(tireBrandItem);
        if (tireBrandCollectionIdentifiers.includes(tireBrandIdentifier)) {
          return false;
        }
        tireBrandCollectionIdentifiers.push(tireBrandIdentifier);
        return true;
      });
      return [...tireBrandsToAdd, ...tireBrandCollection];
    }
    return tireBrandCollection;
  }
}
