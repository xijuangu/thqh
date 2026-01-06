import { createApp } from 'vue'
import App from './HomePage.vue'

// primevue styles
import PrimeVue from 'primevue/config';
import Aura from '@primeuix/themes/aura';

import ToastService from 'primevue/toastservice';


const app = createApp(App)

app.use(PrimeVue, {
    theme: {
        preset: Aura,
        options: {
            prefix: 'p',
            darkModeSelector: 'system',
            cssLayer: true
        }
    }
})

app.use(ToastService);



app.mount('#app')
