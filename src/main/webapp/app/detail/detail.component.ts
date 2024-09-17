import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ITire } from 'app/entities/tire/tire.model';
import { FormsModule } from '@angular/forms';
import { BasketService } from '../basket.service';
import { GetIconsService } from '../shared/get-icons.service';
import { SharedUserDataService } from '../shared/shared-user-data.service';
import { TireImageComponent } from '../image/image.component';
import { FrontTimerService } from '../shared/front-timer.service';

@Component({
  selector: 'jhi-detail',
  standalone: true,
  imports: [FormsModule, TireImageComponent],
  templateUrl: './detail.component.html',
  styleUrl: './detail.component.scss',
})
export class DetailComponent {
  @Input() tire: ITire | null = null;
  @Output() closeModal = new EventEmitter<void>();
  @Output() detailError = new EventEmitter<string>();
  quantity = 1;

  constructor(
    private basketService: BasketService,
    private iconService: GetIconsService,
    private sharedDataService: SharedUserDataService,
    private timerService: FrontTimerService,
  ) {}

  decreaseQuantity(): void {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }
  onAddToCart(tire: ITire | null): void {
    if (tire === null) {
      return;
    }
    this.basketService.addTire(tire, this.quantity).subscribe({
      next: () => {
        this.sharedDataService.setSuccessMessageProduct(true);
        this.close();
      },
      error: (err: string) => {
        const err_split = err.split('|');
        if (err_split[0] === '102') {
          this.timerService.setTimer(1);
        } else {
          this.detailError.emit('Pas assez de pneu en stock');
        }
        this.close();
      },
    });
  }

  increaseQuantity(): void {
    this.quantity++;
  }
  close(): any {
    this.closeModal.emit();
  }

  getIcon(): string {
    const type = this.tire?.tireType;
    if (type) {
      return this.iconService.processItem(type);
    }
    return '';
  }
}
