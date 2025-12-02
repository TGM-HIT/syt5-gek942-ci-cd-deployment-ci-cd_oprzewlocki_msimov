import { createRouter, createWebHistory } from 'vue-router';
import TableWrapper from '@/components/tables/TableWrapper.vue';

export default createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', redirect: '/table/samples' },
    { path: '/table/:name', name: 'table', component: TableWrapper }
  ]
});
