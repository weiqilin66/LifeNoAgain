###开发文档:  
1. 菜单该何时加载:
    * 注意当用户f5刷新页面时,菜单不能消失
    * so: 路由导航守卫
        * router.beforeach(to,from,next=>{})相当于java的过滤器,当跳转的页面不是Login时要执行initMenu方法
        * initMenu不能每次到后台取数据,所以存储到vuex中,当state中有routes时,不需要请求后台
    * 注销时删除vuex中菜单的数据
    
2. 前后端分离的权限管理
    * 菜单的不同并不是用于隐藏页面来控制权限,页面始终是做用户体验的
    * 后端来控制权限, 数据始终是来自后端的
    * 前端校验也是为了用户体验发送正确的数据给后台极高效率,后端校验才是权限的控制

3. 登录控制
    * 前端的router跳转每次都是先判断是否登录,正常不会导致未登录而去访问后端接口的问题
    * 后端仍需做未登录的错误如何返回,因为有可能长时间未操作后端登录过期,前端sessionStorage仍然判断有用户权限,这时候就必须由后端返回信息控制判断

4. 展示 el-table 提交复杂的用el-form 
    * 使用el-form主要是为了rules数据校验
    * el-form-item 的prop数据校验, el-table-column 的prop遍历el-table的:data的key的依据
    
5. Axios同步请求 日期控件近n天ChartTrend.vue


###bugs:
1. 已解决
    1. deleteRequest删除单个 不要使用 ?name = value  直接?后面加要删除的id 后端使用/{id} 占位符
    2. deleteRequest中删除多个 使用?name=value1&name=value2 多个name相同的传参 后端不加占位符 /
    3. 方法里的请求是异步的 请求返回前就往下执行了 这个时候不要去使用请求里被赋值的对象
2. 未解决
    1. 权限管理 权限树取消和关闭后,再次重新点击 选中状态没有回归到默认的选中状态
    
    
    
 
    
    