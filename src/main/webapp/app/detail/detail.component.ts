import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ITire } from 'app/entities/tire/tire.model';
import { FormsModule } from '@angular/forms';
import { BasketService } from '../basket.service';

@Component({
  selector: 'jhi-detail',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './detail.component.html',
  styleUrl: './detail.component.scss',
})
export class DetailComponent {
  @Input() tire: ITire | null = null;
  @Output() closeModal = new EventEmitter<void>();
  quantity = 1;

  constructor(private basketService: BasketService) {}

  decreaseQuantity(): void {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }
  onAddToCart(tire: ITire | null): void {
    if (tire === null) {
      return;
    }
    this.basketService.addTire(tire, this.quantity).subscribe();
  }

  increaseQuantity(): void {
    this.quantity++;
  }
  close(): any {
    this.closeModal.emit();
  }
}
