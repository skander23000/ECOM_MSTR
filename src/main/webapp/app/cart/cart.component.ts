import { Component, OnInit } from '@angular/core';
import { TireContainer } from '../entities/entity.tire-container';
import TranslateDirective from '../shared/language/translate.directive';
import { CartItemComponent } from '../cart-item/cart-item.component';
import { NgIf, NgOptimizedImage } from '@angular/common';
import { Router } from '@angular/router';
import { BasketService } from '../basket.service';
import { FrontTimerService } from '../shared/front-timer.service';
import { PopUpComponent } from '../pop-up/pop-up.component';

@Component({
  selector: 'jhi-cart',
  standalone: true,
  imports: [TranslateDirective, CartItemComponent, NgOptimizedImage, NgIf, PopUpComponent],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss',
})
export class CartComponent implements OnInit {
  cart_items: TireContainer[] = [];
  subscription: any;
  totalPrice = 0;
  errorMessage = '';
  isPopupVisible = false;
  errorTitle = 'Attention';
  popUpCancellable = true;
  isError = false;

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

  confirmAction(): void {
    if (this.isError) {
      this.isError = false;
    } else {
      this.emptyCart();
    }
    this.hideError();
  }

  cancelAction(): void {
    this.hideError();
  }

  // Assurez-vous que cette méthode est dans le composant correspondant
  validateEmptyCart(): void {
    this.errorMessage = 'Êtes-vous sûr de vouloir vider le panier ?';
    this.isPopupVisible = true;
  }

  showError(msg: string): void {
    this.errorMessage = msg;
    this.errorTitle = 'Erreur';
    this.popUpCancellable = false;
    this.isError = true;
    this.isPopupVisible = true;
  }

  hideError(): void {
    this.isPopupVisible = false;
  }

  // On vide le panier quand on appuie dessus
  protected emptyCart(): void {
    // On relance le timer
    this.timerService.addActivity();

    this.basketService.wipe().subscribe({
      next: () => {
        this.cart_items = [];
      },
    });
  }
}
