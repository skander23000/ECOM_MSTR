import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormMoneyBillComponent } from './form-money-bill.component';

describe('FormMoneyBillComponent', () => {
  let component: FormMoneyBillComponent;
  let fixture: ComponentFixture<FormMoneyBillComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormMoneyBillComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FormMoneyBillComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
