import dayjs from 'dayjs/esm';
import { IItem } from 'app/entities/item/item.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IVenda {
  id: number;
  dataVenda?: dayjs.Dayjs | null;
  valor?: number | null;
  item?: IItem | null;
  usuario?: IUsuario | null;
}

export type NewVenda = Omit<IVenda, 'id'> & { id: null };
