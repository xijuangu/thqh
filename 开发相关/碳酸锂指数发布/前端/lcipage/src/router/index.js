
// import full from 'core-js/full';
import { createRouter, createWebHistory } from 'vue-router';


/**
 * 路由规则数组
 * 每个路由对象对应一个页面组件，包含路径、命名、组件加载方式（懒加载）
 * 采用动态import（() => import('组件路径')）实现组件懒加载，优化首屏加载速度
 */
const routes = [

    {
        path: '/index_web', // 路由路径：访问该路径时匹配当前路由
        name: 'home_page', // 路由名称：用于通过名称跳转
        component: () => import('@/home_web.vue'), // 懒加载组件
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
    },





];

// 创建路由实例
const router = createRouter({
    history: createWebHistory(),
    base: '/index_web/', // 路由基路径：所有路由路径的前缀
    routes
});

export default router;