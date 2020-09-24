import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: '登录',
        component: Login,
        hidden: true
    },
    {
        path: '/home',
        name: 'Home',
        component: () => import( '../views/Home'),
        hidden: true
    }
   /* ,
    {  //匹配不上其他路径,既无效路径会进入此路由 跳转到home页
        path: '*',
        name: 'error',
        component:() => import('../views/Error')
    }*/
    // -----------------------------------------------------------------
    // 测试路由 t1/t2 .vue已删除 router跳转优先匹配跳转children下的router
    /*,{
        path: '/home',
        name: '导航一',
        component: () => import( '../views/Home'),
        children: [
            {
                path: '/test1',
                name: '选项1',
                component: () => import( '../views/T1')
            },
            {
                path: '/test2',
                name: '选项2',
                component: () => import( '../views/T2')
            }
        ]
    }*/

]

const router = new VueRouter({
    routes
})

export default router
