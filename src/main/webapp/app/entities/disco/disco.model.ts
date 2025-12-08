import { TipoMidia } from 'app/entities/enumerations/tipo-midia.model';

export interface IDisco {
  id: number;
  tipoMidia?: keyof typeof TipoMidia | null;
  duracaoMinutos?: number | null;

  titulo?: string | null;
  ano?: number | null;
  genero?: string | null;
  autorArtista?: string | null;
  disponibilidade?: boolean | null;
}

export type NewDisco = Omit<IDisco, 'id'> & { id: null };
