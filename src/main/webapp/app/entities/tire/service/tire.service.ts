import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITire, NewTire } from '../tire.model';

export type PartialUpdateTire = Partial<ITire> & Pick<ITire, 'id'>;

export type EntityResponseType = HttpResponse<ITire>;
export type EntityArrayResponseType = HttpResponse<ITire[]>;

@Injectable({ providedIn: 'root' })
export class TireService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tires');

  create(tire: NewTire): Observable<EntityResponseType> {
    return this.http.post<ITire>(this.resourceUrl, tire, { observe: 'response' });
  }

  update(tire: ITire): Observable<EntityResponseType> {
    return this.http.put<ITire>(`${this.resourceUrl}/${this.getTireIdentifier(tire)}`, tire, { observe: 'response' });
  }

  partialUpdate(tire: PartialUpdateTire): Observable<EntityResponseType> {
    return this.http.patch<ITire>(`${this.resourceUrl}/${this.getTireIdentifier(tire)}`, tire, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITire>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITire[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  publishImage(inputFile: File): Observable<string> {
    const file: FormData = new FormData();
    file.append('file', inputFile);
    return this.http.post<string>(this.applicationConfigService.getEndpointFor('api/aws'), file);
  }

  getTireIdentifier(tire: Pick<ITire, 'id'>): number {
    return tire.id;
  }

  compareTire(o1: Pick<ITire, 'id'> | null, o2: Pick<ITire, 'id'> | null): boolean {
    return o1 && o2 ? this.getTireIdentifier(o1) === this.getTireIdentifier(o2) : o1 === o2;
  }

  addTireToCollectionIfMissing<Type extends Pick<ITire, 'id'>>(
    tireCollection: Type[],
    ...tiresToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tires: Type[] = tiresToCheck.filter(isPresent);
    if (tires.length > 0) {
      const tireCollectionIdentifiers = tireCollection.map(tireItem => this.getTireIdentifier(tireItem));
      const tiresToAdd = tires.filter(tireItem => {
        const tireIdentifier = this.getTireIdentifier(tireItem);
        if (tireCollectionIdentifiers.includes(tireIdentifier)) {
          return false;
        }
        tireCollectionIdentifiers.push(tireIdentifier);
        return true;
      });
      return [...tiresToAdd, ...tireCollection];
    }
    return tireCollection;
  }
}
