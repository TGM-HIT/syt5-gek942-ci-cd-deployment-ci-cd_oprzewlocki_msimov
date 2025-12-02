import 'vuetify/styles';
import { createVuetify } from 'vuetify';

export default createVuetify({
  theme: {
    defaultTheme: 'light',
    themes: {
      light: {
        colors: {
          background: '#ffffff',
          surface: '#ffffff',
          primary: '#333333ff',  // near-black accents
          secondary: '#6b7280', // gray-500
          success: '#059669',
          error: '#853838ff'
        },
        variables: {
          'border-radius-root': '16px'
        }
      }
    }
  },
  defaults: {
    VCard: { elevation: 1, rounded: 'xl' },
    VBtn: { rounded: 'xl' },
    VTextField: { density: 'compact', variant: 'outlined' },
    VDataTable: { density: 'comfortable' }
  }
});
