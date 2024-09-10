import { Component } from '@angular/core';
import { FormsModule, NgModel } from '@angular/forms';
import { NgIf } from '@angular/common';

@Component({
  selector: 'jhi-form-contact-adress',
  standalone: true,
  imports: [FormsModule, NgIf],
  templateUrl: './form-contact-adress.component.html',
  styleUrl: './form-contact-adress.component.scss',
})
export class FormContactAdressComponent {
  email = '';
  phoneNumber = '';
  postCode = '';
  firstname = '';
  lastname = '';
  address = '';
  city = '';

  isSubmitted = false;

  validateEmail(emailInput: NgModel): boolean | null {
    const emailPattern = /^[\w\-.]+@([\w-]+\.)+[\w-]{2,}$/;
    return emailInput.touched && !emailPattern.test(emailInput.value);
  }

  validatePhoneNumber(phoneNumber: NgModel): boolean | null {
    const phonePattern = /^(?:\+33\s?|0)[1-9](?:[\s.-]?\d{2}){4}$/; // Formats: +33 6 53 63 33 33 or 06 53 63 33 33
    return phoneNumber.touched && !phonePattern.test(phoneNumber.value);
  }

  validatePostCode(postCode: NgModel): boolean | null {
    const postCodePattern = /^\d{5}$/; // Code postal de 5 chiffres
    return postCode.touched && !postCodePattern.test(postCode.value);
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
