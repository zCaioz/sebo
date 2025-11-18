import { IUsuario, NewUsuario } from './usuario.model';

export const sampleWithRequiredData: IUsuario = {
  id: 23732,
  nome: 'though phew',
  email: 'Meire_Oliveira96@bol.com.br',
};

export const sampleWithPartialData: IUsuario = {
  id: 1812,
  nome: 'usher within',
  email: 'Antonella31@yahoo.com',
};

export const sampleWithFullData: IUsuario = {
  id: 32608,
  nome: 'outlaw lest',
  email: 'Roberto.Carvalho@live.com',
  telefone: 'yum up',
};

export const sampleWithNewData: NewUsuario = {
  nome: 'beside pish strait',
  email: 'JulioCesar69@hotmail.com',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
