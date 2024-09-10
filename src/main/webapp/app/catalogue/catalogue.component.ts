import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { TireService } from 'app/entities/tire/service/tire.service';
import { ITire } from '../entities/tire/tire.model';
import { HttpResponse } from '@angular/common/http';
import { DetailComponent } from 'app/detail/detail.component';

@Component({
  selector: 'jhi-catalogue',
  standalone: true,
  imports: [CommonModule, HttpClientModule, DetailComponent],
  templateUrl: './catalogue.component.html',
  styleUrl: './catalogue.component.scss',
})
export class CatalogueComponent {
  tires: any[] = [];
  selectedTire: ITire | null = null;
  showModal = false;
  constructor(private tireService: TireService) {}
  ngOnInit(): void {
    this.tireService.query().subscribe(
      (res: HttpResponse<ITire[]>) => {
        this.tires = res.body ?? [];
      },
      (error: any) => {
        console.error('Erreur lors du chargement des produits', error);
      },
    );
  }
  openDetails(tire: ITire): void {
    this.selectedTire = tire;
    this.showModal = true;
  }

  closeModal(): any {
    this.showModal = false;
  }
}
