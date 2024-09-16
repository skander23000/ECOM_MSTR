import { Component, OnInit, Renderer2, RendererFactory2, inject } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { LangChangeEvent, TranslateService } from '@ngx-translate/core';
import dayjs from 'dayjs/esm';

import { AccountService } from 'app/core/auth/account.service';
import { AppPageTitleStrategy } from 'app/app-page-title-strategy';
import FooterComponent from '../footer/footer.component';
import PageRibbonComponent from '../profiles/page-ribbon.component';
import { MyHeaderComponent } from '../../my-header/my-header.component';
import { MyFooterComponent } from '../../my-footer/my-footer.component';
import { FormMoneyBillComponent } from '../../form-money-bill/form-money-bill.component';
import { FormContactAdressComponent } from '../../form-contact-adress/form-contact-adress.component';
import { CartComponent } from '../../cart/cart.component';
import { CatalogueComponent } from '../../catalogue/catalogue.component';

@Component({
  standalone: true,
  selector: 'jhi-main',
  templateUrl: './main.component.html',
  providers: [AppPageTitleStrategy],
  imports: [
    RouterOutlet,
    FooterComponent,
    PageRibbonComponent,
    MyHeaderComponent,
    MyFooterComponent,
    FormMoneyBillComponent,
    FormContactAdressComponent,
    CartComponent,
    CatalogueComponent,
  ],
})
export default class MainComponent implements OnInit {
  public isConnected = false;

  private renderer: Renderer2;
  private router = inject(Router);
  private appPageTitleStrategy = inject(AppPageTitleStrategy);
  private accountService = inject(AccountService);
  private translateService = inject(TranslateService);
  private rootRenderer = inject(RendererFactory2);

  constructor() {
    this.renderer = this.rootRenderer.createRenderer(document.querySelector('html'), null);
  }

  ngOnInit(): void {
    // try to log in automatically
    this.accountService.identity().subscribe();
    this.accountService.getAuthenticationState().subscribe(account => {
      if (account) {
        this.isConnected = true;
      }
    });

    this.translateService.onLangChange.subscribe((langChangeEvent: LangChangeEvent) => {
      this.appPageTitleStrategy.updateTitle(this.router.routerState.snapshot);
      dayjs.locale(langChangeEvent.lang);
      this.renderer.setAttribute(document.querySelector('html'), 'lang', langChangeEvent.lang);
    });
  }
}
