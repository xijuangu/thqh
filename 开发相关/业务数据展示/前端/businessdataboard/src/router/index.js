import { createRouter, createWebHistory } from "vue-router";

const routes = [
    {
        path: "/",
        redirect: "/BDDSPage",
    },
    {
        path: "/login",
        name: "LoginPage",
        component: () => import("@/HomePage/LoginPage.vue"),
        meta: { requiresAuth: false } // 显式声明，避免undefined
    },
    {
        path: "/BDDSPage",
        name: "HomePage",
        component: () => import("@/HomePage/HomePage.vue"),
        meta: {
            requiresAuth: true,
        }
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

// 改进的路由守卫
router.beforeEach((to, from, next) => {
    console.log('导航从:', from.path, '到:', to.path);
    // 检查是否需要认证
    if (to.meta.requiresAuth) {
        const userid = localStorage.getItem("userid");
        const loginTime = localStorage.getItem("loginTime");
        const currentTime = new Date().getTime();
        const twentyFourHours = 24 * 60 * 60 * 1000;

        // console.log('Token:', token, 'LoginTime:', loginTime);

        // 验证登录状态和时效
        if (userid && loginTime && (currentTime - loginTime < twentyFourHours)) {
            console.log('用户已登录，允许访问');
            next(); // 已登录，允许访问
        } else {
            // 清除过期或无效的登录状态
            console.log('用户未登录或会话过期，重定向到登录页');
            localStorage.removeItem("token");
            localStorage.removeItem("loginTime");
            next('/login'); // 重定向到登录页
        }
    } else {

        console.log('无需认证的路径');
        next(); // 无需认证的路径，允许访问
    }
});

export default router;

