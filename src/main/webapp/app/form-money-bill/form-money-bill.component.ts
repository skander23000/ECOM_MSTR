import { Component, Input } from '@angular/core';
import { FormsModule, NgForm, NgModel } from '@angular/forms';
import { NgClass, NgIf } from '@angular/common';
import TranslateDirective from '../shared/language/translate.directive';

@Component({
  selector: 'jhi-form-money-bill',
  standalone: true,
  imports: [FormsModule, NgIf, NgClass, TranslateDirective],
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
    this.useDeliveryAddress = !this.useDeliveryAddress;
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
    return cardNumber.touched && !cardNumberPattern.test(cardNumber.value.replace(/\s+/g, ''));
  }

  validateMonth(monthValue: NgModel): boolean | null {
    const monthPattern = /^(0[1-9]|1[0-2])\/\d{2}$/; // Mois au format MM/AA
    return monthValue.touched && !monthPattern.test(monthValue.value);
  }

  // Méthode pour valider la date d'expiration
  validateIsExpired(monthValue: NgModel): boolean | null {
    const [inputMonth, inputYear] = monthValue.value.split('/');
    const currentDate = new Date();
    const currentMonth = currentDate.getMonth() + 1; // Les mois dans JS sont indexés de 0 à 11
    const currentYear = currentDate.getFullYear() % 100; // Année sur deux chiffres

    // Comparaison de la date
    const inputMonthNumber = parseInt(inputMonth, 10);
    const inputYearNumber = parseInt(inputYear, 10);
    if (!monthValue.value) {
      return null;
    }
    return monthValue.touched && !(inputYearNumber > currentYear || (inputYearNumber === currentYear && inputMonthNumber >= currentMonth));
  }

  validateSecurityCode(securityCode: NgModel): boolean | null {
    const securityCodePattern = /^\d{3}$/; // Code postal de 5 chiffres
    return securityCode.touched && !securityCodePattern.test(securityCode.value);
  }

  onSubmit(form: NgForm): void {
    this.isSubmitted = true;

    if (form.valid) {
      // Si le formulaire est valide, tu peux exécuter la logique de soumission
      // eslint-disable-next-line no-console
      console.log('Formulaire soumis avec succès');

      // Logique supplémentaire, comme l'envoi des données au serveur
    } else {
      // Par exemple, ici, tu pourrais faire défiler jusqu'à la première erreur :
      const firstInvalidControl = document.querySelector('.ng-invalid');
      if (firstInvalidControl) {
        firstInvalidControl.scrollIntoView({ behavior: 'smooth' });
      }
    }
  }

  // Méthode pour retourner au panier
  goBackToCart(): void {
    // Logique pour rediriger ou revenir au panier
    // eslint-disable-next-line no-console
    console.log('Retour au panier');
  }
}
