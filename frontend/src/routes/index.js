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
                title: '모집공고',
            }
        },
    ],
});

router.beforeEach((to) => {
    document.title = to.meta.title === undefined ? '청년안심주택' : to.meta.title;
});

export default router;
