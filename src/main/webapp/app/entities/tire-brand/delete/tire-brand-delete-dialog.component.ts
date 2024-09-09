import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITireBrand } from '../tire-brand.model';
import { TireBrandService } from '../service/tire-brand.service';

@Component({
  standalone: true,
  templateUrl: './tire-brand-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TireBrandDeleteDialogComponent {
  tireBrand?: ITireBrand;

  protected tireBrandService = inject(TireBrandService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tireBrandService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
