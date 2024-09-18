import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormMoneyBillComponent } from './form-money-bill.component';
import { provideHttpClient } from '@angular/common/http';

describe('FormMoneyBillComponent', () => {
  let component: FormMoneyBillComponent;
  let fixture: ComponentFixture<FormMoneyBillComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormMoneyBillComponent],
      providers: [provideHttpClient()],
    }).compileComponents();

    fixture = TestBed.createComponent(FormMoneyBillComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
