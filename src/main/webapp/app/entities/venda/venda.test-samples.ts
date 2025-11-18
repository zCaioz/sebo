import dayjs from 'dayjs/esm';

import { IVenda, NewVenda } from './venda.model';

export const sampleWithRequiredData: IVenda = {
  id: 21309,
  dataVenda: dayjs('2025-11-18'),
  valor: 17420.41,
};

export const sampleWithPartialData: IVenda = {
  id: 23139,
  dataVenda: dayjs('2025-11-18'),
  valor: 29606.36,
};

export const sampleWithFullData: IVenda = {
  id: 32471,
  dataVenda: dayjs('2025-11-18'),
  valor: 12970.47,
};

export const sampleWithNewData: NewVenda = {
  dataVenda: dayjs('2025-11-18'),
  valor: 30025.52,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
