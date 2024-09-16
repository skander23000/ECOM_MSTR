import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComparaisonComponent } from './comparaison.component';

describe('ComparaisonComponent', () => {
  let component: ComparaisonComponent;
  let fixture: ComponentFixture<ComparaisonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ComparaisonComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ComparaisonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
