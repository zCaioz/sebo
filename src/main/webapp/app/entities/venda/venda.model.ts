import dayjs from 'dayjs/esm';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { IItem } from 'app/entities/item/item.model';

export interface IVenda {
  id: number;
  dataVenda?: dayjs.Dayjs | null;
  valor?: number | null;
  usuario?: IUsuario | null;
  itens?: IItem[] | null;
}

export type NewVenda = Omit<IVenda, 'id'> & { id: null };
