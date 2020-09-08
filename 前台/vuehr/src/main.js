import Vue from 'vue'
import App from './App.vue'
import router from './router'
/*main.js中配置 不能使用this.$xxx 因为this就是Vue实例本身,所有this.$xx都是在main.js配置后再.vue文件中使用的*/
// 引入 ElementUI
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
Vue.use(ElementUI,{ size: 'small', zIndex: 1800 });//弹窗大小默认2000

Vue.config.productionTip = false

// 引入封装完的axios 所有.vue文件通过this.xx引用 其他文件例如.js不行,需要额外import
import {postKeyValueRequest, postRequest, getRequest, putRequest, deleteRequest} from "./utils/api";

Vue.prototype.postKeyValueRequest = postKeyValueRequest
Vue.prototype.postRequest = postRequest
Vue.prototype.getRequest = getRequest
Vue.prototype.putRequest = putRequest
Vue.prototype.deleteRequest = deleteRequest


import {initMenu} from "./utils/menu";
import {Message} from "element-ui";
// 导航守卫 拦截方法如果不是登录页的页面跳转,调用initMenu方法
router.beforeEach((to, from, next)=>{////每次router跳转都会执行此方法
    if(to.name==='登录'){
        next() // 放行
    }else {
        if (window.sessionStorage.getItem('user')) {
            initMenu(store,router)
            next()
        }else {
            Message.warning('sir,请先登录')
            // 未登录&访问其他url: ,/先跳转到/login,?redirect携带登录成功后跳转的页面,在Login页判断要跳转的页面
            next('/?redirect='+to.path)
        }

    }
})

// 导入 font-awesome 小图标
import 'font-awesome/css/font-awesome.min.css'
// 引入 vuex
import store from './store'

// // 使用vue-echarts
// import ECharts from 'vue-echarts' // 在 webpack 环境下指向 components/ECharts.vue
// // 手动引入 ECharts 各模块来减小打包体积
// import 'echarts/lib/chart/bar'
// import 'echarts/lib/component/tooltip'
// import 'echarts-gl'
// // 注册组件后即可使用
// Vue.component('v-chart', ECharts)
import charts from 'echarts'
Vue.prototype.echarts = charts

//时间格式化
Vue.prototype.getyyyyMMdd=function(d) {
    var curr_date = d.getDate();
    var curr_month = d.getMonth() + 1;
    var curr_year = d.getFullYear();
    String(curr_month).length < 2 ? (curr_month = "0" + curr_month): curr_month;
    String(curr_date).length < 2 ? (curr_date = "0" + curr_date): curr_date;
    var yyyyMMdd = curr_year + "" + curr_month +""+ curr_date;
    return yyyyMMdd;
}



new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')
