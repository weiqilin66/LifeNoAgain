// 处理后端返回的menuJSON children的回调遍历和component的转换
import {getRequest} from "./api";

export const initMenu = (store, router) => {
    // vuex中已存在,不需要通过请求后台获取菜单,f5刷新state的东西会清空
    if (store.state.routes.length > 0) {
        return
    }
    getRequest('/sysConfig/menu').then(data => {
        if (data) {
            let fmRoutes = formatRoutes(data);
            router.addRoutes(fmRoutes);
            // 提交mutations 保存菜单到vuex
            store.commit('initRoutes', fmRoutes);

            //store.dispatch('connect');
        }
    })
}

export const formatRoutes = (routes) => {
    let fmRoutes = []
    routes.forEach(route => {
        // 批量定义 等于let path = route.path
        let {
            path,
            name,
            component,
            meta,
            iconCls,
            children
        } = route

        if (children && children instanceof Array) {//一级菜单回调
            children = formatRoutes(children)
        }
        let fmRouter = {
            path: path,
            name: name,
            meta: meta,
            iconCls: iconCls,
            children: children,
            // es5 导入写法
            component(resolve) {
                if (component.startsWith("Home")) {
                    require(['../views/' + component + '.vue'], resolve);
                } else if (component.startsWith("Emp")) {
                    require(['../views/emp/' + component + '.vue'], resolve);
                } else if (component.startsWith("Stock")) {
                    require(['../views/stock/' + component + '.vue'], resolve);
                } else if (component.startsWith("Sal")) {
                    require(['../views/sal/' + component + '.vue'], resolve);
                } else if (component.startsWith("Sta")) {
                    require(['../views/sta/' + component + '.vue'], resolve);
                } else if (component.startsWith("Sys")) {
                    require(['../views/sys/' + component + '.vue'], resolve);
                }
            }
        }
        fmRoutes.push(fmRouter)
    })
    return fmRoutes
}