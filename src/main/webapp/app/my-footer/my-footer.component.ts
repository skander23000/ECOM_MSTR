import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import TranslateDirective from '../shared/language/translate.directive';

@Component({
  selector: 'jhi-my-footer',
  standalone: true,
  imports: [RouterModule, TranslateDirective],
  templateUrl: './my-footer.component.html',
  styleUrl: './my-footer.component.scss',
})
export class MyFooterComponent {}
