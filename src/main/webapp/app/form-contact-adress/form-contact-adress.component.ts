import { Component, OnInit } from '@angular/core';
import { FormsModule, NgForm, NgModel } from '@angular/forms';
import { NgIf, NgOptimizedImage } from '@angular/common';
import TranslateDirective from '../shared/language/translate.directive';
import { ICustomer } from '../entities/customer/customer.model';
import { SharedUserDataService } from '../shared/shared-user-data.service';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-form-contact-adress',
  standalone: true,
  imports: [FormsModule, NgIf, TranslateDirective, NgOptimizedImage],
  templateUrl: './form-contact-adress.component.html',
  styleUrl: './form-contact-adress.component.scss',
})
export class FormContactAdressComponent implements OnInit {
  // [TODO] Pass the required id to a possibly null id
  user_info: ICustomer = {
    id: 0,
  };

  isSubmitted = false;

  constructor(
    private sharedDataService: SharedUserDataService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.sharedDataService.userId$.subscribe(data => {
      if (data) {
        this.user_info.id = data;
      }
    });
  }

  validateEmail(emailInput: NgModel): boolean | null {
    const emailPattern = /^[\w\-.]+@([\w-]+\.)+[\w-]{2,}$/;
    return emailInput.touched && !emailPattern.test(emailInput.value);
  }

  validatePhoneNumber(phoneNumber: NgModel): boolean | null {
    const phonePattern = /^(?:\+33\s?|0)[1-9](?:[\s.-]?\d{2}){4}$/; // Formats: +33 6 53 63 33 33 or 06 53 63 33 33
    return phoneNumber.touched && !phonePattern.test(phoneNumber.value.replace(/\s+/g, ''));
  }

  validatePostCode(postCode: NgModel): boolean | null {
    const postCodePattern = /^\d{5}$/; // Code postal de 5 chiffres
    return postCode.touched && !postCodePattern.test(postCode.value);
  }

  // Méthode pour valider le formulaire
  onSubmit(form: NgForm): void {
    this.isSubmitted = true;

    if (form.valid) {
      this.sharedDataService.setUserInfo(this.user_info);
      this.router.navigate(['/payment']);
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
    this.router.navigate(['/panier']);
  }
}
