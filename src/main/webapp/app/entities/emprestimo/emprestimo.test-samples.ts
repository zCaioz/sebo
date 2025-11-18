import dayjs from 'dayjs/esm';

import { IEmprestimo, NewEmprestimo } from './emprestimo.model';

export const sampleWithRequiredData: IEmprestimo = {
  id: 17451,
  dataEmprestimo: dayjs('2025-11-18'),
  dataPrevistaDevolucao: dayjs('2025-11-18'),
  status: 'ATRASADO',
};

export const sampleWithPartialData: IEmprestimo = {
  id: 27468,
  dataEmprestimo: dayjs('2025-11-18'),
  dataPrevistaDevolucao: dayjs('2025-11-18'),
  dataDevolucao: dayjs('2025-11-18'),
  status: 'ATRASADO',
};

export const sampleWithFullData: IEmprestimo = {
  id: 9367,
  dataEmprestimo: dayjs('2025-11-18'),
  dataPrevistaDevolucao: dayjs('2025-11-18'),
  dataDevolucao: dayjs('2025-11-18'),
  status: 'DEVOLVIDO',
};

export const sampleWithNewData: NewEmprestimo = {
  dataEmprestimo: dayjs('2025-11-18'),
  dataPrevistaDevolucao: dayjs('2025-11-18'),
  status: 'EM_ANDAMENTO',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
