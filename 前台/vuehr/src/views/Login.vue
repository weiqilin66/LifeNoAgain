<template>
    <div>
        <el-form
                :rules="rules"
                ref="loginForm"
                :model="loginForm"
                v-loading.fullscreen.lock="fullscreenLoading"
                class="loginContainer">
            <h3 class="loginTitle">系统登录</h3>
            <el-form-item prop="username">
                <el-input size="normal" type="text" auto-complete="off" placeholder="请输入用户名" v-model="loginForm.username"/>
            </el-form-item>
            <el-form-item prop="password">
                <el-input size="normal" type="password" auto-complete="off" placeholder="请输入密码" v-model="loginForm.password"
                @keydown.enter.native="submitForm"/>
                <!--enter键登录-->
            </el-form-item>
            <font size="2px">Remember</font>
            <el-checkbox size="normal" class="loginRemember" v-model="checked"/>
            <el-button size="normal" type="primary" style="width:100%" @click="submitForm">登录</el-button>
        </el-form>
    </div>
</template>

<script>
    export default {
        name: "Login",
        data() {
            return {
                loginForm: {
                    username: 'admin',
                    password: '123'
                },
                checked: true,
                fullscreenLoading:false,
                rules: {
                    username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
                    password: [{required: true, message: '请输入密码', trigger: 'blur'}]
                }
            }
        },
        methods: {
            submitForm() {
                this.fullscreenLoading = true
                this.$refs.loginForm.validate((valid) => {
                    if (valid) {
                        this.postKeyValueRequest('/doLogin', this.loginForm).then(resp => {
                            this.fullscreenLoading = false
                            // Axios封装已经判断http状态,此时的response就是后端的respBean
                            if (resp) {
                                // 用户数据保存到sessionStorage,浏览器关闭就消失
                                window.sessionStorage.setItem('user', JSON.stringify(resp.data))
                                //获取当前的url路径的redirect的value
                                let path = this.$route.query.redirect;
                                // router push() 可后退 replace() 替换不可后退
                                this.$router.replace((path==='/'||path===undefined)?'/home':path)
                            }
                        })
                        //伪登录
                        /*const user = {
                            username: 'admin',
                            password: '123'
                        }
                        window.sessionStorage.setItem('user', JSON.stringify(user))*/

                        //获取当前的url路径的redirect的value
                        let path = this.$route.query.redirect;
                        // router push() 可后退 replace() 替换不可后退
                        this.$router.replace((path==='/'||path===undefined)?'/home':path).catch(err=>{err})

                    } else {
                        this.$message.error('请确认输入用户名及密码')
                        return false;
                    }
                });
            },
        }
    }
</script>

<style>
    .loginContainer {
        border-radius: 15px;
        background-clip: padding-box;
        margin: 180px auto;
        width: 350px;
        padding: 15px 35px 15px 35px;
        background: #fff;
        border: 1px solid #eaeaea;
        box-shadow: 0 0 25px #cac6c6;
    }

    .loginTitle {
        margin: 15px auto 20px auto;
        text-align: center;
        color: #505458;
    }

    .loginRemember {
        text-align: left;
        margin: 0px 0px 15px 0px;

    }
</style>