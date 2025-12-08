export interface ILivro {
  id: number;
  isbn?: string | null;
  editora?: string | null;
  numeroPaginas?: number | null;
  titulo?: string | null;
  ano?: number | null;
  genero?: string | null;
  autorArtista?: string | null;
  disponibilidade?: boolean | null;
}

export type NewLivro = Omit<ILivro, 'id'> & { id: null };
