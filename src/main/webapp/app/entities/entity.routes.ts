import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'Authorities' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'item',
    data: { pageTitle: 'Items' },
    loadChildren: () => import('./item/item.routes'),
  },
  {
    path: 'livro',
    data: { pageTitle: 'Livros' },
    loadChildren: () => import('./livro/livro.routes'),
  },
  {
    path: 'disco',
    data: { pageTitle: 'Discos' },
    loadChildren: () => import('./disco/disco.routes'),
  },
  {
    path: 'usuario',
    data: { pageTitle: 'Usuarios' },
    loadChildren: () => import('./usuario/usuario.routes'),
  },
  {
    path: 'emprestimo',
    data: { pageTitle: 'Emprestimos' },
    loadChildren: () => import('./emprestimo/emprestimo.routes'),
  },
  {
    path: 'venda',
    data: { pageTitle: 'Vendas' },
    loadChildren: () => import('./venda/venda.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
