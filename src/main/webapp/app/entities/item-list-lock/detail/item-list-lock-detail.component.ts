import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IItemListLock } from '../item-list-lock.model';

@Component({
  standalone: true,
  selector: 'jhi-item-list-lock-detail',
  templateUrl: './item-list-lock-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ItemListLockDetailComponent {
  itemListLock = input<IItemListLock | null>(null);

  previousState(): void {
    window.history.back();
  }
}
