import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class GetIconsService {
  processItem(item: 'SUMMER' | 'WINTER' | 'ALL_SEASON'): string {
    if (item === 'SUMMER') {
      return '';
    }
    if (item === 'ALL_SEASON') {
      return '';
    }
    return '../../content/images/website_icon_pack/icon_pack/snowflakes_icon.png';
  }
}
