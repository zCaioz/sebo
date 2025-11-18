import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDisco, NewDisco } from '../disco.model';

export type PartialUpdateDisco = Partial<IDisco> & Pick<IDisco, 'id'>;

export type EntityResponseType = HttpResponse<IDisco>;
export type EntityArrayResponseType = HttpResponse<IDisco[]>;

@Injectable({ providedIn: 'root' })
export class DiscoService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/discos');

  create(disco: NewDisco): Observable<EntityResponseType> {
    return this.http.post<IDisco>(this.resourceUrl, disco, { observe: 'response' });
  }

  update(disco: IDisco): Observable<EntityResponseType> {
    return this.http.put<IDisco>(`${this.resourceUrl}/${this.getDiscoIdentifier(disco)}`, disco, { observe: 'response' });
  }

  partialUpdate(disco: PartialUpdateDisco): Observable<EntityResponseType> {
    return this.http.patch<IDisco>(`${this.resourceUrl}/${this.getDiscoIdentifier(disco)}`, disco, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDisco>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDisco[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDiscoIdentifier(disco: Pick<IDisco, 'id'>): number {
    return disco.id;
  }

  compareDisco(o1: Pick<IDisco, 'id'> | null, o2: Pick<IDisco, 'id'> | null): boolean {
    return o1 && o2 ? this.getDiscoIdentifier(o1) === this.getDiscoIdentifier(o2) : o1 === o2;
  }

  addDiscoToCollectionIfMissing<Type extends Pick<IDisco, 'id'>>(
    discoCollection: Type[],
    ...discosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const discos: Type[] = discosToCheck.filter(isPresent);
    if (discos.length > 0) {
      const discoCollectionIdentifiers = discoCollection.map(discoItem => this.getDiscoIdentifier(discoItem));
      const discosToAdd = discos.filter(discoItem => {
        const discoIdentifier = this.getDiscoIdentifier(discoItem);
        if (discoCollectionIdentifiers.includes(discoIdentifier)) {
          return false;
        }
        discoCollectionIdentifiers.push(discoIdentifier);
        return true;
      });
      return [...discosToAdd, ...discoCollection];
    }
    return discoCollection;
  }
}
