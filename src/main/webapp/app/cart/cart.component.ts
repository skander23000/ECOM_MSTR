import { Component, OnInit } from '@angular/core';
import { TireContainer } from '../entities/entity.tire-container';
import TranslateDirective from '../shared/language/translate.directive';
import { CartItemComponent } from '../cart-item/cart-item.component';
import { NgOptimizedImage } from '@angular/common';
import { Router } from '@angular/router';
import { BasketService } from '../basket.service';

@Component({
  selector: 'jhi-cart',
  standalone: true,
  imports: [TranslateDirective, CartItemComponent, NgOptimizedImage],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss',
})
export class CartComponent implements OnInit {
  cart_items: TireContainer[] = [];
  totalPrice = 0;

  constructor(
    private router: Router,
    private basketService: BasketService,
  ) {}

  ngOnInit(): void {
    this.cart_items = this.basketService.getContent();
    this.updateTotalPrice();
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
    this.router.navigate(['/informations']);
  }

  goToHome(): void {
    this.router.navigate(['/']);
  }

  // Assurez-vous que cette méthode est dans le composant correspondant
  confirmAndEmptyCart(): void {
    if (confirm('Êtes-vous sûr de vouloir vider le panier ?')) {
      this.emptyCart();
    }
  }

  // On vide le panier quand on appuie dessus
  protected emptyCart(): void {
    this.basketService.wipe();
    this.cart_items = [];
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
