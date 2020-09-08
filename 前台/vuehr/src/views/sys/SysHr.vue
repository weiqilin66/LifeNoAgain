<template>
    <div>
        <div style="display: flex; justify-content: center;margin-top: 10px; ">
            <el-input prefix-icon="el-icon-search" v-model="keyWord" style="width: 600px;margin-right: 5px"
                      size="normal"
                      placeholder="输入要搜索的用户名..."/>
            <el-button type="primary" @click="doSearch">搜索</el-button>
        </div>
        <div class="hr-container">
            <div v-for="(hr,hIndex) in hrs" :key="hIndex" style="width: 500px">
                <el-card class="hr-card">
                    <div slot="header" style="display: flex;justify-content: space-between">
                        <span>{{hr.name}}</span>
                        <el-button type="danger" icon="el-icon-delete" @click="doDelete(hr)"/>
                    </div>
                    <div>
                        <div class="img-container">
                            <img :src="hr.userface" :alt="hr.name" :title="hr.name" class="userface-img">
                        </div>
                        <div class="userinfo-container">
                            <div>用户名：{{hr.name}}</div>
                            <div>手机号码：{{hr.phone}}</div>
                            <div>地址：{{hr.address}}</div>
                            <div>用户状态：
                                <el-switch
                                        v-model="hr.enabled"
                                        active-text="启用"
                                        active-color="#13ce66"
                                        inactive-color="#ff4949"
                                        @change="change(hr)"
                                        inactive-text="禁用">
                                </el-switch>
                            </div>
                            <div>用户角色：
                                <el-tag type="success" style="margin-right: 4px" v-for="(role,indexj) in hr.roles"
                                        :key="indexj">{{role.nameZh}}
                                </el-tag>
                                <el-popover
                                        placement="right"
                                        title="角色列表"
                                        @show="showPop(hr)"
                                        @hide="hidePop(hr)"
                                        width="200"
                                        trigger="click">
                                    <!--弹出框里面放下拉框-->
                                    <el-select v-model="selectedRoles" multiple placeholder="请选择">
                                        <el-option
                                                v-for="(r,rIndex) in allRoles"
                                                :key="rIndex"
                                                :label="r.nameZh"
                                                :value="r.id">
                                        </el-option>
                                    </el-select>
                                    <el-button slot="reference" icon="el-icon-more" type="text"></el-button>
                                </el-popover>
                            </div>
                            <div>备注：{{hr.remark}}</div>
                        </div>
                    </div>
                </el-card>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "SysHr",
        data() {
            return {
                keyWord: '',
                hrs: [],
                hr: [],
                allRoles: [],
                selectedRoles: [],
                oldHrs:[]
            }
        },
        mounted() {
            this.initHr()
        },
        methods: {
            doDelete(hr){
                this.$confirm('此操作将删除角色['+hr.name+'] 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                   this.deleteRequest('/system/hr/'+hr.id).then(resp=>{
                       if (resp) {
                           this.initHr()
                       }
                   })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            // 执行更新
            hidePop(hr) {
                let url = '/system/hr/roles?hrid=' + hr.id
                this.selectedRoles.forEach(i => {
                    url += '&rids=' + i
                })
                this.putRequest(url).then(resp=>{
                    if (resp) {
                        this.initHr()
                    }
                })
            },
            showPop(hr) {
                this.getAllRoles()
                // 预选中 el-select中value的值放入 v-model的属性中
                let roles = hr.roles
                this.selectedRoles =[]
                roles.forEach(r => {
                    this.selectedRoles.push(r.id)
                })
            },
            change(hr) {
                delete hr.roles
                this.putRequest("/system/hr/", hr).then(resp => {
                    if (resp) {
                        this.initHr()
                    } else {
                        hr.enabled = !hr.enabled
                    }
                })
            },
            getAllRoles() {
                this.getRequest('/system/hr/roles').then(resp => {
                    if (resp) {
                        this.allRoles = resp
                    }
                })
            },
            // 前端实现检索
            doSearch() {
                this.hrs = []
                Object.assign(this.hrs,this.oldHrs)
                let searchHrs = []
                this.hrs.forEach(hr=>{
                    if (!hr.name.indexOf(this.keyWord)) {
                        searchHrs.push(hr)
                    }
                })
                // 为空检索全部
                if (this.keyWord===''){
                    Object.assign(this.hrs,this.oldHrs)
                    return
                }
                this.hrs = searchHrs

            },
            initHr() {
                this.getRequest("/system/hr/").then(resp => {
                    if (resp) {
                        this.hrs = resp
                        Object.assign(this.oldHrs,this.hrs)
                    }
                })
            }
        }
    }
</script>

<style>
    .userinfo-container div {
        font-size: 15px;
        color: #409eff;
    }

    .userinfo-container {
        margin-top: 20px;
    }

    .img-container {
        width: 100%;
        display: flex;
        justify-content: center;
    }

    .userface-img {
        width: 150px;
        height: 150px;
        border-radius: 72px;
    }

    .hr-container {
        margin-top: 10px;
        display: flex;
        flex-wrap: wrap;
        justify-content: space-around;
    }

    .hr-card {
        width: 500px;
        margin-bottom: 20px;
    }
</style>