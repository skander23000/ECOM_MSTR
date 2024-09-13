import { Component, OnInit } from '@angular/core';
import { TireContainer } from '../entities/entity.tire-container';
import TranslateDirective from '../shared/language/translate.directive';
import { CartItemComponent } from '../cart-item/cart-item.component';
import { NgOptimizedImage } from '@angular/common';
import { BasketService } from '../entities/basket.service';

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
  basketService: BasketService;

  // Exemple d'appel de la fonction avec un JSON en tant que chaîne
  jsonData = `
[
  {
    "tire": {
      "id": 1,
      "reference": "123ABC",
      "name": "Tire A",
      "price": 99.99,
      "tireWidth": "205",
      "tireHeight": "55",
      "tireDiameter": "16",
      "tireType": "SUMMER",
      "imageUrl": "https://example.com/tireA.jpg",
      "speedIndex": "V",
      "weightIndex": "95",
      "quantity": 10,
      "disable": false,
      "disableReason": null,
      "description": "High-performance summer tire",
      "tireBrand": {
        "id": 1,
        "name": "Brand A"
      }
    },
    "count": 5
  },
  {
    "tire": {
      "id": 2,
      "reference": "456DEF",
      "name": "Tire B",
      "price": 79.99,
      "tireWidth": "195",
      "tireHeight": "60",
      "tireDiameter": "15",
      "tireType": "WINTER",
      "imageUrl": "https://example.com/tireB.jpg",
      "speedIndex": "T",
      "weightIndex": "91",
      "quantity": 20,
      "disable": true,
      "disableReason": "Out of stock",
      "description": "Durable winter tire",
      "tireBrand": {
        "id": 2,
        "name": "Brand B"
      }
    },
    "count": 3
  }
]`;

  constructor(basketService: BasketService) {
    this.basketService = basketService;
  }

  // [TODO] RETIRER TOUTE EXISTENCE DE CETTE FONCTION POUR LE CODE FINAL
  // Fonction pour lire le fichier JSON
  readTireDataFromFile(): TireContainer[] {
    try {
      // [TODO] Retirer le fichier mock.json
      return JSON.parse(this.jsonData) as TireContainer[];
    } catch (error) {
      console.error('Error reading or parsing JSON file:', error);
      return [];
    }
  }
  // [TODO] RETIRER TOUTE EXISTENCE DE LA FONCTION CI DESSUS POUR LE CODE FINAL

  ngOnInit(): void {
    // this.cart_items = this.basketService.getContent();
    this.cart_items = this.readTireDataFromFile();
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
