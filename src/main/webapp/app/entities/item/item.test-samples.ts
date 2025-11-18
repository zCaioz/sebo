import { IItem, NewItem } from './item.model';

export const sampleWithRequiredData: IItem = {
  id: 27990,
  titulo: 'yippee',
  disponibilidade: true,
};

export const sampleWithPartialData: IItem = {
  id: 22790,
  titulo: 'near knit bleakly',
  ano: 8352,
  genero: 'suspiciously',
  disponibilidade: true,
};

export const sampleWithFullData: IItem = {
  id: 23534,
  titulo: 'evince',
  ano: 29818,
  genero: 'skateboard',
  autorArtista: 'convalesce',
  disponibilidade: false,
};

export const sampleWithNewData: NewItem = {
  titulo: 'considering whose',
  disponibilidade: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
