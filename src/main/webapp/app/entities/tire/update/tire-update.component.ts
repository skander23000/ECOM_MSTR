import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITireBrand } from 'app/entities/tire-brand/tire-brand.model';
import { TireBrandService } from 'app/entities/tire-brand/service/tire-brand.service';
import { TireType } from 'app/entities/enumerations/tire-type.model';
import { SpeedIndex } from 'app/entities/enumerations/speed-index.model';
import { ChargeIndex } from 'app/entities/enumerations/charge-index.model';
import { TireService } from '../service/tire.service';
import { ITire } from '../tire.model';
import { TireFormGroup, TireFormService } from './tire-form.service';
import { S3Service } from '../../../s3.service';

@Component({
  standalone: true,
  selector: 'jhi-tire-update',
  templateUrl: './tire-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TireUpdateComponent implements OnInit {
  isSaving = false;
  tire: ITire | null = null;
  tireTypeValues = Object.keys(TireType);
  speedIndexValues = Object.keys(SpeedIndex);
  chargeIndexValues = Object.keys(ChargeIndex);

  tireBrandsSharedCollection: ITireBrand[] = [];

  protected tireService = inject(TireService);
  protected s3Service: S3Service = inject(S3Service);
  protected tireFormService = inject(TireFormService);
  protected tireBrandService = inject(TireBrandService);
  protected activatedRoute = inject(ActivatedRoute);
  private file: File | null = null;

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TireFormGroup = this.tireFormService.createTireFormGroup();

  compareTireBrand = (o1: ITireBrand | null, o2: ITireBrand | null): boolean => this.tireBrandService.compareTireBrand(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tire }) => {
      this.tire = tire;
      if (this.tire?.imageUrl) {
        this.tire.imageUrl = '';
      }
      if (tire) {
        this.updateForm(tire);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  public handleFileInput(event: Event): void {
    const element = event.currentTarget as HTMLInputElement;
    const fileList: FileList | null = element.files;

    if (fileList) {
      this.file = fileList[0];
    } else {
      this.file = null;
    }
  }
  save(): void {
    this.isSaving = true;
    const tire = this.tireFormService.getTire(this.editForm);
    if (this.file) {
      this.s3Service.uploadImage(this.file);
    }

    if (tire.id !== null) {
      this.subscribeToSaveResponse(this.tireService.update(tire));
    } else {
      this.subscribeToSaveResponse(this.tireService.create(tire));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITire>>): void {
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

  protected updateForm(tire: ITire): void {
    this.tire = tire;
    this.tireFormService.resetForm(this.editForm, tire);

    this.tireBrandsSharedCollection = this.tireBrandService.addTireBrandToCollectionIfMissing<ITireBrand>(
      this.tireBrandsSharedCollection,
      tire.tireBrand,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tireBrandService
      .query()
      .pipe(map((res: HttpResponse<ITireBrand[]>) => res.body ?? []))
      .pipe(
        map((tireBrands: ITireBrand[]) =>
          this.tireBrandService.addTireBrandToCollectionIfMissing<ITireBrand>(tireBrands, this.tire?.tireBrand),
        ),
      )
      .subscribe((tireBrands: ITireBrand[]) => (this.tireBrandsSharedCollection = tireBrands));
  }
}
