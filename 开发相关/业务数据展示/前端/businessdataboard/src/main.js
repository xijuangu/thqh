import { createApp } from 'vue';
import App from './App.vue';
import PrimeVue from 'primevue/config';
import Aura from '@primeuix/themes/aura';

import 'animate.css';

import Particles from "@tsparticles/vue3";
import { loadFull } from 'tsparticles';

import ConfirmationService from 'primevue/confirmationservice'
import ToastService from 'primevue/toastservice'
import ConfirmDialog from 'primevue/confirmdialog';


import router from './router';

import Tooltip from 'primevue/tooltip';






const app = createApp(App);

app.use(PrimeVue, {
    theme: {
        preset: Aura,
        options: {
            prefix: 'p',
            darkModeSelector: 'system',
            cssLayer: true
        }
    }
});

app.use(Particles,{
    init: async (engine) => {
        await loadFull(engine);
    }
});

app.use(ConfirmationService);
app.use(ToastService);
app.use(router);
app.directive('tooltip', Tooltip);
app.component('ConfirmDialog', ConfirmDialog);


app.mount('#app');