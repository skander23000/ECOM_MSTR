import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormContactAdressComponent } from './form-contact-adress.component';

describe('FormContactAdressComponent', () => {
  let component: FormContactAdressComponent;
  let fixture: ComponentFixture<FormContactAdressComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormContactAdressComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FormContactAdressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
