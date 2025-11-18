import { TipoMidia } from 'app/entities/enumerations/tipo-midia.model';

export interface IDisco {
  id: number;
  tipoMidia?: keyof typeof TipoMidia | null;
  duracaoMinutos?: number | null;
}

export type NewDisco = Omit<IDisco, 'id'> & { id: null };
