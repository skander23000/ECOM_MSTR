import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ITire } from '../tire.model';

@Component({
  standalone: true,
  selector: 'jhi-tire-detail',
  templateUrl: './tire-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TireDetailComponent {
  tire = input<ITire | null>(null);

  previousState(): void {
    window.history.back();
  }
}
