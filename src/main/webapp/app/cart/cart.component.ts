import { Component, OnInit } from '@angular/core';
import { TireContainer } from '../entities/entity.tire-container';
import TranslateDirective from '../shared/language/translate.directive';
import { CartItemComponent } from '../cart-item/cart-item.component';
import { NgIf, NgOptimizedImage } from '@angular/common';
import { Router } from '@angular/router';
import { BasketService } from '../basket.service';
import { FrontTimerService } from '../shared/front-timer.service';

@Component({
  selector: 'jhi-cart',
  standalone: true,
  imports: [TranslateDirective, CartItemComponent, NgOptimizedImage, NgIf],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss',
})
export class CartComponent implements OnInit {
  cart_items: TireContainer[] = [];
  subscription: any;
  totalPrice = 0;
  showItemError = false;
  errorMessage = '';

  constructor(
    private router: Router,
    private basketService: BasketService,
    private timerService: FrontTimerService,
  ) {}

  ngOnInit(): void {
    // On relance le timer
    this.timerService.addActivity();

    this.subscription = this.basketService.getObservableContent().subscribe({
      next: (content: TireContainer[]) => {
        this.cart_items = content;
        this.updateTotalPrice();
      },
    });
  }

  updateTireItemCount($event: { id: number; count: number }): void {
    this.cart_items.forEach(item => {
      if (item.tire && item.tire.id === $event.id) {
        item.count = $event.count;
      }
    });
    this.updateTotalPrice();
  }

  // Supprime un pneu du panier
  destroyItem($event: number): void {
    this.cart_items = this.cart_items.filter(item => item.tire?.id !== $event);
    this.updateTotalPrice();
  }

  updateTotalPrice(): void {
    let som = 0;
    // On relance le timer
    this.timerService.addActivity();

    this.cart_items.forEach((item: TireContainer) => {
      if (item.tire?.price && item.count) {
        som += item.tire.price * item.count;
      }
    });
    this.totalPrice = parseFloat(som.toFixed(2));
  }

  isCartEmpty(): boolean {
    return this.cart_items.length === 0;
  }

  goToCheckout(): void {
    // On relance le timer
    this.timerService.resetTimer();
    this.router.navigate(['/informations']);
  }

  goToHome(): void {
    // On relance le timer
    this.timerService.addActivity();
    this.router.navigate(['/']);
  }

  // Assurez-vous que cette méthode est dans le composant correspondant
  confirmAndEmptyCart(): void {
    if (confirm('Êtes-vous sûr de vouloir vider le panier ?')) {
      this.emptyCart();
    }
  }

  showError(msg: string){
    this.errorMessage = msg;
    this.showItemError = true;
  }

  hideError(){
    this.showItemError = false;
  }

  // On vide le panier quand on appuie dessus
  protected emptyCart(): void {
    // On relance le timer
    this.timerService.addActivity();

    this.basketService.wipe().subscribe({
      next: () => {
        this.cart_items = [];
      },
      error: (err: string) => {
        const err_split = err.split('|')
        if (err_split[0] === '102') {
          this.timerService.setTimer(1);
        } else {
          this.showError("Impossible de vider le panier");
        }
      }
    });
  }

  // On récupère le prix total dans le cas ou on voudrait l'afficher
  protected computeTotalPrice(): void {
    this.totalPrice = 0;
    this.cart_items.forEach(item => {
      if (item.tire?.price) {
        this.totalPrice += item.tire.price;
      }
    });
  }
}
