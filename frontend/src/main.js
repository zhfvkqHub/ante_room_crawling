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
        .register('/firebase-messaging-sw.js')
        .then((registration) => {
            console.log('서비스 워커 등록 성공:', registration);
        })
        .catch((error) => {
            console.log('서비스 워커 등록 실패:', error);
        });
}

app.mount('#app');
