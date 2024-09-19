import {createApp} from 'vue';
import App from './App.vue';
import router from '@/routes/index.js';
import store from '@/store/store.js';

import './firebase';

const app = createApp(App);
app.use(store);
app.use(router);

if ('serviceWorker' in navigator) {
    navigator.serviceWorker
        .register('/firebase-messaging-sw.js');
}

app.mount('#app');
