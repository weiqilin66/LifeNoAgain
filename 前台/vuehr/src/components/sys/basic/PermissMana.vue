<template>
    <div>
        <div class="menuDiv">
            <el-input placeholder="请输入英文角色名" style="width:25%;margin-right: 5px" v-model="role.name">
                <template slot="prepend">ROLE_</template>
            </el-input>
            <el-input placeholder="请输入中文角色名" v-model="role.nameZh" style="width:25%;margin-right: 5px">
            </el-input>
            <el-button type="primary" icon="el-icon-plus" @click="handleAdd">添加角色</el-button>
        </div>
        <div style="margin-top: 10px ; width:50%">
            <el-collapse v-model="activeName" accordion @change="change">
                <el-collapse-item :title="r.nameZh" :name="r.id" v-for="(r,index) in roles" :key="index">
                    <el-card class="box-card">
                        <div slot="header" class="clearfix">
                            <span>可访问的资源</span>
                            <el-button style="float: right; padding: 3px 0;color:#ff0000;" icon="el-icon-delete"
                                       size="normal" @click="handleDel(r.id)"/>
                        </div>
                        <div>
                            <el-tree :data="menuList" :props="defaultProps"
                                     show-checkbox
                                     node-key="id"
                                     :key="index"
                                     :default-checked-keys="selectedMenus"
                                     ref="tree"
                            />
                            <div style="display: flex;justify-content: flex-end">
                            <el-button @click="cancelUpdate(index)">取消修改</el-button>
                            <el-button type="primary" @click="handleUpdate(r.id,index)">确认修改</el-button>
                            </div>
                        </div>
                    </el-card>
                </el-collapse-item>
            </el-collapse>
        </div>

    </div>
</template>

<script>
    export default {
        name: "PermissMana",
        data() {
            return {
                role: {
                    name: '',
                    nameZh: ''
                },
                roles: {},
                menuList: [],
                selectedMenus:[],
                activeName: -1,
                defaultProps: {
                    children: 'children',
                    label: 'name'
                }
            }

        },
        mounted() {
            this.initPs()
            this.getMenus()
        },
        methods: {
            handleDel(rid){
                this.deleteRequest("/system/basic/ps/"+rid).then(resp=>{
                    if (resp) {
                        this.initPs()
                    }
                })
            },
            handleAdd(){
                if (this.role.nameZh && this.role.name) {
                    this.postRequest("/system/basic/ps/",this.role).then(resp=>{
                        if (resp) {
                            this.initPs()
                        }
                    })
                }else {
                    this.$message.error("输入角色中文名和英文名")
                }
            },
            cancelUpdate(index){
               /* this.activeName = -1
                this.$refs.tree[index].setCheckedKeys = this.selectedMenus*/
               // 未解决问题1 临时使用刷新页面
                location.reload();
            },
            handleUpdate(rid,index){
                // ref="xx" 给dom取名 $refs.xx 获取当前页面该dom对象 多个xx时,得到的是一个数组 r

                let tree = this.$refs.tree[index]  // 当前展开的tree
                let keys = tree.getCheckedKeys(true) // true只返回选中子节点
                let url = '/system/basic/ps/?rid='+rid
                keys.forEach(mid =>{
                    url+='&mids=' + mid
                })
                this.putRequest(url).then(resp=>{
                    if (resp) {
                        // 关闭展开的页面
                        this.activeName = -1
                    }
                })
            },
            change(rid) {
                // change事件只触发开始的点击事件,不触发关闭
                if (rid) {
                    // 预选中
                    this.getPermissionsId(rid)
                }
            },
            getPermissionsId(rid){
              this.getRequest("/system/basic/ps/mid/"+rid).then(resp=>{
                  if (resp) {
                      this.selectedMenus = resp
                  }
              })
            },
            getMenus() {
                this.getRequest("/system/basic/ps/menus").then(resp => {
                    if (resp) {
                        this.menuList = resp
                    }
                })
            },
            initPs() {
                this.getRequest('/system/basic/ps/').then(resp => {
                    if (resp) {
                        this.roles = resp
                    }
                })
            }
        }
    }
</script>

<style scoped>
    .menuDiv {
        display: flex;

    }
</style>