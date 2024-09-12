import { Component, Input } from '@angular/core';
import { TireContainer } from '../entities/entity.tire-container';

@Component({
  selector: 'jhi-cart-item',
  standalone: true,
  imports: [],
  templateUrl: './cart-item.component.html',
  styleUrl: './cart-item.component.scss',
})
export class CartItemComponent {
  @Input() cart_item: TireContainer = {};
}
