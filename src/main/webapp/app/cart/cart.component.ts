import { Component, OnInit } from '@angular/core';
import { TireContainer } from '../entities/entity.tire-container';
import TranslateDirective from '../shared/language/translate.directive';
import { CartItemComponent } from '../cart-item/cart-item.component';

@Component({
  selector: 'jhi-cart',
  standalone: true,
  imports: [TranslateDirective, CartItemComponent],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss',
})
export class CartComponent implements OnInit {
  cart_items: TireContainer[] = [];

  ngOnInit(): void {
    // [TODO] Récupérer les éléments du panier via le service
    this.cart_items = [];
  }
}
