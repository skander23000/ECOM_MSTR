import { Component } from '@angular/core';
import TranslateDirective from '../shared/language/translate.directive';

@Component({
  selector: 'jhi-credits',
  standalone: true,
  imports: [TranslateDirective],
  templateUrl: './credits.component.html',
  styleUrl: './credits.component.scss',
})
export class CreditsComponent {}
