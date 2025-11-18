import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IItem } from '../item.model';

@Component({
  selector: 'jhi-item-detail',
  templateUrl: './item-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ItemDetailComponent {
  item = input<IItem | null>(null);

  previousState(): void {
    window.history.back();
  }
}
