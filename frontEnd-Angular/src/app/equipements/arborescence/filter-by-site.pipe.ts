import { Pipe, PipeTransform } from '@angular/core';
import {Immuble} from "../../models/immuble";


@Pipe({
  name: 'filterBySite'
})
export class FilterBySitePipe implements PipeTransform {
  transform(immubles: Immuble[], selectedSiteId: number | null): Immuble[] {
    if (!selectedSiteId) {
      return [];
    }
    return immubles.filter(immuble => immuble.site.id === selectedSiteId);
  }
}
