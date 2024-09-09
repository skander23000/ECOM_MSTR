import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IItemListLock } from '../item-list-lock.model';
import { ItemListLockService } from '../service/item-list-lock.service';

@Component({
  standalone: true,
  templateUrl: './item-list-lock-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ItemListLockDeleteDialogComponent {
  itemListLock?: IItemListLock;

  protected itemListLockService = inject(ItemListLockService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.itemListLockService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
