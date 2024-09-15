import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class GetIconsService {
  processItem(item: 'SUMMER' | 'WINTER' | 'ALL_SEASON'): string {
    if (item === 'SUMMER') {
      return '../../content/images/website_icon_pack/icon_pack/summer_icon.png';
    }
    if (item === 'ALL_SEASON') {
      return '../../content/images/website_icon_pack/icon_pack/all_season.png';
    }
    return '../../content/images/website_icon_pack/icon_pack/snowflakes_icon.png';
  }
}
