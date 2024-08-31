import {createRouter, createWebHistory} from 'vue-router';

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            redirect: '/notice',
        },
        {
            path: '/notice',
            name: 'NoticeMain',
            component: () => import('@/views/NoticeMain.vue'),
            meta: {
                title: '공실 서비스'
            }
        },
    ],
});

export default router;
