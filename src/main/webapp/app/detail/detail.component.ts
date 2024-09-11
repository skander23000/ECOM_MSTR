import { Component, Input, Output, EventEmitter } from '@angular/core';
import { ITire } from 'app/entities/tire/tire.model';
import { BasketService } from 'app/basket.service';

@Component({
  selector: 'jhi-detail',
  standalone: true,
  imports: [],
  templateUrl: './detail.component.html',
  styleUrl: './detail.component.scss',
})
export class DetailComponent {
  @Input() tire: ITire | null = null;
  @Output() closeModal = new EventEmitter<void>();
  quantity = 0;
  constructor(private basket: BasketService) {}

  removeATire(tire: ITire | null): void {
    if (!tire) {
      return;
    }
    this.basket.removeTires(tire);
  }
  addTire(tire: ITire | null): void {
    if (!tire) {
      return;
    }
    this.basket.addTire(tire);
  }
  setTire(tire: ITire | null, count: number): void {
    if (!tire) {
      return;
    }
    this.basket.setTire(tire, count);
  }
  close(): any {
    this.closeModal.emit();
  }
}
