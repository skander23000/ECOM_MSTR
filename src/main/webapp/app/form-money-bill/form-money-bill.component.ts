import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm, NgModel } from '@angular/forms';
import { DatePipe, NgClass, NgIf, NgOptimizedImage } from '@angular/common';
import TranslateDirective from '../shared/language/translate.directive';
import { ICustomer } from '../entities/customer/customer.model';
import { SharedUserDataService } from '../shared/shared-user-data.service';
import { PaymentInfo } from '../entities/entity.payment-info';
import { Router } from '@angular/router';
import { FrontTimerService } from '../shared/front-timer.service';
import { BasketService } from '../basket.service';
import { PopUpComponent } from '../pop-up/pop-up.component';

@Component({
  selector: 'jhi-form-money-bill',
  standalone: true,
  imports: [FormsModule, NgIf, NgClass, TranslateDirective, NgOptimizedImage, DatePipe, PopUpComponent],
  templateUrl: './form-money-bill.component.html',
  styleUrl: './form-money-bill.component.scss',
})
export class FormMoneyBillComponent implements OnInit, AfterViewInit {
  paymentInfo: PaymentInfo;
  user_infos: ICustomer | null = null;
  totalItems: number | null = 0;
  useDeliveryAddress = false;
  isSubmitted = false;
  endTime: Date | null = null;
  showValidationPopUp = false;
  popUpTitle = 'Notification';
  popUpMessage = 'Voulez-vous vraiment passer la commande ?';
  @ViewChild('firstInput') firstInputElement!: ElementRef;

  constructor(
    private sharedDataService: SharedUserDataService,
    private router: Router,
    private timerService: FrontTimerService,
    private basketService: BasketService,
  ) {
    this.paymentInfo = {};
  }

  ngOnInit(): void {
    this.user_infos = this.sharedDataService.getUserInfo();

    this.basketService.totalItems$.subscribe(total => {
      this.totalItems = total;
    });
    this.timerService.getTimerState().subscribe(remainingTimeInSeconds => {
      const currentTime = new Date(); // Heure actuelle
      this.endTime = new Date(currentTime.getTime() + remainingTimeInSeconds * 1000);
    });
  }

  ngAfterViewInit(): void {
    this.firstInputElement.nativeElement.focus();
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
    if (!monthValue.value) {
      return null;
    }
    const [inputMonth, inputYear] = monthValue.value.split('/');
    const currentDate = new Date();
    const currentMonth = currentDate.getMonth() + 1; // Les mois dans JS sont indexés de 0 à 11
    const currentYear = currentDate.getFullYear() % 100; // Année sur deux chiffres

    // Comparaison de la date
    const inputMonthNumber = parseInt(inputMonth, 10);
    const inputYearNumber = parseInt(inputYear, 10);
    return monthValue.touched && !(inputYearNumber > currentYear || (inputYearNumber === currentYear && inputMonthNumber >= currentMonth));
  }

  validateSecurityCode(securityCode: NgModel): boolean | null {
    if (!securityCode.value) {
      return false;
    }
    const securityCodePattern = /^\d{3}$/; // Code postal de 5 chiffres
    return securityCode.touched && !securityCodePattern.test(securityCode.value);
  }

  onDismissPopup(): void {
    this.showValidationPopUp = false;
  }

  onConfirmPopup(): void {
    this.showValidationPopUp = false;
    this.validateCart();
  }

  onSubmit(form: NgForm): void {
    this.isSubmitted = true;
    if (form.valid) {
      this.showValidationPopUp = true;
    } else {
      // Par exemple, ici, tu pourrais faire défiler jusqu'à la première erreur :
      const firstInvalidControl = document.querySelector('.ng-invalid');
      if (firstInvalidControl) {
        firstInvalidControl.scrollIntoView({ behavior: 'smooth' });
      }
    }
  }

  validateCart(): void {
    this.sharedDataService.setPaymentInfo(this.paymentInfo);
    this.timerService.stopTimer();

    // Logique supplémentaire, comme l'envoi des données au serveur
    const userUuid = this.sharedDataService.getUserId();
    let userInfoWithoutId;

    // Vérifier que this.user_infos existe et est un objet avant de le déstructurer
    if (this.user_infos) {
      const { id, ...rest } = this.user_infos;
      userInfoWithoutId = rest; // Stocker les infos utilisateur sans l'id
    } else {
      console.warn('Aucune information utilisateur disponible.');
      return;
    }
    const now = new Date().toISOString();
    const orderItems: any[] = this.basketService.getContent().map(item => ({
      quantity: item.count,
      price: item.tire.price,
      customerOrder: {
        totalAmount: (this.totalItems ?? 0) * item.count,
        paymentDate: now,
        orderDate: now,
        status: 'PENDING',
        paymentMethod: 'CREDIT_CARD',
        paymentStatus: 'PENDING',
        customer: { country: 'France', ...userInfoWithoutId },
      },
      tire: item.tire,
    }));
    // Appeler la méthode createOrderItemsForPayment avec les paramètres nécessaires
    this.basketService.createOrderItemsForPayment(userUuid, orderItems).subscribe({
      next: () => {
        this.sharedDataService.setSuccessMessage(true);
        this.basketService.wipe().subscribe();
        // eslint-disable-next-line no-console
        console.log('Commande succès');
        this.router.navigate(['/']);
      },
      error: () => {
        this.sharedDataService.setErrorPaiementMessage(true);
        this.router.navigate(['/']);
      },
    });
  }

  // Méthode pour retourner au panier
  goBackToCart(): void {
    this.timerService.addActivity();
    this.router.navigate(['/panier']);
  }

  onDivClick(event: MouseEvent): void {
    if ((event.target as HTMLElement).tagName !== 'INPUT') {
      const checkbox = (event.currentTarget as HTMLElement).querySelector('input[type="checkbox"]') as HTMLInputElement;
      checkbox.checked = !checkbox.checked;
      this.toggleAddressFields();
    }
  }
}
