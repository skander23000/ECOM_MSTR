import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'mstrtireApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'tire',
    data: { pageTitle: 'mstrtireApp.tire.home.title' },
    loadChildren: () => import('./tire/tire.routes'),
  },
  {
    path: 'tire-brand',
    data: { pageTitle: 'mstrtireApp.tireBrand.home.title' },
    loadChildren: () => import('./tire-brand/tire-brand.routes'),
  },
  {
    path: 'customer',
    data: { pageTitle: 'mstrtireApp.customer.home.title' },
    loadChildren: () => import('./customer/customer.routes'),
  },
  {
    path: 'customer-order',
    data: { pageTitle: 'mstrtireApp.customerOrder.home.title' },
    loadChildren: () => import('./customer-order/customer-order.routes'),
  },
  {
    path: 'order-item',
    data: { pageTitle: 'mstrtireApp.orderItem.home.title' },
    loadChildren: () => import('./order-item/order-item.routes'),
  },
  {
    path: 'item-list-lock',
    data: { pageTitle: 'mstrtireApp.itemListLock.home.title' },
    loadChildren: () => import('./item-list-lock/item-list-lock.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
