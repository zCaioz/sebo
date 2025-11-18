import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDisco } from '../disco.model';
import { DiscoService } from '../service/disco.service';

@Component({
  templateUrl: './disco-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DiscoDeleteDialogComponent {
  disco?: IDisco;

  protected discoService = inject(DiscoService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.discoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
