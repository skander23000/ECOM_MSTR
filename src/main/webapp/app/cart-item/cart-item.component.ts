import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { TireContainer } from '../entities/entity.tire-container';
import { FormsModule } from '@angular/forms';
import { NgOptimizedImage } from '@angular/common';
import TranslateDirective from '../shared/language/translate.directive';
import { GetIconsService } from '../shared/get-icons.service';
import { BasketService } from '../basket.service';
import { TireImageComponent } from '../image/image.component';
import { FrontTimerService } from '../shared/front-timer.service';

@Component({
  selector: 'jhi-cart-item',
  standalone: true,
  imports: [FormsModule, NgOptimizedImage, TranslateDirective, TireImageComponent],
  templateUrl: './cart-item.component.html',
  styleUrl: './cart-item.component.scss',
})
export class CartItemComponent implements OnInit {
  @Input() cart_item: TireContainer = { tire: null, count: 0 };
  @Output() countChanged = new EventEmitter<{ id: number; count: number }>();
  @Output() destroy = new EventEmitter<number>();
  @Output() itemError = new EventEmitter<string>();
  total_price = 0;
  isAvailable = true;
  getIconservice: GetIconsService;
  basktService: BasketService;
  timerService: FrontTimerService;

  constructor(getIconservice: GetIconsService, basketService: BasketService, timerService: FrontTimerService) {
    this.getIconservice = getIconservice;
    this.basktService = basketService;
    this.timerService = timerService;
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
    if (!this.cart_item.count || !this.cart_item.tire) {
      this.isAvailable = false;
      return;
    }
    this.basktService.removeATire(this.cart_item.tire).subscribe({
      next: () => {
        if (this.cart_item.count && this.cart_item.count > 1) {
          this.cart_item.count--;
          this.updateTotalPrice();
        }
        this.isAvailable = true;
      },
      error: (err: string) => {
        const err_split = err.split('|');
        if (err_split[0] === '102') {
          this.isAvailable = true;
          this.timerService.setTimer(1);
        } else {
          this.isAvailable = true;
          this.itemError.emit('Impossible de retirer ce nombre de pneu du panier');
        }
      },
    });
  }

  increaseQuantity(): void {
    this.isAvailable = false;
    if (!this.cart_item.count || !this.cart_item.tire) {
      this.isAvailable = true;
      return;
    }
    this.basktService.addTire(this.cart_item.tire, 1).subscribe({
      next: () => {
        if (this.cart_item.count) {
          this.cart_item.count++;
          this.updateTotalPrice();
        }
        this.isAvailable = true;
      },
      error: (err: string) => {
        const err_split = err.split('|');
        if (err_split[0] === '102') {
          this.isAvailable = true;
          this.timerService.setTimer(1);
        } else {
          this.isAvailable = true;
          this.itemError.emit("Ce pneu n'est plus disponible en stock");
        }
      },
    });
  }

  onDestroy(): void {
    if (!confirm('Voulez vous vraiment retirer ce pneu de votre panier ?')) {
      return;
    }
    if (this.cart_item.tire?.id) {
      this.basktService.removeTires(this.cart_item.tire).subscribe({
        next: () => {
          if (this.cart_item.tire) {
            this.destroy.emit(this.cart_item.tire.id);
          }
        },
        error: (err: string) => {
          const err_split = err.split('|');
          if (err_split[0] === '102') {
            this.isAvailable = true;
            this.timerService.setTimer(1);
          } else {
            this.isAvailable = true;
            this.itemError.emit('Une erreur est survenue lors de la suppression du pneu du panier');
          }
        },
      });
    }
  }
}
