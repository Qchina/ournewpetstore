import { createRouter, createWebHistory } from 'vue-router'  

const routes = [
  // ...其他路由...
  {
    path: '/',
    redirect: '/accountauth'  // 确保有默认重定向
  },
  {
    path: '/userauth',
    component: () => import('../views/UserAuth.vue')  // 使用动态导入
  },
  {
    path: '/accountauth',
    component: () => import('../views/AccountAuth.vue')  // 使用动态导入
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router