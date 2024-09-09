import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TireBrandDetailComponent } from './tire-brand-detail.component';

describe('TireBrand Management Detail Component', () => {
  let comp: TireBrandDetailComponent;
  let fixture: ComponentFixture<TireBrandDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TireBrandDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./tire-brand-detail.component').then(m => m.TireBrandDetailComponent),
              resolve: { tireBrand: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TireBrandDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TireBrandDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tireBrand on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TireBrandDetailComponent);

      // THEN
      expect(instance.tireBrand()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
