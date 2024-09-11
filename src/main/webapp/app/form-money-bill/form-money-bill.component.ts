import { Component, Input } from '@angular/core';
import { FormsModule, NgModel } from '@angular/forms';
import { NgClass, NgIf } from '@angular/common';

@Component({
  selector: 'jhi-form-money-bill',
  standalone: true,
  imports: [FormsModule, NgIf, NgClass],
  templateUrl: './form-money-bill.component.html',
  styleUrl: './form-money-bill.component.scss',
})
export class FormMoneyBillComponent {
  cardOwner = '';
  cardNumber = '';
  postCode = '';
  expirationDate = '';
  securityCode = '';
  address = '';
  city = '';

  @Input() deliAddress = '18 rue de la Liberté'; // Exemple de valeur
  @Input() deliPostCode = '79000'; // Exemple de valeur
  @Input() deliCity = 'Nogent sur marne'; // Exemple de valeur

  useDeliveryAddress = false; // Détermine si l'adresse de livraison est utilisée
  isSubmitted = false;

  toggleAddressFields(): void {
    if (this.useDeliveryAddress) {
      this.address = this.deliAddress;
      this.postCode = this.deliPostCode;
      this.city = this.deliCity;
    } else {
      this.address = '';
      this.postCode = '';
      this.city = '';
    }
  }

  validatePostCode(postCode: NgModel): boolean | null {
    const postCodePattern = /^\d{5}$/; // Code postal de 5 chiffres
    return postCode.touched && !postCodePattern.test(postCode.value);
  }

  validateCardNumber(cardNumber: NgModel): boolean | null {
    const cardNumberPattern = /^\d{13,19}$/; // Numéro de carte de 13 à 19 chiffres
    return cardNumber.touched && !cardNumberPattern.test(cardNumber.value);
  }

  validateMonth(monthValue: NgModel): boolean | null {
    const monthPattern = /^(0[1-9]|1[0-2])\/\d{2}$/; // Mois au format MM/AA
    return monthValue.touched && !monthPattern.test(monthValue.value);
  }

  validateSecurityCode(securityCode: NgModel): boolean | null {
    const securityCodePattern = /^\d{3}$/; // Code postal de 5 chiffres
    return securityCode.touched && !securityCodePattern.test(securityCode.value);
  }

  // Méthode pour soumettre le formulaire
  onSubmit(): void {
    // Si tout est valide, exécuter la logique de soumission
    this.isSubmitted = true;
    // eslint-disable-next-line no-console
    console.log('Formulaire soumis avec succès');
  }

  // Méthode pour retourner au panier
  goBackToCart(): void {
    // Logique pour rediriger ou revenir au panier
    // eslint-disable-next-line no-console
    console.log('Retour au panier');
  }
}
