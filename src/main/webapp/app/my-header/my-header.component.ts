import { Component, Input } from '@angular/core';

@Component({
  selector: 'jhi-my-header',
  standalone: true,
  imports: [],
  templateUrl: './my-header.component.html',
  styleUrls: ['./my-header.component.scss'],
})
export class MyHeaderComponent {
  @Input() cartItemCount = 0; // Variable pour représenter le nombre d'articles dans le panier
}
