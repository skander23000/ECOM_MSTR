import { Injectable } from '@angular/core';
<<<<<<<< HEAD:src/main/webapp/app/entities/basket.service.ts
import { ITire } from './tire/tire.model';
========
import { ITire } from '../entities/tire/tire.model';
>>>>>>>> 57da280 (Update : css amelioration and working add to basket):src/main/webapp/app/basket/basket.service.ts

interface TireContainer {
  tire: ITire;
  count: number;
}

@Injectable({
  providedIn: 'root',
})
export class BasketService {
  setTire(t_tire: ITire, t_count: number): void {
    let basket = this.getContent();
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
      basket[0] = { count: 1, tire: t_tire };
    }
    localStorage.setItem('basket', JSON.stringify(basket));
  }

  addTire(t_tire: ITire): void {
    let basket = this.getContent();
    if (basket.length > 0) {
      const item: any = basket.find(x => x.tire.id === t_tire.id);
      if (item !== undefined) {
        const index: number = basket.indexOf(item);
        basket[index].count += 1;
      } else {
        basket.push({ count: 1, tire: t_tire });
      }
    } else {
      basket = new Array(1);
      basket[0] = { count: 1, tire: t_tire };
    }
    localStorage.setItem('basket', JSON.stringify(basket));
  }

  getContent(): TireContainer[] {
    const basket: string = localStorage.getItem('basket') ?? '{}';
    return JSON.parse(basket) as TireContainer[];
  }

  removeATire(t_tire: ITire): void {
    const basket = this.getContent();
    if (basket.length > 0) {
      const item: any = basket.find(x => x.tire.id === t_tire.id);
      if (item !== undefined) {
        const index: number = basket.indexOf(item);
        if (basket[index].count > 1) basket[index].count -= 1;
      }
    }
    localStorage.setItem('basket', JSON.stringify(basket));
  }

  removeTires(t_tire: ITire): void {
    let basket: TireContainer[] = this.getContent();
    if (basket.find(p => p.tire.id === t_tire.id)) {
      basket = basket.filter(p => p.tire.id !== t_tire.id);
      localStorage.setItem('basket', JSON.stringify(basket));
    } else {
      throw new Error('Item has to exist !');
    }
  }
  getNumberOfATire(t_tire: ITire): number {
    const basket: TireContainer[] = this.getContent();
    if (basket.length > 0) {
      const item: any = basket.find(x => x.tire === t_tire);
      if (item !== undefined) {
        const index: number = basket.indexOf(item);
        return basket[index].count;
      }
      return 0;
    } else {
      throw new Error('Item has to exist !');
    }
  }
  // Efface tous les pneus du panier
  wipe(): void {
    localStorage.setItem('basket', JSON.stringify('{}'));
  }
}
