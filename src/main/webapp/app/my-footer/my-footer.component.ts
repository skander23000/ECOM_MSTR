import { Component } from '@angular/core';
import { RouterModule } from '@angular/router'; // Importer RouterModule

@Component({
  selector: 'jhi-my-footer',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './my-footer.component.html',
  styleUrl: './my-footer.component.scss',
})
export class MyFooterComponent {}
