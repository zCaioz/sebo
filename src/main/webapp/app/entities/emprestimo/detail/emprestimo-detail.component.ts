import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IEmprestimo } from '../emprestimo.model';

@Component({
  selector: 'jhi-emprestimo-detail',
  templateUrl: './emprestimo-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class EmprestimoDetailComponent {
  emprestimo = input<IEmprestimo | null>(null);

  previousState(): void {
    window.history.back();
  }
}
