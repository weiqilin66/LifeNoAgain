<template>
    <div>
        <div>
            <el-input placeholder="输入商品名称自动检索... " v-model="goodName" style="width: 400px;margin-right: 5px"/>
            <el-button type="warning" icon="el-icon-search" @click="showAdd">添加商品</el-button>
            <el-button type="success" icon="el-icon-search" @click="initFocus">刷新数据</el-button>

        </div>
        <div>
            <el-table
                    :data="tableData"
                    height="1150"
                    style="width:50%;margin-top: 5px"
                    :row-class-name="tableRowClassName">
                <el-table-column
                        prop="label"
                        label="☆"
                        align="center"
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="name"
                        label="商品名称"
                        align="center"
                        width="380">
                </el-table-column>

                <el-table-column
                        label="标识">
                    <template slot-scope="scope">
                        <el-switch
                                @change="changeEnabled(scope.row)"
                                v-model="scope.row.enabled">
                        </el-switch>
                    </template>
                </el-table-column>

            </el-table>

            <!--&lt;!&ndash;编辑弹窗&ndash;&gt;
            <el-dialog
                    title="编辑"
                    :visible.sync="dialogVisible"
                    width="30%">
                <div class="updateVisible">
                    <table>
                        <tr>
                            <td>
                                <el-tag size="normal">平台</el-tag>
                            </td>
                            <td>
                                <el-select v-model="updateGood.label" placeholder="请选择标题" style="margin-left: 10px">
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
                                <el-tag size="normal">商品名称</el-tag>
                            </td>
                            <td width="1000">
                                <el-input style="width: 50%;margin-left: 10px" v-model="updateGood.kw"/>
                            </td>
                        </tr>

                        <tr style="margin-top: 3px">
                            <td>
                                <el-tag size="normal">回收价</el-tag>
                            </td>
                            <td>
                                <el-input style="width: 50%;margin-left: 10px" v-model="updateGood.price"/>
                            </td>
                        </tr>
                        <tr style="margin-top: 3px">
                            <td>
                                <el-tag size="normal">库存</el-tag>
                            </td>
                            <td>
                                <el-input style="width: 50%;margin-left: 10px" v-model="updateGood.stock"
                                          @keydown.enter.native="handleUpate"/>
                            </td>
                        </tr>
                        <tr style="margin-top: 3px">
                            <td>
                                <el-tag size="normal">备注</el-tag>
                            </td>
                            <td>
                                <el-input style="width: 50%;margin-left: 10px" v-model="updateGood.comment"
                                          @keydown.enter.native="handleUpate"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <el-tag size="normal">商品标题</el-tag>
                            </td>
                            <td width="1000">
                                <el-input style="width: 100%;margin-left: 10px" v-model="updateGood.title"/>
                            </td>
                        </tr>
                    </table>
                </div>
                <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="handleUpate">确 定</el-button>
            </span>
            </el-dialog>-->
            <!--添加弹窗-->
            <el-dialog
                    title="添加"
                    :visible.sync="addDialogVisible"
                    width="30%">
                <div class="updateVisible">
                    <table>
                        <tr>
                            <td>
                                <el-tag size="normal">平台</el-tag>
                            </td>
                            <td>
                                <el-select v-model="addGood.label" placeholder="请选择标题" style="margin-left: 10px">
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
                                <el-tag size="normal">商品名称</el-tag>
                            </td>
                            <td width="1000">
                                <el-input style="width: 50%;margin-left: 10px" v-model="addGood.name"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <el-tag size="normal">关键词</el-tag>
                            </td>
                            <td width="1000">
                                <el-input style="width: 50%;margin-left: 10px" v-model="addGood.kw"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <el-tag size="normal">标识</el-tag>
                            </td>
                            <td>
                                <el-switch
                                v-model="addGood.enabled">
                                </el-switch>
                            </td>

                        </tr>
                    </table>
                </div>
                <span slot="footer" class="dialog-footer">
                <el-button @click="addDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="handleAdd">确 定</el-button>
            </span>
            </el-dialog>
        </div>

    </div>
</template>
<style>
    .el-table .warning-row {
        background: oldlace;
    }

    .el-table .success-row {
        background: #f0f9eb;
    }
</style>
<script>
    export default {
        name: "PerEmp",
        data() {
            return {
                size: '',
                total: 0,
                num: 1,
                goodName: '',
                dialogVisible: false,
                addDialogVisible: false,
                options: ["NS", "PS4"],
                addGood: {
                    label: '',
                    name: '',
                    enabled: '',
                    kw:''
                },
                goodTitles: ['输入商品名称在选择此项'],
                updateGood: {
                    label: '',
                    name: '',
                    enabled: ''
                },
                tableData: [],
                tableDataBak: [],
            }
        },
        watch: {
            goodName(val) {
                let tmp = []
                if (val) {
                    this.tableData.forEach(item => {
                        if (item.kw.includes(val)) {
                            tmp.push(item)
                        }
                    })
                    this.tableData = tmp
                } else {
                    this.tableData = this.tableDataBak
                }

                let temList = this.$store.state.bakBtnList
                if (val) {//为空重置btnList
                    let tem2 = this.$store.state.btnList
                    let tem3 = []
                    tem2.forEach(item => {
                        if (item.name.includes(val)) {
                            tem3.push(item)
                        }
                    })
                    this.$store.commit("initBtnList", tem3)
                } else {
                    this.$store.commit("initBtnList", temList)

                }


            }
        },
        mounted() {
            this.initFocus()
        },
        methods: {
            changeEnabled(row) {
                this.putRequest("/stock/focus/", row).then(resp => {
                    if (resp) {
                        this.$message.success('修改成功!')
                        this.initFocus()
                    }
                })
            },
            /*beforeAdd() {
                this.postRequest("/stock/stock1/check", this.addGood).then(resp => {
                    if (resp.data) {
                        this.$confirm(resp.data + ',是否继续添加该商品', '提示'
                            , {
                                confirmButtonText: '确定',
                                cancelButtonText: '取消',
                                type: 'warning'
                            }).then(() => {
                            this.handleAdd()
                        }).catch(() => {
                            this.$message.warning('已取消添加')
                        })
                    } else {

                        this.handleAdd()
                    }
                })
            },*/
            handleAdd() {
                this.postRequest("/stock/focus/", this.addGood).then(resp => {
                    if (resp) {
                        this.initFocus()
                        this.addDialogVisible = false
                    }
                })
            },
           /* handleUpate() {
                this.putRequest('/stock/focus/', this.updateGood).then(resp => {
                    if (resp) {
                        this.initFocus()
                        this.dialogVisible = false
                        this.$message.success("修改成功!")
                    }
                })
            },
            handleDel(index, row) {
                this.$confirm('此操作将删除商品 [ ' + row.kw + ' ] 是否继续?',
                    '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                    this.deleteRequest("/stock/focus/" + row.id).then(resp => {
                        if (resp) {
                            this.initFocus()
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
                this.initFocus()
            },*/
            showVisible(index, data) {
                this.dialogVisible = true
                Object.assign(this.updateGood, data)
            },
            showAdd() {
                this.addDialogVisible = true
                this.addGood = {}
            },
            handleChange(row) {
                Object.assign(this.updateGood, row)
                this.handleUpate()
            },
            tableRowClassName({row, rowIndex}) {
                if (row.enabled ===true) {
                    return 'success-row';
                } else {
                    return 'warning-row';
                }
            },
            initFocus() {
                this.getRequest('/stock/focus/').then(resp => {
                    if (resp) {
                        this.tableData = resp
                        this.tableDataBak = resp
                        // console.log(this.tableData);
                    }
                })
            }
        },
    }
</script>
