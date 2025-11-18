import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IDisco } from '../disco.model';

@Component({
  selector: 'jhi-disco-detail',
  templateUrl: './disco-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class DiscoDetailComponent {
  disco = input<IDisco | null>(null);

  previousState(): void {
    window.history.back();
  }
}
