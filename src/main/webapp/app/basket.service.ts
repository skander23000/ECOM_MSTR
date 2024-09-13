import { Injectable, inject } from '@angular/core';
import { ITire } from './entities/tire/tire.model';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from './core/config/application-config.service';
import { createRequestOption } from './core/request/request-util';
import { BehaviorSubject, Observable } from 'rxjs';

interface TireContainer {
  tire: ITire;
  count: number;
}

interface IDContainer {
  id: number;
  count: number;
}

@Injectable({
  providedIn: 'root',
})
export class BasketService {
  totalItemsSubject = new BehaviorSubject<number | null>(0);
  totalItems$ = this.totalItemsSubject.asObservable();
  private http = inject(HttpClient);
  private applicationConfigService = inject(ApplicationConfigService);
  private resourceUrl = this.applicationConfigService.getEndpointFor('api/item-list-locks');

  constructor() {
    const empty: TireContainer[] = [];
    localStorage.setItem('basket', JSON.stringify(empty));
    this.updateTotalItems();
  }

  addTire(t_tire: ITire, t_count = 1): Observable<boolean> {
    // eslint-disable-next-line no-console
    console.log('hey1');
    return new Observable<boolean>(sub => {
      // eslint-disable-next-line no-console
      console.log('hey11');
      this.checkAccountValidity().subscribe({
        next: value => {
          let nbtire = 0;
          let basket = this.getContent();
          if (basket.length > 0) {
            // eslint-disable-next-line no-console
            console.log('hey2');
            const item: any = basket.find(x => x.tire.id === t_tire.id);
            if (item !== undefined) {
              const index: number = basket.indexOf(item);
              basket[index].count += t_count;
              nbtire = basket[index].count;
              // eslint-disable-next-line no-console
              console.log('hey3');
            } else {
              basket.push({ count: t_count, tire: t_tire });
              nbtire = t_count;
              // eslint-disable-next-line no-console
              console.log('hey4');
            }
          } else {
            basket = new Array(1);
            basket[0] = { count: t_count, tire: t_tire };
            nbtire = t_count;
            // eslint-disable-next-line no-console
            console.log('hey5');
          }
          this.setTireBDD(t_tire, nbtire).subscribe({
            next: value2 => {
              sub.next(true);
              sub.complete();
              localStorage.setItem('basket', JSON.stringify(basket));
              this.updateTotalItems();
              // eslint-disable-next-line no-console
              console.log('hey6');
            },
            error(err) {
              // eslint-disable-next-line no-console
              console.log('hey7');
              sub.error('101|No Enough Items !');
            },
          });
        },
        error(err) {
          sub.error('102|Account timeout !');
        },
        complete() {
          // eslint-disable-next-line no-console
          console.log('heyo');
        },
      });
    });
  }

  getContent(): TireContainer[] {
    const basket: string = localStorage.getItem('basket') ?? '{}';
    const result: TireContainer[] = JSON.parse(basket);
    return result;
  }

  refreshContent(): Observable<HttpResponse<TireContainer>> {
    const options = createRequestOption('test');
    return this.http.get<TireContainer>(`${this.resourceUrl}/check-availability`, { params: options, observe: 'response' });
  }

  removeATire(t_tire: ITire): Observable<boolean> {
    return new Observable<boolean>(sub => {
      this.checkAccountValidity().subscribe({
        next: value => {
          const basket = this.getContent();
          let nbtire = 0;
          if (basket.length > 0) {
            const item: any = basket.find(x => x.tire.id === t_tire.id);
            if (item !== undefined) {
              const index: number = basket.indexOf(item);
              if (basket[index].count > 1) {
                basket[index].count -= 1;
              } else {
                sub.error("104|can't add -1 tire!");
              }
              nbtire = basket[index].count;
            }
            this.setTireBDD(t_tire, nbtire).subscribe({
              next: value2 => {
                sub.next(true);
                sub.complete();
                localStorage.setItem('basket', JSON.stringify(basket));
                this.updateTotalItems();
              },
              error(err) {
                sub.error("104|can't add -1 tire!");
              },
            });
          }
        },
        error(err) {
          sub.error('102|Account timeout !');
        },
      });
    });
  }

  removeTires(t_tire: ITire): Observable<any> {
    return new Observable<boolean>(sub => {
      this.checkAccountValidity().subscribe({
        next: value => {
          let basket: TireContainer[] = this.getContent();
          if (basket.find(p => p.tire.id === t_tire.id)) {
            basket = basket.filter(p => p.tire.id !== t_tire.id);
            this.setTireBDD(t_tire, 0).subscribe({
              next: value2 => {
                localStorage.setItem('basket', JSON.stringify(basket));
                this.updateTotalItems();
              },
              error(err) {
                sub.error('103|Item has to exist !');
              },
            });
          } else {
            sub.error('100|Item has to exist !');
          }
        },
        error(err) {
          sub.error('102|Account timeout !');
        },
      });
    });
  }
  getNumberOfATire(t_tire: ITire): number {
    const basket: TireContainer[] = this.getContent();
    if (basket.length > 0) {
      const item: any = basket.find(x => x.tire === t_tire);
      if (item !== undefined) {
        const index: number = basket.indexOf(item);
        return basket[index].count;
      }
    }
    return 0;
  }

  getNumberOfTires(): number {
    const basket: TireContainer[] = this.getContent();
    if (!Array.isArray(basket)) return 0;
    return basket.length;
  }

  wipe(): Observable<boolean> {
    return new Observable<boolean>(sub => {
      this.checkAccountValidity().subscribe({
        next: value => {
          const basket = this.getContent();
          for (const tire of basket) {
            this.removeTires(tire.tire).subscribe();

            localStorage.setItem('basket', JSON.stringify('{}'));
            this.updateTotalItems();
            sub.next(true);
            sub.complete();
          }
        },
        error(err) {
          sub.error('102|Account timeout !');
        },
      });
    });
  }

  checkAccountValidity(): Observable<HttpResponse<boolean>> {
    const options = createRequestOption('test');
    return new Observable<HttpResponse<boolean>>(hey => {
      hey.next(new HttpResponse<boolean>());
      hey.complete();
    });
    // return this.http.get<boolean>(`${this.resourceUrl}/check-availability`, { params: options, observe: 'response' });
  }
  setTire(t_tire: ITire, t_count: number): Observable<boolean> {
    return new Observable<boolean>(sub => {
      this.checkAccountValidity().subscribe({
        next: value => {
          let basket = this.getContent();
          if (t_count <= 0) {
            sub.error("104|can't add -1 tire!");
          } else {
            if (basket.length > 0) {
              const item: any = basket.find(x => x.tire.id === t_tire.id);
              if (item !== undefined) {
                const index: number = basket.indexOf(item);
                basket[index].count = t_count;
              } else {
                basket.push({ count: t_count, tire: t_tire });
              }
            } else {
              basket = new Array(1);
              basket[0] = { count: t_count, tire: t_tire };
            }
            this.setTireBDD(t_tire, t_count).subscribe({
              next: value2 => {
                localStorage.setItem('basket', JSON.stringify(basket));
                this.updateTotalItems();
                sub.next(true);
                sub.complete();
              },
              error(err) {
                sub.error('101|No Enough Items !');
              },
            });
          }
        },
        error(err) {
          sub.error('102|Account timeout !');
        },
      });
    });
  }

  private setTireBDD(t_tire: ITire, t_count: number): Observable<HttpResponse<boolean>> {
    const container: IDContainer = { id: t_tire.id, count: t_count };
    const options = createRequestOption(container);
    // return new Observable(hey => {
    //   hey.next();
    //   hey.complete();
    // });
    return this.http.get<boolean>(`${this.resourceUrl}/check-availability`, { params: options, observe: 'response' });
  }
  private updateTotalItems(): void {
    const basket = this.getContent();
    const totalItems = basket.reduce((total, item) => total + item.count, 0);
    this.totalItemsSubject.next(totalItems);
  }
}
