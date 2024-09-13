import { Component } from '@angular/core';
import { CommonModule, ViewportScroller } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { TireService } from 'app/entities/tire/service/tire.service';
import { ITire } from '../entities/tire/tire.model';
import { HttpResponse } from '@angular/common/http';
import { DetailComponent } from 'app/detail/detail.component';
import { FormsModule } from '@angular/forms';
import { NgxSliderModule, Options } from '@angular-slider/ngx-slider';
import { SharedUserDataService } from '../shared/shared-user-data.service';
import { BasketService } from '../basket.service';
import { TruncatePipe } from '../pipe/truncate.pipe';

@Component({
  selector: 'jhi-catalogue',
  standalone: true,
  imports: [CommonModule, HttpClientModule, DetailComponent, FormsModule, NgxSliderModule, TruncatePipe],
  templateUrl: './catalogue.component.html',
  styleUrl: './catalogue.component.scss',
})
export class CatalogueComponent {
  tires: ITire[] = [];
  selectedTire: ITire | null = null;
  showModal = false;

  // Variables de pagination
  currentPage = 0;
  itemsPerPage = 10;
  totalItems = 0;

  // Variables de tri
  currentSortField = 'price';
  currentSortDirection = 'asc';

  // Variables de filtrage
  tireType = '';
  priceMin = 0;
  priceMax = 50000;

  // Variable de recherche
  searchQuery = '';
  // Variable d'affichage du message de succès
  showSuccessMessage: boolean | null = false;

  sliderOptions: Options = {
    floor: 0,
    ceil: 50000,
    step: 100,
    translate(value: number) {
      return `${value} + €`;
    },
  };

  constructor(
    private tireService: TireService,
    private sharedDataService: SharedUserDataService,
    private viewportScroller: ViewportScroller,
    private basketService: BasketService,
  ) {}

  ngOnInit(): void {
    this.loadTires();
    this.sharedDataService.successInfo$.subscribe(data => {
      this.viewportScroller.scrollToPosition([0, 0]);
      this.showSuccessMessage = data;
    });
  }

  loadTires(): void {
    const params: any = {
      page: this.currentPage,
      size: this.itemsPerPage,
      sort: `${this.currentSortField},${this.currentSortDirection}`,
    };

    if (this.tireType) {
      params['tireType.equals'] = this.tireType;
    }

    if (this.priceMin > 0) {
      params['price.greaterThan'] = this.priceMin;
    }

    if (this.priceMax) {
      params['price.lessThan'] = this.priceMax;
    }

    if (this.searchQuery) {
      params['name.contains'] = this.searchQuery;
    }

    this.tireService.query(params).subscribe(
      (res: HttpResponse<ITire[]>) => {
        this.tires = res.body ?? [];
        const headers = res.headers;
        this.totalItems = parseInt(headers.get('X-Total-Count')!, 10);
      },
      (error: any) => {
        console.error('Erreur lors du chargement des produits', error);
      },
    );
  }

  onSortChange(sortField: string, sortDirection: string): void {
    this.currentSortField = sortField;
    this.currentSortDirection = sortDirection;
    this.loadTires();
  }

  onPageChange(newPage: number): void {
    this.currentPage = newPage;
    this.loadTires();
  }

  onFilterChange(): void {
    this.currentPage = 0;
    this.loadTires();
  }

  onSearchChange(): void {
    this.currentPage = 0;
    this.loadTires();
  }

  openDetails(tire: ITire): void {
    this.selectedTire = tire;
    this.showModal = true;
  }

  closeModal(): any {
    this.showModal = false;
  }
  closeSuccessMessage(): void {
    this.showSuccessMessage = false;
  }
  onAddToCart(tire: ITire): void {
    this.basketService.addTire(tire);
  }
  stopPropagation(event: Event): void {
    event.stopPropagation();
  }
  onResetFilters(): void {
    this.tireType = '';
    this.priceMin = 0;
    this.priceMax = 50000;
    this.searchQuery = '';
    this.loadTires();
  }
}
