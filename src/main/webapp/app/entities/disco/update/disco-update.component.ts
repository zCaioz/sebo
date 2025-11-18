import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { TipoMidia } from 'app/entities/enumerations/tipo-midia.model';
import { IDisco } from '../disco.model';
import { DiscoService } from '../service/disco.service';
import { DiscoFormGroup, DiscoFormService } from './disco-form.service';

@Component({
  selector: 'jhi-disco-update',
  templateUrl: './disco-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DiscoUpdateComponent implements OnInit {
  isSaving = false;
  disco: IDisco | null = null;
  tipoMidiaValues = Object.keys(TipoMidia);

  protected discoService = inject(DiscoService);
  protected discoFormService = inject(DiscoFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DiscoFormGroup = this.discoFormService.createDiscoFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ disco }) => {
      this.disco = disco;
      if (disco) {
        this.updateForm(disco);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const disco = this.discoFormService.getDisco(this.editForm);
    if (disco.id !== null) {
      this.subscribeToSaveResponse(this.discoService.update(disco));
    } else {
      this.subscribeToSaveResponse(this.discoService.create(disco));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDisco>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(disco: IDisco): void {
    this.disco = disco;
    this.discoFormService.resetForm(this.editForm, disco);
  }
}
