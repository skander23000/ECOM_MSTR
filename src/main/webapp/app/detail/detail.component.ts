import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ITire } from 'app/entities/tire/tire.model';
import { FormsModule } from '@angular/forms';
import { BasketService } from '../basket.service';
import { GetIconsService } from '../shared/get-icons.service';
import { SharedUserDataService } from '../shared/shared-user-data.service';
import { S3Service } from '../s3.service';
import { TireImageComponent } from '../image/image.component';

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
  quantity = 1;

  constructor(
    private basketService: BasketService,
    private iconService: GetIconsService,
    private sharedDataService: SharedUserDataService,
    private s3: S3Service,
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
    if (this.quantity + this.basketService.getNumberOfATire(tire) > 9) {
      this.sharedDataService.setErrorMessage(true);
      return;
    }
    this.basketService.addTire(tire, this.quantity).subscribe();
    this.sharedDataService.setSuccessMessageProduct(true);
    this.close();
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
