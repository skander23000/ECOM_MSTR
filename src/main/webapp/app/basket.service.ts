import { inject, Injectable } from '@angular/core';
import { ITire } from './entities/tire/tire.model';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from './core/config/application-config.service';
import { createRequestOption } from './core/request/request-util';
import { BehaviorSubject, concatMap, from, Observable, of, Subscriber } from 'rxjs';
import { SharedUserDataService } from './shared/shared-user-data.service';

interface TireContainer {
  tire: ITire;
  count: number;
}

interface RequestContainer {
  userUuid: string;
  tireId: number;
  quantity: number;
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

  constructor(private userinfo: SharedUserDataService) {
    userinfo.generateUserId();
    this.updateTotalItems();
  }

  addTire(t_tire: ITire, t_count = 1): Observable<boolean> {
    // enlever les fonction intermédiaire puis récupérer le bool de next
    t_count = Number(t_count);
    return new Observable<boolean>(sub => {
      this.checkAccountValidity().subscribe({
        next: check => {
          let nbtire;
          let basket = this.getContent();

          if (basket.length > 0) {
            if (check.body) {
              const item: any = basket.find(x => x.tire.id === t_tire.id);
              if (item !== undefined) {
                const index: number = basket.indexOf(item);
                basket[index].count += t_count;
                nbtire = basket[index].count;
              } else {
                basket.push({ count: t_count, tire: t_tire });
                nbtire = t_count;
              }
            } else {
              this.wipe().subscribe();
              sub.error('102|Account timeout !');
              return;
            }
          } else {
            basket = new Array(1);
            basket[0] = { count: t_count, tire: t_tire };
            nbtire = t_count;
          }
          // eslint-disable-next-line no-console
          console.log(nbtire);
          this.setTireBDD(t_tire, nbtire).subscribe({
            // eslint-disable-next-line @typescript-eslint/no-shadow
            next: check => {
              // eslint-disable-next-line no-console
              console.log(check);
              if (check.body) {
                sub.next(true);
                sub.complete();
                localStorage.setItem('basket', JSON.stringify(basket));
                this.updateTotalItems();
              } else {
                sub.error('101|No Enough Items !');
              }
            },
            error() {
              sub.error('101|No Enough Items !');
            },
          });
        },
        error: () => {
          this.wipe();
          sub.error('102|Account timeout !');
        },
      });
    });
  }

  getContent(): TireContainer[] {
    const basket: string = localStorage.getItem('basket') ?? '[]';
    return JSON.parse(basket) as TireContainer[];
  }

  getObservableContent(): Observable<TireContainer[]> {
    const basket: string = localStorage.getItem('basket') ?? '[]';
    const parsedBasket: TireContainer[] = JSON.parse(basket) as TireContainer[];
    return of(parsedBasket); // Retourne l'Observable contenant le tableau
  }

  refreshContent(): Observable<HttpResponse<TireContainer>> {
    const options = createRequestOption('test');
    return this.http.get<TireContainer>(`${this.resourceUrl}/check-availability`, { params: options, observe: 'response' });
  }

  removeATire(t_tire: ITire): Observable<boolean> {
    return new Observable<boolean>(sub => {
      this.checkAccountValidity().subscribe({
        next: check => {
          const basket = this.getContent();
          let nbtire = 0;
          if (basket.length > 0) {
            if (check.body) {
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
                // eslint-disable-next-line @typescript-eslint/no-shadow
                next: check => {
                  if (check.body) {
                    sub.next(true);
                    sub.complete();
                    localStorage.setItem('basket', JSON.stringify(basket));
                    this.updateTotalItems();
                  } else {
                    sub.error("104|can't add -1 tire!");
                  }
                },
                error() {
                  sub.error("104|can't add -1 tire!");
                },
              });
            } else {
              this.wipe().subscribe();
              sub.error('102|Account timeout !');
            }
          }
        },
        error() {
          sub.error('102|Account timeout !');
        },
      });
    });
  }

  removeTires(t_tire: ITire): Observable<boolean> {
    return new Observable<boolean>(sub => {
      this.checkAccountValidity().subscribe({
        next: check => {
          let basket: TireContainer[] = this.getContent();
          if (basket.find(p => p.tire.id === t_tire.id)) {
            if (check.body) {
              basket = basket.filter(p => p.tire.id !== t_tire.id);
              this.setTireBDD(t_tire, 0).subscribe({
                // eslint-disable-next-line @typescript-eslint/no-shadow
                next: check => {
                  if (check.body) {
                    localStorage.setItem('basket', JSON.stringify(basket));
                    this.updateTotalItems();
                    sub.next(); // Émet une valeur de succès
                    sub.complete(); // Complète l'observable
                  } else {
                    sub.error('103|Item has to exist !');
                  }
                },
                error() {
                  sub.error('103|Item has to exist !');
                },
              });
            } else {
              this.wipe().subscribe();
              sub.error('102|Account timeout !');
            }
          }
        },
        error() {
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
        next: check => {
          const basket = this.getContent();

          if (basket.length === 0) {
            this.updateTotalItems();
            sub.next(true);
            sub.complete();
          } else {
            if (check.body) {
              from(basket)
                .pipe(concatMap(tire => this.removeTires(tire.tire)))
                .subscribe({
                  next: () => {
                    // Once all tires are removed
                    this.updateTotalItems();
                    sub.next(true);
                    sub.complete();
                  },
                  error(err) {
                    sub.error(err || 'Error removing tires.');
                  },
                });
            } else {
              localStorage.setItem('basket', '[]');
              sub.error('102|Account timeout !');
            }
          }
        },
        error() {
          localStorage.setItem('basket', '[]');
          sub.error('102|Account timeout !');
        },
      });
    });
  }

  checkAccountValidity(): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(`${this.resourceUrl}/exist`, {
      params: createRequestOption({ userUuid: this.userinfo.getUserId() }),
      observe: 'response',
    });
  }
  setTire(t_tire: ITire, t_count: number): Observable<boolean> {
    return new Observable<boolean>(sub => {
      this.checkAccountValidity().subscribe({
        next: check => {
          let basket = this.getContent();
          if (t_count <= 0) {
            sub.error("104|can't add -1 tire!");
          } else {
            if (check.body) {
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
                // eslint-disable-next-line @typescript-eslint/no-shadow
                next: check => {
                  if (check.body) {
                    localStorage.setItem('basket', JSON.stringify(basket));
                    this.updateTotalItems();
                    sub.next(true);
                    sub.complete();
                  } else {
                    sub.error('101|No Enough Items !');
                  }
                },
              });
            } else {
              this.wipe().subscribe();
              sub.error('102|Account timeout !');
            }
          }
        },
        error() {
          sub.error('102|Account timeout !');
        },
      });
    });
  }

  private setTireBDD(t_tire: ITire, t_count: number): Observable<HttpResponse<boolean>> {
    const container: RequestContainer = { userUuid: this.userinfo.getUserId(), tireId: t_tire.id, quantity: t_count };
    const options = createRequestOption(container);
    return this.http.get<boolean>(`${this.resourceUrl}/check-availability`, { params: options, observe: 'response' });
  }
  private updateTotalItems(): void {
    const basket = this.getContent();
    const totalItems = basket.reduce((total, item) => total + item.count, 0);
    this.totalItemsSubject.next(totalItems);
  }
}
