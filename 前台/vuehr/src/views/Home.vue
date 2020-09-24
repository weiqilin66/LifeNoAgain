<template>
    <div>
        <el-container>
            <!--header-->
            <el-header class="homeHeader" :style="customColor">
                <div>
                    <!--E 下拉框 el-dropdown-item command值传入@command方法中-->
                    <el-dropdown @command="changeTheme">
                        <span class="el-dropdown-link"
                              style="font-size: 30px;font-family: 华文行楷;color: #ffffff;cursor: pointer;">灼猪</span>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item command="black">黑色</el-dropdown-item>
                            <el-dropdown-item divided command="blue">蓝色</el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </div>
                <el-dropdown class="userInfo" @command="dropdownHandler">
                    <span class="el-dropdown-link">
                {{user.name}}<i><img :src="user.userface"></i>
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item command="center">个人中心</el-dropdown-item>
                        <el-dropdown-item command="setting">设置</el-dropdown-item>
                        <el-dropdown-item divided command="logout">注销登录</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </el-header>

            <el-container>
                <!--侧边栏-->
                <el-aside width="200px">
                    <el-menu router>
                        <!-- index 区分是否是同一个菜单 index+''数值变字符串-->
                        <el-submenu :index="index+''" v-for="(item,index) in routes" v-if="!item.hidden" :key="index">
                            <template slot="title">
                                <i :class="item.iconCls" :style="customColorLeft"/>
                                <span>{{item.name}}</span>
                            </template>
                            <el-menu-item :index="child.path" v-for="(child,index2) in item.children" :key="index2">
                                {{child.name}}
                            </el-menu-item>
                        </el-submenu>
                    </el-menu>
                </el-aside>
                <!--中间栏-->
                <el-main>
                    <!--面包屑--><!--home页不显示面包屑-->
                    <el-breadcrumb separator-class="el-icon-arrow-right" v-if="this.$router.currentRoute.path!='/home'">
                        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
                        <el-breadcrumb-item>{{this.$router.currentRoute.name}}</el-breadcrumb-item>
                    </el-breadcrumb>
                    <div v-if="this.$router.currentRoute.name=='Home'">
                        <h1 style="display: flex;justify-content: center">Wayne监控</h1>
                    </div>
                    <router-view class="homeRouterView"/>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script>
    export default {
        name: "Home",
        components:{},
        data() {
            return {
                user: JSON.parse(window.sessionStorage.getItem('user')),
                customColor: 'background-color: #409eff;',
                customColorLeft: 'margin-right:6px; color: #409eff'

            }
        },
        computed: {
            // vuex 中取值一般使用computed
            routes() {
                return this.$store.state.routes
            }
        },
        mounted() {//登录成功首页初始化数据
            // this.initExcelData()
            this.getKeyWord()//获取折线图检索词
            this.getGoodMain() //获取goodMain表
        },
        methods: {
            // 个人中心下拉框配置
            dropdownHandler(cmd) {
                if (cmd === 'logout') {
                    this.$confirm('是否确认注销?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.getRequest('/logout')
                        window.sessionStorage.clear()
                        this.$store.state.routes = []
                        this.$router.replace('/')
                    }).catch(() => {
                    });
                } else if (cmd === 'center') {

                } else {

                }
            },
            // 修改主题
            changeTheme(cmd) {
                if (cmd === 'black') {
                    this.customColor = 'background-color: #404040;'
                    this.customColorLeft = 'margin-right:6px; color: #404040'

                } else if (cmd === 'blue') {
                    this.customColor = 'background-color: #409eff;'
                    this.customColorLeft = 'margin-right:6px; color: #409eff'
                }
            },
            // initExcelData() {
            //     this.getRequest("/statistics/chart/initExcel").then(resp=>{
            //         if (resp) {
            //             console.log("goodExcel初始化导入成功")
            //         }
            //     })
            // },
            getKeyWord() {
                this.getRequest("/statistics/chart/goodList").then(resp => {
                    if (resp) {
                        this.btnList = resp.data
                        //趋势图使用 存入store
                        this.$store.commit("initBtnList", this.btnList)
                        this.$store.commit("backBtnList",this.btnList)
                        console.log('Home初始化存入折线图btnList>>>')
                    }
                })
            },
            getGoodMain() {
                this.getRequest("/noRight/goodMain/").then(resp=>{
                    if (resp) {
                        this.$store.commit("initGoodMainList", resp.data)
                        console.log('Home初始化存入商品主表信息goodMainList>>>')
                    }
                })
            }
        }

    }
</script>


<style>
    .homeRouterView {
        margin-top: 10px;
    }

    .homeWelcome {
        text-align: center;
        font-size: 30px;
        font-family: 华文行楷;
        color: #409eff;
        padding-top: 50px;
    }

    .homeHeader {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 0px 15px;
        box-sizing: border-box;
    }

    .homeHeader .userInfo {
        cursor: pointer;
    }

    .el-dropdown-link img {
        width: 48px;
        height: 48px;
        border-radius: 24px;
        margin-left: 8px;
    }

    .el-dropdown-link {
        display: flex;
        align-items: center;
    }
</style>