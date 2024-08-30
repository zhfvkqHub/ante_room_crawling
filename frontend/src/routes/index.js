import {createRouter, createWebHistory} from 'vue-router';

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            redirect: '/home',
        },
        {
            path: '/home',
            name: 'Home',
            component: () => import('@/views/HomePage.vue'),
            meta: {
                title: '공실 서비스'
            }
        },
    ],
});

export default router;
