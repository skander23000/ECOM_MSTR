import { Component, OnDestroy, OnInit } from '@angular/core';
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
import { TireImageComponent } from 'app/image/image.component';
import { GetIconsService } from '../shared/get-icons.service';
import { FrontTimerService } from '../shared/front-timer.service';
import TranslateDirective from '../shared/language/translate.directive';
import { PopUpComponent } from '../pop-up/pop-up.component';

@Component({
  selector: 'jhi-catalogue',
  standalone: true,
  imports: [
    CommonModule,
    HttpClientModule,
    DetailComponent,
    FormsModule,
    NgxSliderModule,
    TruncatePipe,
    TireImageComponent,
    TranslateDirective,
    PopUpComponent,
  ],
  templateUrl: './catalogue.component.html',
  styleUrl: './catalogue.component.scss',
})
export class CatalogueComponent implements OnInit, OnDestroy {
  tires: ITire[] = [];
  selectedTire: ITire | null = null;
  showModal = false;

  // Variables de pagination
  currentPage = 0;
  itemsPerPage = 6;
  totalItems = 0;

  // Variables de tri
  currentSortField = 'price';
  currentSortDirection = 'asc';

  // Variables de filtrage
  tireType = '';
  priceMin = 0;
  priceMax = 300;

  // Variable de recherche
  searchQuery = '';
  // Variable d'affichage du message de succès
  showSuccessMessage: boolean | null = false;
  showSuccessProductMessage: boolean | null = false;
  isPopupVisible = false;
  errorMessage = '';
  errorTitle = 'Erreur';

  sliderOptions: Options = {
    floor: 0,
    ceil: 300,
    step: 5,
    translate(value: number) {
      return `${value} + €`;
    },
  };

  constructor(
    private tireService: TireService,
    private sharedDataService: SharedUserDataService,
    private viewportScroller: ViewportScroller,
    private basketService: BasketService,
    private iconService: GetIconsService,
    protected timerService: FrontTimerService,
  ) {}

  ngOnInit(): void {
    document.addEventListener('keydown', this.handleKeyboardEvent.bind(this));
    // Si le minuteur n'est pas initialisé, démarrer le timer
    if (!this.timerService.getIsInitialized()) {
      this.timerService.startTimer();
    }
    // S'abonner à l'événement de fin du minuteur et mettre à jour l'état
    this.timerService.getTimerComplete().subscribe(() => {
      this.timerService.setShowTimerError(true); // Sauvegarder l'état de showTimerError dans le service
    });

    // Charger la liste des pneus
    this.loadTires();

    // S'abonner à la variable successInfo pour afficher un message de succès
    this.sharedDataService.successInfo$.subscribe(data => {
      this.viewportScroller.scrollToPosition([0, 0]);
      this.showSuccessMessage = data;
    });
    // S'abonner à la variable successInfoProduct pour afficher un message de succès produit
    this.sharedDataService.successInfoProduct$.subscribe(data => {
      this.viewportScroller.scrollToPosition([0, 0]);
      this.showSuccessProductMessage = data;
    });
  }

  loadTires(): void {
    // On relance le timer
    this.timerService.addActivity();

    const params: any = {
      page: this.currentPage,
      size: this.itemsPerPage,
      sort: `${this.currentSortField},${this.currentSortDirection}`,
    };

    if (this.tireType) {
      params['tireType.equals'] = this.tireType;
    }

    if (this.priceMin > 0) {
      params['price.greaterThanOrEqual'] = this.priceMin;
    }

    if (this.priceMax) {
      params['price.lessThanOrEqual'] = this.priceMax;
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
    this.showSuccessMessage = false;
    this.sharedDataService.setSuccessMessage(false);
  }
  closeSuccessProductMessage(): void {
    this.showSuccessProductMessage = false;
    this.sharedDataService.setSuccessMessageProduct(false);
  }
  closePopup(): void {
    this.isPopupVisible = false;
  }

  onAddToCart(tire: ITire): void {
    this.timerService.addActivity();
    this.basketService.addTire(tire).subscribe({
      next: () => {
        this.showSuccessProductMessage = true;
      },
      error: (err: string) => {
        const err_split = err.split('|');
        if (err_split[0] === '102') {
          this.timerService.setTimer(1);
        } else {
          this.errorMessage = 'Pas assez de pneu en stock';
          this.isPopupVisible = true;
        }
      },
    });
  }
  treatError(err: string): void {
    this.errorMessage = err;
    this.isPopupVisible = true;
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

  getIcon(tire: ITire): string {
    const type = tire.tireType;
    if (type) {
      return this.iconService.processItem(type);
    }
    return '';
  }

  closeTimerError(): void {
    this.timerService.setShowTimerError(false);
  }
  handleKeyboardEvent(event: KeyboardEvent): void {
    if (event.key === 'Escape' && this.showModal) {
      this.closeModal();
    }
  }
  ngOnDestroy(): void {
    document.removeEventListener('keydown', this.handleKeyboardEvent.bind(this));
  }
}
