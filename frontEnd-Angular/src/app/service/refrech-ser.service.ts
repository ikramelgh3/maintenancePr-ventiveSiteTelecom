import { Injectable } from '@angular/core';
import {Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class RefrechSerService {

  private refreshSignal = new Subject<void>();

  constructor() { }

  getRefreshSignal() {
    return this.refreshSignal.asObservable();
  }

  triggerRefresh() {
    this.refreshSignal.next();
  }


}
