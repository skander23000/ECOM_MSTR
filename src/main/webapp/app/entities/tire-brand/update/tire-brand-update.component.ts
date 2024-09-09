import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITireBrand } from '../tire-brand.model';
import { TireBrandService } from '../service/tire-brand.service';
import { TireBrandFormGroup, TireBrandFormService } from './tire-brand-form.service';

@Component({
  standalone: true,
  selector: 'jhi-tire-brand-update',
  templateUrl: './tire-brand-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TireBrandUpdateComponent implements OnInit {
  isSaving = false;
  tireBrand: ITireBrand | null = null;

  protected tireBrandService = inject(TireBrandService);
  protected tireBrandFormService = inject(TireBrandFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TireBrandFormGroup = this.tireBrandFormService.createTireBrandFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tireBrand }) => {
      this.tireBrand = tireBrand;
      if (tireBrand) {
        this.updateForm(tireBrand);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tireBrand = this.tireBrandFormService.getTireBrand(this.editForm);
    if (tireBrand.id !== null) {
      this.subscribeToSaveResponse(this.tireBrandService.update(tireBrand));
    } else {
      this.subscribeToSaveResponse(this.tireBrandService.create(tireBrand));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITireBrand>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(tireBrand: ITireBrand): void {
    this.tireBrand = tireBrand;
    this.tireBrandFormService.resetForm(this.editForm, tireBrand);
  }
}
