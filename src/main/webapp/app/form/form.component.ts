import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'jhi-form',
  standalone: true,
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.scss'],
})
export class FormComponent {
  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      name: ['', Validators.required],
      firstname: ['', Validators.required],
      phone: ['', [Validators.required, this.phoneValidator]],
      email: ['', [Validators.required, Validators.email]],
      adress: ['', Validators.required],
      post_code: ['', [Validators.required, Validators.pattern(/^[0-9]{5}$/)]],
      city: ['', Validators.required],
    });
  }

  // Vérification personnalisée pour le format du téléphone
  phoneValidator(control: any) {
    const phone = control.value || '';
    const formattedPhone = phone.replace(/\D+/g, '');

    if (formattedPhone.length !== 10) {
      return { invalidPhone: true };
    }
    return null;
  }

  // Formatage automatique du téléphone
  formatPhone(event: any) {
    let input = event.target.value;
    input = input.replace(/\D+/g, ''); // Enlève tout sauf les chiffres
    if (input.length > 0) {
      input = input.match(new RegExp('.{1,2}', 'g')).join(' '); // Format par groupes de 2
    }
    event.target.value = input;
  }

  // Méthode pour soumettre le formulaire
  onSubmit() {
    if (this.form.valid) {
      console.log('Formulaire valide', this.form.value);
    } else {
      console.log('Formulaire invalide');
    }
  }
}
