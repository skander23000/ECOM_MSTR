import { Component, OnInit } from '@angular/core';
import TranslateDirective from '../shared/language/translate.directive';
import { RouterLink } from '@angular/router';
import { BasketService } from '../basket.service';

@Component({
  selector: 'jhi-my-header',
  standalone: true,
  imports: [TranslateDirective, RouterLink],
  templateUrl: './my-header.component.html',
  styleUrls: ['./my-header.component.scss'],
})
export class MyHeaderComponent implements OnInit {
  totalItems: number | null = 0;
  constructor(private basketService: BasketService) {}
  ngOnInit(): void {
    // S'abonner au total des items
    this.basketService.totalItems$.subscribe(total => {
      this.totalItems = total;
    });
  }
}
