import { Component, Input } from '@angular/core';
import TranslateDirective from '../shared/language/translate.directive';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'jhi-my-header',
  standalone: true,
  imports: [TranslateDirective, RouterLink],
  templateUrl: './my-header.component.html',
  styleUrls: ['./my-header.component.scss'],
})
export class MyHeaderComponent {
  @Input() cartItemCount = 0; // Variable pour representer le nombre d articles dans le panier
}
