import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { VendaDetailComponent } from './venda-detail.component';

describe('Venda Management Detail Component', () => {
  let comp: VendaDetailComponent;
  let fixture: ComponentFixture<VendaDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VendaDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./venda-detail.component').then(m => m.VendaDetailComponent),
              resolve: { venda: () => of({ id: 31753 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(VendaDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(VendaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load venda on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', VendaDetailComponent);

      // THEN
      expect(instance.venda()).toEqual(expect.objectContaining({ id: 31753 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
