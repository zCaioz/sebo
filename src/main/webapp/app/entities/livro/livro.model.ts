export interface ILivro {
  id: number;
  isbn?: string | null;
  editora?: string | null;
  numeroPaginas?: number | null;
}

export type NewLivro = Omit<ILivro, 'id'> & { id: null };
