<template>
    <div>
        <div>
            <el-input prefix-icon="el-icon-circle-plus" v-model="JL.name" style="width: 300px;margin-right: 5px"
                      placeholder="添加职称..."/>
            <el-select v-model="JL.titleLevel" placeholder="职称等级">
                <el-option
                        v-for="item in options"
                        :key="item"
                        :label="item"
                        :value="item">
                </el-option>
            </el-select>
            <el-button icon="el-icon-circle-plus" type="primary" @click="handleAdd"
                       style="margin-right: 5px;margin-left: 5px">添加
            </el-button>
            <el-button type="danger" style="margin-left: 5px" @click="handleBatchDel"
                       :disabled="multipleSelection.length===0">批量删除
            </el-button>
        </div>
        <div style="margin-top: 10px">
            <el-table
                    :data="tableData"
                    border
                    @selection-change="handleSelectionChange"
                    style="width: 70%">
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="id"
                        label="编号"
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="name"
                        label="职称"
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="titleLevel"
                        label="职级">
                </el-table-column>
                <el-table-column
                        prop="createDate"
                        label="创建时间"
                        width="120">
                </el-table-column>
                <el-table-column
                        label="启用">
                    <template slot-scope="scope">
                        <el-switch
                                v-model="scope.row.enabled"
                                disabled>
                        </el-switch>
                    </template>
                </el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-button @click="showVisible(scope.index,scope.row)">编辑</el-button>
                        <el-button type="danger" @click="handleDel(scope.index,scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <!--弹窗-->
        <el-dialog
                title="编辑"
                :visible.sync="dialogVisible"
                width="20%">
            <div>
                <table>
                    <tr>
                        <td>
                            <el-tag size="normal">职称</el-tag>
                        </td>
                        <td>
                            <el-input style="width: 100%;margin-left: 0px" v-model="updateJL.name"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <el-tag size="normal" style="margin-right: 10px">职级</el-tag>
                        </td>
                        <td>
                            <el-select v-model="updateJL.titleLevel" placeholder="职级">
                                <el-option
                                        v-for="item in options"
                                        :key="item"
                                        :label="item"
                                        :value="item">
                                </el-option>
                            </el-select>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <el-tag size="normal">启用</el-tag>
                        </td>
                        <td>
                            <el-switch v-model="updateJL.enabled">
                            </el-switch>
                        </td>
                    </tr>
                </table>

                <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="handleUpate">确 定</el-button>
            </span>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "JobLevelMana",
        data() {
            return {
                JL: {
                    name: '',
                    titleLevel: ''
                },
                updateJL: {
                    name: '',
                    titleLevel: '',
                    enabled: ''
                },
                options: [
                    '正高级',
                    '副高级',
                    '初级',
                    '中级',
                    '员级'
                ],
                tableData: [],
                dialogVisible: false,
                multipleSelection: []
            }
        },
        mounted() {
            this.initJL();
        },
        methods: {
            handleAdd() {
                if (this.JL.name === '' || this.JL.titleLevel === '') {
                    this.$message.warning("职称和职级为必填项!")
                    return
                }
                this.postRequest('/system/basic/jl/', this.JL).then(resp => {
                    if (resp) {
                        this.initJL();
                        this.JL.name = ''
                        this.JL.titleLevel = ''
                        // empBasic页面联动
                        if (window.sessionStorage.getItem('joblevels')) {
                            window.sessionStorage.removeItem('joblevels')
                        }
                    }
                })
            },
            showVisible(index, data) {
                this.dialogVisible = true
                Object.assign(this.updateJL, data)
            },
            handleUpate() {
                this.putRequest('/system/basic/jl/', this.updateJL).then(resp => {
                    if (resp) {
                        this.dialogVisible = false
                        this.initJL()
                    }
                })
            },
            handleDel(index, data) {
                this.$confirm('此操作将删除职称[' + data.name + ']是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('system/basic/jl/' + data.id).then(resp => {
                        if (resp) {
                            this.initJL()
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            handleSelectionChange(val) {
                this.multipleSelection = val
            },
            handleBatchDel() {
                this.$confirm('此操作将批量删除数据是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let ids = '?'
                    this.multipleSelection.forEach(item => {
                        ids += 'ids=' + item.id + '&'
                    })
                    this.deleteRequest('/system/basic/jl/' + ids).then(resp => {
                        if (resp) {
                            this.initJL()
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });

            },
            initJL() {
                this.getRequest('system/basic/jl/').then(resp => {
                    if (resp) {
                        this.tableData = resp;
                    }
                })
            }

        }
    }
</script>

<style scoped>

</style>