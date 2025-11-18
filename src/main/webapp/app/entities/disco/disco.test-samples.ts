import { IDisco, NewDisco } from './disco.model';

export const sampleWithRequiredData: IDisco = {
  id: 25241,
  tipoMidia: 'CD',
};

export const sampleWithPartialData: IDisco = {
  id: 6192,
  tipoMidia: 'VINIL',
};

export const sampleWithFullData: IDisco = {
  id: 23122,
  tipoMidia: 'DVD',
  duracaoMinutos: 28011,
};

export const sampleWithNewData: NewDisco = {
  tipoMidia: 'CD',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
