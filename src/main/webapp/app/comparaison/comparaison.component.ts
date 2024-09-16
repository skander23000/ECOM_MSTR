import { Component } from '@angular/core';
import { ITire } from '../entities/tire/tire.model';
import { TireService } from '../entities/tire/service/tire.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TireImageComponent } from '../image/image.component';

@Component({
  selector: 'jhi-comparaison',
  standalone: true,
  imports: [CommonModule, FormsModule, TireImageComponent],
  templateUrl: './comparaison.component.html',
  styleUrl: './comparaison.component.scss',
})
export class ComparaisonComponent {
  tires: ITire[] = [];
  selectedTire1: ITire | null = null;
  selectedTire2: ITire | null = null;

  constructor(protected tireService: TireService) {}

  ngOnInit(): void {
    this.loadAllTires();
  }

  loadAllTires(): void {
    this.tireService.query().subscribe({
      next: res => {
        if (res.body) {
          this.tires = res.body;
        }
      },
      error() {
        console.error('Failed to load tires');
      },
    });
  }

  compareReady(): boolean {
    return this.selectedTire1 !== null && this.selectedTire2 !== null;
  }
}
