import { IEmprestimo } from 'app/entities/emprestimo/emprestimo.model';
import { IVenda } from 'app/entities/venda/venda.model';

export interface IItem {
  id: number;
  titulo?: string | null;
  ano?: number | null;
  genero?: string | null;
  autorArtista?: string | null;
  disponibilidade?: boolean | null;
  emprestimo?: IEmprestimo | null;
  venda?: IVenda | null;
}

export type NewItem = Omit<IItem, 'id'> & { id: null };
