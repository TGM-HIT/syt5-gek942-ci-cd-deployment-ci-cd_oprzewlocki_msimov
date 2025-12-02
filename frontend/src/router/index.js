import { createRouter, createWebHistory } from 'vue-router';
import AnalysisPage from '@/pages/AnalysisPage.vue';

const routes = [
  { path: '/', redirect: '/analysis' },
  { path: '/analysis', component: AnalysisPage },
];

export default createRouter({
  history: createWebHistory(),
  routes,
});
