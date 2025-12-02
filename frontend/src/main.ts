import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import { createPinia } from 'pinia';
import vuetify from './plugins/vuetify';
import './styles/theme.css';
import '@mdi/font/css/materialdesignicons.css';

createApp(App)
  .use(createPinia())
  .use(router)
  .use(vuetify)
  .mount('#app');
