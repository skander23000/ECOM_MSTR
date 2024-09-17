import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NgIf } from '@angular/common';

@Component({
  selector: 'jhi-pop-up',
  standalone: true,
  imports: [NgIf],
  templateUrl: './pop-up.component.html',
  styleUrl: './pop-up.component.scss',
})
export class PopUpComponent {
  @Input() cancellable = true;
  @Input() title = 'Attention';
  @Input() message = '';
  @Output() Confirm = new EventEmitter<void>();
  @Output() Cancel = new EventEmitter<void>();

  // Méthode appelée lors de la validation
  confirm(): void {
    this.Confirm.emit();
  }

  // Méthode appelée lors de l'annulation
  cancel(): void {
    this.Cancel.emit();
  }
}
