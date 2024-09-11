import { Injectable } from '@angular/core';
import { ITire } from './entities/tire/tire.model';

interface TireContainer {
    tire : ITire
    count : number
}

@Injectable({
  providedIn: 'root'
})
export class BasketService {

      setTire(t_tire : ITire, t_count : number) : void {
          let basket = this.getContent()
          let item = basket.find((x)=>x.tire == t_tire);
          if(item != undefined)
          {
              let index : number = basket.indexOf(item);
              basket[index].count=t_count;
          }
          else
          {
              basket.push({count: t_count, tire: t_tire})
          }
          localStorage.setItem("basket", JSON.stringify(basket));
      }

      addTire(t_tire : ITire) : void {
          let basket = this.getContent()
          let item = basket.find((x)=>x.tire == t_tire);
          if(item != undefined)
          {
              let index : number = basket.indexOf(item);
              basket[index].count+=1;
          }
          else
          {
              basket.push({count: 1, tire: t_tire})
          }
          localStorage.setItem("basket", JSON.stringify(basket));
      }

      getContent(): TireContainer[] {
          const panier = localStorage.getItem("basket");
          return panier ? JSON.parse(panier) : [];
      }

      removeATire(t_tire : ITire) : void {
          let basket = this.getContent();
          let item = basket.find((x)=>x.tire == t_tire);

          if(item !=undefined)
          {
              let index : number = basket.indexOf(item);
              basket[index].count-=1;
              localStorage.setItem("basket", JSON.stringify(basket));
          }
          else
          {
            throw new Error("Item has to exist !");
          }
      }

      removeTires(t_tire : ITire) : void {
        let basket = this.getContent();
        if(basket.filter(p => p.tire.id === t_tire.id))
        {
          basket=basket.filter(p => p.tire.id !== t_tire.id);
          localStorage.setItem("basket", JSON.stringify(basket));
        }
        else
        {
          throw new Error("Item has to exist !");
        }
      }
}
