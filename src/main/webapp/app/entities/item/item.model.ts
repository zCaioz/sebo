export interface IItem {
  id: number;
  titulo?: string | null;
  ano?: number | null;
  genero?: string | null;
  autorArtista?: string | null;
  disponibilidade?: boolean | null;
}

export type NewItem = Omit<IItem, 'id'> & { id: null };
