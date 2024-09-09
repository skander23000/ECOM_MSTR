import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITire } from '../tire.model';
import { TireService } from '../service/tire.service';

@Component({
  standalone: true,
  templateUrl: './tire-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TireDeleteDialogComponent {
  tire?: ITire;

  protected tireService = inject(TireService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tireService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
