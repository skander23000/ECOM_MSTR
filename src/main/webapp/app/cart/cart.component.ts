import { Component, OnInit } from '@angular/core';
import { TireContainer } from '../entities/entity.tire-container';
import TranslateDirective from '../shared/language/translate.directive';
import { CartItemComponent } from '../cart-item/cart-item.component';
import { NgOptimizedImage } from '@angular/common';

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

  ngOnInit(): void {
    // [TODO] Récupérer les éléments du panier via le service de Théo
    this.cart_items = [];
  }

  isCartEmpty(): boolean {
    return this.cart_items.length !== 0;
  }

  goToCheckout(): void {
    // [TODO] [ROUTAGE] Rediriger vers le formulaire d'adresse et de contact
  }

  goToHome(): void {
    // [TODO] [ROUTAGE] Rediriger vers la page d'accueil
  }

  // Assurez-vous que cette méthode est dans le composant correspondant
  confirmAndEmptyCart(): void {
    if (confirm('Êtes-vous sûr de vouloir vider le panier ?')) {
      this.emptyCart();
    }
  }

  // On vide le panier quand on appuie dessus
  protected emptyCart(): void {
    // [TODO] Vider le panier
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
