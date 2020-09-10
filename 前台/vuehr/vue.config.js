// 跨域解决->配置代理 开发环境由nodejs转发请求,生产环境用Nginx转发
let proxyObj = {}

proxyObj['/'] = {
    ws: false,
    target: 'http://localhost:8081',//转发目标
    changeOrigin: true,
    pathRewrite: {
        '/': ''
    }
}


module.exports = {
    devServer: {
        host: 'localhost',
        port: 8088,     //启动的端口
        open: true,     //启动完毕自动打开
        proxy: proxyObj //代理
    },
    // vue-echarts
    transpileDependencies: [
        'vue-echarts',
        'resize-detector'
    ]
}
