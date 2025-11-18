export interface IUsuario {
  id: number;
  nome?: string | null;
  email?: string | null;
  telefone?: string | null;
}

export type NewUsuario = Omit<IUsuario, 'id'> & { id: null };
