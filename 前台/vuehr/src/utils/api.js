import Axios from "axios";
import {Message} from "element-ui";
import router from "../router";
// Axios.interceptors.request.use() 配置axios请求处理
// 拦截器封装返回的axios处理
// axios的httpCode=200
Axios.interceptors.response.use(success => {

        if (success.status && success.status === 200 && success.data.status === 500) {//请求成功后台报错500
            Message.error(success.data.message);
            return
        }
        if (success.data.message) {//显示请求成功的msg
            Message.success(success.data.message)
            // 后台未登录跨域CORS处理,修改到导航守卫中判断处理
            // 二次修理:当后台登录过期,浏览器一直开着sessionStorage未过期,此时只会进入initMenu方法并报错
            if (success.data.status === 401) {
                router.replace('/')
            }
        }
        // 成功返回respBean对象
        return success.data
// axios的httpCode<>200
    }, error => {

        if (error.response.status === 504 || error.response.status === 404) {
            Message.error("找不到服务器")
        } else if (error.response.status === 403) {
            Message.error("权限不足,请联系管理员")
        } else if (error.response.status === 401) {
            Message.error("尚未登录或登录过期,请重新登录")
            router.replace('/')
        } else {
            if (error.response.data.message) {
                Message.error(error.response.data.message)
            } else {
                Message.error("未知错误!")
            }
        }
    }
)
// 超时设置10分钟
Axios.defaults.timeout=600000
// 定义base 灵活定义url
// let base = '/api'
let base = ''

// 封装请求方法,并加入到Vue.prototype中供直接调用

//springSecurity默认登录只接受keyValue不接受json
export const postKeyValueRequest = (url, params) => {
    return Axios({
        method: 'post',
        url: `${base}${url}`,
        data: params,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},// 设置响应头
        transformRequest: [function (data) { //转换格式
            let res = ''

            for (let i in data) {
                res += encodeURIComponent(i) + '=' + encodeURIComponent(data[i]) + '&'
            }
            //console.log("keyValueRequest转换结果:" + res)
            return res
        }]
    })
}

// 提交JSON数据格式

export const postRequest = (url, params) => {
    return Axios({
        method: 'post',
        url: `${base}${url}`,
        data: params
    })
}
export const putRequest = (url, params) => {
    return Axios({
        method: 'put',
        url: `${base}${url}`,
        data: params
    })
}
// url传参
export const getRequest = (url) => {
    return Axios({
        method: 'get',
        url: `${base}${url}`
    })
}
export const deleteRequest = (url) => {
    return Axios({
        method: 'delete',
        url: `${base}${url}`
    })
}
// 同步请求




