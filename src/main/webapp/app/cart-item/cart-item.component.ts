import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { TireContainer } from '../entities/entity.tire-container';
import { FormsModule } from '@angular/forms';
import { NgOptimizedImage } from '@angular/common';
import TranslateDirective from '../shared/language/translate.directive';
import { GetIconsService } from '../shared/get-icons.service';

@Component({
  selector: 'jhi-cart-item',
  standalone: true,
  imports: [FormsModule, NgOptimizedImage, TranslateDirective],
  templateUrl: './cart-item.component.html',
  styleUrl: './cart-item.component.scss',
})
export class CartItemComponent implements OnInit {
  @Input() cart_item: TireContainer = {};
  @Output() countChanged = new EventEmitter<{ id: number; count: number }>();
  @Output() destroy = new EventEmitter<number>();
  total_price = 0;
  isAvailable = true;
  getIconservice: GetIconsService;

  constructor(getIconservice: GetIconsService) {
    this.getIconservice = getIconservice;
  }

  updateTotalPrice(): void {
    if (this.cart_item.count && this.cart_item.tire?.price) {
      this.total_price = this.cart_item.tire.price * this.cart_item.count;
      this.total_price = parseFloat(this.total_price.toFixed(2));
      this.countChanged.emit({ id: this.cart_item.tire.id, count: this.cart_item.count });
    }
  }

  // Récupère l'icone du type de pneu correspondant
  getIcon(): string {
    const type = this.cart_item.tire?.tireType;
    if (type) {
      return this.getIconservice.processItem(type);
    }
    return '';
  }

  ngOnInit(): void {
    this.updateTotalPrice();
  }

  decreaseQuantity(): void {
    this.isAvailable = false;
    if (!this.cart_item.count) {
      this.isAvailable = true;
      return;
    }
    if (this.cart_item.count > 1) {
      this.cart_item.count--;
      this.updateTotalPrice();
    }
    // [TODO] Ajouter la logique concernant l'ajout d'un pneu au panier

    this.isAvailable = true;
  }

  increaseQuantity(): void {
    this.isAvailable = false;
    if (!this.cart_item.count) {
      this.isAvailable = true;
      return;
    }
    this.cart_item.count++;
    this.updateTotalPrice();
    // [TODO] Ajouter la logique concernant l'ajout d'un pneu au panier
    this.isAvailable = true;
  }

  onCountChange(): void {
    this.isAvailable = false;
    if (!this.cart_item.count) {
      this.cart_item.count = 1;
      this.updateTotalPrice();
      this.isAvailable = true;
      return;
    }

    const countValue = parseFloat(String(this.cart_item.count));

    if (isNaN(countValue) || countValue <= 0) {
      // Réinitialiser à 1 si la valeur est non numérique ou inférieure ou égale à 0
      this.cart_item.count = 1;
    } else {
      // Sinon, utilisez la valeur validée
      this.cart_item.count = countValue;
    }
    this.updateTotalPrice();
    this.isAvailable = true;
  }

  onDestroy(): void {
    if (!confirm('Voulez vous vraiment retirer ce pneu de votre panier ?')) {
      return;
    }
    if (this.cart_item.tire?.id) {
      this.destroy.emit(this.cart_item.tire.id);
    }
  }
}
