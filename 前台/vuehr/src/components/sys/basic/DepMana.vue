<template>
    <div style="width: 500px;">
        <el-input
                prefix-icon="el-icon-search"
                placeholder="输入要搜索的部门"
                v-model="filterText">
        </el-input>

        <el-tree
                class="filter-tree"
                :data="deps"
                :props="defaultProps"
                :filter-node-method="filterNode"
                :expand-on-click-node="false"
                ref="tree"
                style="margin-top: 5px">
            <!--加了自定义之后显示的node相当于props的label和children-->
            <span class="custom-tree-node" style="display: flex;justify-content: space-between;width: 100%"
                  slot-scope="{ node, data }">
                <span>{{ node.label }}</span>
                <span>
                  <el-button
                          type="primary"
                          style="margin-right: 1px"
                          class="depBtn"
                          size="normal"
                          @click="() => showVisible(data)">
                    添加
                  </el-button>
                  <el-button
                          type="danger"
                          class="depBtn"
                          size="normal"
                          @click="() => removeDep(data)">
                    删除
                  </el-button>
                </span>
            </span>
        </el-tree>
        <el-dialog
                title="添加部门"
                :visible.sync="dialogVisible"
                width="30%">
            <div style="margin-left: 50px">
                <div>
                    <el-tag size="normal">上级部门</el-tag>
                    <span style="margin-left: 5px">{{parentName}}</span>
                </div>
                <div style="display: flex;margin-top: 2px">
                    <el-tag size="normal">部门名称</el-tag>
                    <el-input placeholder="请输入部门名称" v-model="addDep.name" style="margin-left: 5px;width: 50%" @keydown.enter.native="handleAdd"/>
                </div>
            </div>
            <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="handleAdd">确 定</el-button>
  </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "DepMana",
        data() {
            return {
                filterText: '',
                deps: [],
                parentName: '',
                addDep: {
                    name: '',
                    parentId: -1
                },
                dialogVisible: false,
                defaultProps: {
                    children: 'children',
                    label: 'name'
                }

            }
        },
        watch: {
            // 监控 filterText 变量的变化 val为filterText的最新值
            filterText(val) {
                // filter执行的方法为 :filter-node-method 定义的方法 filterNode
                this.$refs.tree.filter(val);
            }
        },
        mounted() {
            this.initDeps()
        },
        methods: {
            // value时监控方法里的val值 data是tree每一项的json
            // 执行逻辑是多次调用过滤data里的值!==-1为不符合条件的json
            filterNode(value, data) {
                if (!value) return true;
                return data.name.indexOf(value) !== -1;
            },
            initDeps() {
                this.getRequest("/system/basic/dep/").then(resp => {
                    if (resp) {
                        this.deps = resp
                    }
                })
            },
            showVisible(data) {
                this.parentName = data.name
                this.addDep.parentId = data.id
                this.dialogVisible = true
            },
            handleAdd() {
                this.dialogVisible = false
                this.postRequest("/system/basic/dep/", this.addDep).then(resp => {
                    if (resp) {
                        // this.initDeps() 为了动态刷新不能直接初始化会关闭tree
                        this.addDep2Deps(this.deps, resp.data)
                        this.addDep.name = ''
                    }
                })
            },
            addDep2Deps(deps, dep) {
                for (let i = 0; i < deps.length; i++) {
                    if (deps[i].id === dep.parentId) {
                        deps[i].children = deps[i].children.concat(dep)
                        return
                    } else {
                        // 当前项不是添加进来的dep的上级dep 回调判断该级的子部门是不是添加部门的上级部门
                        this.addDep2Deps(deps[i].children, dep)
                    }
                }
            },
            removeDepFromDeps(deps,repId){
                for (let i = 0; i < deps.length; i++) {
                    if (deps[i].id === repId) {
                        // splice 操作数组 从第(x,y) x个开始 删除y个
                        deps.splice(i,1)
                        return
                    }else {
                        this.removeDepFromDeps(deps[i].children,repId)
                    }
                }

            },
            removeDep(data) {
                /*if (data.isParent) {  前端判断存在缓存问题 使用后端校验是否是父部门
                    this.$message.warning("无法删除有子部门的父部门");
                    return
                }*/
                this.$confirm('此操作将删除该部门' + data.name + ' 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest("/system/basic/dep/" + data.id).then(resp => {
                        if (resp) {
                            this.removeDepFromDeps(this.deps,data.id)
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            }
        }


    }
</script>

<style>
    .depBtn {
        padding: 2px;
    }
</style>