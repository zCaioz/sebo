import dayjs from 'dayjs/esm';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IVenda {
  id: number;
  dataVenda?: dayjs.Dayjs | null;
  valor?: number | null;
  usuario?: IUsuario | null;
}

export type NewVenda = Omit<IVenda, 'id'> & { id: null };
