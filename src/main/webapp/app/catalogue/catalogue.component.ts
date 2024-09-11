import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { TireService } from 'app/entities/tire/service/tire.service';
import { ITire } from '../entities/tire/tire.model';
import { HttpResponse } from '@angular/common/http';
import { DetailComponent } from 'app/detail/detail.component';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'jhi-catalogue',
  standalone: true,
  imports: [CommonModule, HttpClientModule, DetailComponent, FormsModule],
  templateUrl: './catalogue.component.html',
  styleUrl: './catalogue.component.scss',
})
export class CatalogueComponent {
  tires: ITire[] = [];
  selectedTire: ITire | null = null;
  showModal = false;

  // Variables de pagination
  currentPage = 0;
  itemsPerPage = 3;
  totalItems = 0;

  // Variables de tri
  currentSortField = 'price';
  currentSortDirection = 'asc';

  // Variables de filtrage
  selectedBrand = ''; // Filtre par marque
  priceMin = 0; // Filtre prix minimum
  priceMax = 10000; // Filtre prix maximum

  constructor(private tireService: TireService) {}

  ngOnInit(): void {
    this.loadTires();
  }

  loadTires(): void {
    const params = {
      page: this.currentPage,
      size: this.itemsPerPage,
      sort: `${this.currentSortField},${this.currentSortDirection}`,
      brand: this.selectedBrand, // Filtre par marque
      priceMin: this.priceMin, // Filtre prix minimum
      priceMax: this.priceMax, // Filtre prix maximum
    };

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

  // Méthode appelée lors du changement de tri
  onSortChange(sortField: string, sortDirection: string): void {
    this.currentSortField = sortField;
    this.currentSortDirection = sortDirection;
    this.loadTires();
  }

  // Méthode appelée lors du changement de page
  onPageChange(newPage: number): void {
    this.currentPage = newPage;
    this.loadTires();
  }

  // Méthode appelée lors du changement de filtres
  onFilterChange(): void {
    this.currentPage = 0; // Réinitialiser à la première page après un changement de filtre
    this.loadTires();
  }

  openDetails(tire: ITire): void {
    this.selectedTire = tire;
    this.showModal = true;
  }

  closeModal(): any {
    this.showModal = false;
  }
}
