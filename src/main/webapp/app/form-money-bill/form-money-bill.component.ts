import { Component, OnInit } from '@angular/core';
import { FormsModule, NgForm, NgModel } from '@angular/forms';
import { NgClass, NgIf, NgOptimizedImage } from '@angular/common';
import TranslateDirective from '../shared/language/translate.directive';
import { ICustomer } from '../entities/customer/customer.model';
import { SharedUserDataService } from '../shared/shared-user-data.service';
import { PaymentInfo } from '../entities/entity.payment-info';

@Component({
  selector: 'jhi-form-money-bill',
  standalone: true,
  imports: [FormsModule, NgIf, NgClass, TranslateDirective, NgOptimizedImage],
  templateUrl: './form-money-bill.component.html',
  styleUrl: './form-money-bill.component.scss',
})
export class FormMoneyBillComponent implements OnInit {
  paymentInfo: PaymentInfo;
  user_infos: ICustomer | null = null;

  useDeliveryAddress = false; // Détermine si l'adresse de livraison est utilisée
  isSubmitted = false;

  constructor(private sharedDataService: SharedUserDataService) {
    this.paymentInfo = {};
  }

  ngOnInit(): void {
    this.sharedDataService.userInfo$.subscribe(data => {
      this.user_infos = data;
    });
  }

  toggleAddressFields(): void {
    if (!this.user_infos) {
      return;
    }
    this.useDeliveryAddress = !this.useDeliveryAddress;
    if (this.useDeliveryAddress) {
      this.paymentInfo.address = this.user_infos.address;
      this.paymentInfo.postCode = this.user_infos.zipCode;
      this.paymentInfo.city = this.user_infos.city;
    } else {
      this.paymentInfo.address = '';
      this.paymentInfo.postCode = '';
      this.paymentInfo.city = '';
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
      this.sharedDataService.setPaymentInfo(this.paymentInfo);
      // [TODO] [ROUTAGE] Routage vers l'alerte "Votre commande est passée avec succès"

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
    // [TODO] [ROUTAGE] Routage vers le panier
    // eslint-disable-next-line no-console
    console.log('Retour au panier');
  }
}
