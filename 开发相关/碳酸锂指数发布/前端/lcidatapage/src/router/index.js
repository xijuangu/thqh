
// import full from 'core-js/full';
import { createRouter, createWebHistory } from 'vue-router';

const routes = [

    {
        path: '/index_upload_web',
        name: 'DataManagement',
        component: () => import('@/DataManagement.vue'),
    },
    {
        path: '/IndicatorBoard',
        name: 'IndicatorBoard',
        component: () => import('@/components/IndicatorBoard.vue'),
    },
    {
        path: '/TableTab',
        name: 'TableTab',
        component: () => import('@/components/TableTab.vue'),
    },
    {
        path: '/LineChart',
        name: 'LineChart',
        component: () => import('@/components/LineChart.vue'),
    }
];


const router = createRouter({
    base: '/index_upload_web/',
    history: createWebHistory(),
    routes
});

export default router;