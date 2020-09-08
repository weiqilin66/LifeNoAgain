<template>
    <div>
        <!--添加-->
        <div>
            <el-input
                    size="small"
                    class="addInput"
                    placeholder="请输入职位..."
                    prefix-icon="el-icon-circle-plus"
                    @keydown.enter.native="handleAdd"
                    v-model="pos.name">
            </el-input>
            <el-button size="small" icon="el-icon-circle-plus" type="primary" @click="handleAdd">添加</el-button>
            <el-button size="small" icon="el-icon-delete" type="danger" @click="handleBatchDel"
                       :disabled="multiSelection.length==0">批量删除
            </el-button>
        </div>
        <!--表格data-->
        <div class="mainPos">
            <el-table
                    :data="tableData"
                    border
                    style="width: 60%"
                    @selection-change="handleSelectionChange"
                    :row-class-name="tableRowClassName">
                <!--width: 60% 设置表格布局宽度-->
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="id"
                        label="编号"
                        width="55">
                </el-table-column>
                <el-table-column
                        prop="name"
                        label="职位"
                        width="200">
                </el-table-column>
                <el-table-column
                        prop="createDate"
                        label="创建时间"
                        width="120">
                </el-table-column>
                <el-table-column
                        label="操作"
                        width="">
                    <template slot-scope="scope">
                        <el-button
                                size="mini"
                                @click="showEditVisible(scope.$index, scope.row)">编辑
                        </el-button>
                        <el-button
                                size="mini"
                                type="danger"
                                @click="handleDelete(scope.$index, scope.row)">删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <!--弹窗-->
        <el-dialog
                title="编辑"
                :visible.sync="dialogVisible"
                width="30%">
            <div class="updateVisible">
                <el-tag size="normal">职位名称</el-tag>
                <el-input style="width: 50%;margin-left: 10px" v-model="updatePos.name"
                          @keydown.enter.native="handleUpate"/>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="handleUpate">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        name: "PosMana",
        data() {
            return {
                pos: {
                    name: ''
                },
                // 切换组件时 使用这个中间变量拷贝row值
                updatePos: {
                    id: '',
                    name: ''
                },
                multiSelection: [],
                dialogVisible: false,
                tableData: []
            }
        },
        mounted() {
            this.initPos()
        },
        methods: {
            handleSelectionChange(val) {
                this.multiSelection = val
            },
            // 批量删除,向后端传递数组
            handleBatchDel() {
                this.$confirm('此操作将批量删除数据,是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let ids = '?'
                    this.multiSelection.forEach(item => {
                        ids += 'ids=' + item.id +'&'
                    })
                    this.deleteRequest('/system/basic/pos/' + ids).then(resp => {
                        if (resp) {
                            this.initPos()
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            // 弹窗
            showEditVisible(index, data) {
                /*this.updatePos.name = data.name
                this.updatePos.id = data.id
                使用Object.assign()拷贝属性值,不能使用this.updatePos=data,会使得updatePos改变时data所指向的数值也会改变,vue的数据绑定特性如此*/
                Object.assign(this.updatePos, data)
                this.dialogVisible = true
            },
            // 更新
            handleUpate() {
                console.log(this.updatePos)
                this.putRequest('/system/basic/pos/', this.updatePos).then(resp => {
                    if (resp) {
                        this.initPos()
                        this.dialogVisible = false
                    }
                })
            },
            // 删除
            handleDelete(index, data) {
                this.$confirm('此操作将删除<' + data.name + '>, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest('/system/basic/pos/' + data.id).then(resp => {
                        if (resp) {
                            this.initPos()
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            // 添加
            handleAdd() {
                if (this.pos.name === '') {
                    this.$message.warning("请输入职位")
                    return
                }
                this.postRequest('/system/basic/pos/', this.pos).then(resp => {
                    if (resp) {
                        this.initPos()
                        this.pos.name = ''
                    }
                })
            },
            // 初始化
            initPos() {
                this.getRequest('/system/basic/pos/').then(resp => {
                    if (resp) {
                        this.tableData = resp;
                    }
                })
            },
            // 表格class切换
            tableRowClassName({row, rowIndex}) {
                return rowIndex % 2 === 0 ? 'warning-row' : 'success-row'
            }
        }
    }
</script>

<style>
    .addInput {
        width: 300px;
        margin-right: 15px
    }

    .mainPos {
        margin-top: 10px;
    }

    .el-table .warning-row {
        background: oldlace;
    }

    .el-table .success-row {
        background: #f0f9eb;
    }

    .updateVisible {
        display: flex;
        justify-content: center;

    }
</style>