import dayjs from 'dayjs/esm';
import { IItem } from 'app/entities/item/item.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { StatusEmprestimo } from 'app/entities/enumerations/status-emprestimo.model';

export interface IEmprestimo {
  id: number;
  dataEmprestimo?: dayjs.Dayjs | null;
  dataPrevistaDevolucao?: dayjs.Dayjs | null;
  dataDevolucao?: dayjs.Dayjs | null;
  status?: keyof typeof StatusEmprestimo | null;
  item?: IItem | null;
  usuario?: IUsuario | null;
}

export type NewEmprestimo = Omit<IEmprestimo, 'id'> & { id: null };
