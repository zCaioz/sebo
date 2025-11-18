import { ILivro, NewLivro } from './livro.model';

export const sampleWithRequiredData: ILivro = {
  id: 158,
  isbn: 'calculating even',
};

export const sampleWithPartialData: ILivro = {
  id: 22903,
  isbn: 'who tenderly',
  editora: 'into uh-huh',
};

export const sampleWithFullData: ILivro = {
  id: 2308,
  isbn: 'until woot geez',
  editora: 'part violently merrily',
  numeroPaginas: 13277,
};

export const sampleWithNewData: NewLivro = {
  isbn: 'aha',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
