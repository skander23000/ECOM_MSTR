import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ITire } from 'app/entities/tire/tire.model';
import { BasketService } from '../basket.service';
import { FormsModule } from '@angular/forms';

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

  constructor(private basket: BasketService) {}

  decreaseQuantity(tire: ITire | null): void {
    if (this.quantity > 1) {
      this.quantity--;
    }
    if (tire)
      this.basket.removeATire(tire).subscribe({
        next(value) {
          // eslint-disable-next-line no-console
          console.log('Success');
        },
        error(err) {
          // eslint-disable-next-line no-console
          console.log('Failed');
        },
      });
  }

  setvalue(tire: ITire | null): void {
    if (Number(this.quantity) && tire) {
      this.basket.setTire(tire, Number(this.quantity)).subscribe({
        next(value) {
          // eslint-disable-next-line no-console
          console.log('__');
        },
        error(err) {
          // eslint-disable-next-line no-console
          console.log('__');
        },
      });
    }
  }

  increaseQuantity(tire: ITire | null): void {
    this.quantity++;
    if (tire)
      this.basket.addTire(tire).subscribe({
        next(value) {
          // eslint-disable-next-line no-console
          console.log('__');
        },
        error(err) {
          // eslint-disable-next-line no-console
          console.log('__');
        },
      });
  }
  close(): any {
    this.closeModal.emit();
  }
}
