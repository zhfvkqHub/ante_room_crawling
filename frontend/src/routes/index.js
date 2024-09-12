import {createRouter, createWebHistory} from 'vue-router';

const router = createRouter({
    history: createWebHistory(),
    routes: [
        // {
        //     path: '/',
        //     redirect: '/notice',
        // },
        {
            path: '/',
            name: 'NoticeMain',
            component: () => import('@/views/NoticeMain.vue'),
            meta: {
                title: '청년안심주택 모집공고',
            }
        },
        {
            path: '/:catchAll(.*)',
            name: 'NotFound',
            component: () => import('@/views/common/NotFoundPage.vue'),
            meta: {
                title: 'Not Found',
            }
        }
    ],
});

router.beforeEach((to) => {
    document.title = to.meta.title === undefined ? '청년안심주택' : to.meta.title;
});

export default router;
