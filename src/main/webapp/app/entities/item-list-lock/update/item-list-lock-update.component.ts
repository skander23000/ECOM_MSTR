import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITire } from 'app/entities/tire/tire.model';
import { TireService } from 'app/entities/tire/service/tire.service';
import { IItemListLock } from '../item-list-lock.model';
import { ItemListLockService } from '../service/item-list-lock.service';
import { ItemListLockFormGroup, ItemListLockFormService } from './item-list-lock-form.service';

@Component({
  standalone: true,
  selector: 'jhi-item-list-lock-update',
  templateUrl: './item-list-lock-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ItemListLockUpdateComponent implements OnInit {
  isSaving = false;
  itemListLock: IItemListLock | null = null;

  tiresSharedCollection: ITire[] = [];

  protected itemListLockService = inject(ItemListLockService);
  protected itemListLockFormService = inject(ItemListLockFormService);
  protected tireService = inject(TireService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ItemListLockFormGroup = this.itemListLockFormService.createItemListLockFormGroup();

  compareTire = (o1: ITire | null, o2: ITire | null): boolean => this.tireService.compareTire(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemListLock }) => {
      this.itemListLock = itemListLock;
      if (itemListLock) {
        this.updateForm(itemListLock);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const itemListLock = this.itemListLockFormService.getItemListLock(this.editForm);
    if (itemListLock.id !== null) {
      this.subscribeToSaveResponse(this.itemListLockService.update(itemListLock));
    } else {
      this.subscribeToSaveResponse(this.itemListLockService.create(itemListLock));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItemListLock>>): void {
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

  protected updateForm(itemListLock: IItemListLock): void {
    this.itemListLock = itemListLock;
    this.itemListLockFormService.resetForm(this.editForm, itemListLock);

    this.tiresSharedCollection = this.tireService.addTireToCollectionIfMissing<ITire>(this.tiresSharedCollection, itemListLock.tire);
  }

  protected loadRelationshipsOptions(): void {
    this.tireService
      .query()
      .pipe(map((res: HttpResponse<ITire[]>) => res.body ?? []))
      .pipe(map((tires: ITire[]) => this.tireService.addTireToCollectionIfMissing<ITire>(tires, this.itemListLock?.tire)))
      .subscribe((tires: ITire[]) => (this.tiresSharedCollection = tires));
  }
}
