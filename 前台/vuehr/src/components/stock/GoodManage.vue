<template>
    <!--

        商品定义

    -->
    <div>
        <div style="display: flex;">
            <MyInput :cData="cData" @resp="getCData"/>
            <el-button type="warning" icon="el-icon-search" @click="showAdd">添加商品</el-button>
            <el-button type="success" icon="el-icon-search" @click="initStock">刷新数据</el-button>

        </div>
        <div>
            <el-table
                    :data="tableData"
                    height="1150"
                    stripe
                    style="width:100%;margin-top: 15px"
                    :row-class-name="tableRowClassName">
                <el-table-column
                        prop="label"
                        label="☆"
                        align="center"
                        width="250">
                </el-table-column>
                <el-table-column
                        prop="name"
                        label="商品名称"
                        align="center"
                        width="280">
                </el-table-column>

                <el-table-column label="操作" align="center"
                                 width="350px">
                    <template slot-scope="scope">
                        <el-button @click="showVisible(scope.index,scope.row)">编辑</el-button>
                        <el-button type="danger" @click="handleDel(scope.index,scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>

            <!--编辑弹窗-->
            <el-dialog
                    title="编辑"
                    :visible.sync="updateDialogVisible"
                    width="30%">
                <div class="updateVisible">
                    <table>
                        <tr>
                            <td>
                                <el-tag size="normal">平台</el-tag>
                            </td>
                            <td>
                                <el-select v-model="updateGood.label" placeholder="请选择标题" style="margin-left: 10px" >
                                    <el-option
                                            v-for="item in options"
                                            :key="item.id"
                                            :label="item.label"
                                            :value="item.label">
                                    </el-option>
                                </el-select>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <el-tag size="normal">商品名称</el-tag>
                            </td>
                            <td width="1000">
                                <el-input style="width: 50%;margin-left: 10px" v-model="updateGood.name"/>
                            </td>
                        </tr>

                    </table>
                </div>
                <span slot="footer" class="dialog-footer">
                <el-button @click="updateDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="handleUpate">确 定</el-button>
            </span>
            </el-dialog>
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
                                <el-select v-model="addGood.label" placeholder="请选择标题" style="margin-left: 10px" >
                                    <el-option
                                            v-for="item in options"
                                            :key="item.id"
                                            :label="item.label"
                                            :value="item.label">
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

                    </table>
                </div>
                <span slot="footer" class="dialog-footer">
                <el-button @click="addDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="beforeAdd">确 定</el-button>
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
    import MyInput from "../public/MyInput";
    import MyGoodSelect from "../public/MyGoodSelect";
    export default {
        name: "GoodMain",
        components:{
            MyInput,
            MyGoodSelect
        },
        data() {
            return {
                cData:{
                    bak:[],
                    kw:'name'
                },
                size:'',
                total:0,
                num: 1,
                goodName: '',
                updateDialogVisible: false,
                addDialogVisible: false,
                addGood: {
                    label:'',
                    name:'',
                },
                updateGood: {
                    label:'',
                    name:'',
                },
                tableData: [],
                tableDataBak: [],
                options:[]
            }
        },
        mounted() {
            this.initStock()
        },
        methods: {
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
            getCData(data){
                this.tableData=data
            },
            beforeAdd(){
                this.postRequest("/noRight/goodMain/check", this.addGood).then(resp => {
                    if (!resp) {
                        this.$confirm(',是否继续添加该商品','提示'
                            ,{
                                confirmButtonText: '确定',
                                cancelButtonText: '取消',
                                type: 'warning'
                            }).then(()=>{
                                this.handleAdd()
                        }).catch(()=>{
                            this.$message.warning('已取消添加')
                        })
                    }else {

                        this.handleAdd()
                    }
                })
            },
            handleAdd() {
                this.postRequest("/noRight/goodMain/", this.addGood).then(resp => {
                    if (resp) {
                        this.initStock()
                        this.addDialogVisible = false
                        this.$message.success("添加成功!")
                        this.getKeyWord()
                    }
                })
            },
            handleUpate() {
                this.putRequest('/noRight/goodMain/', this.updateGood).then(resp => {
                    if (resp) {
                        this.initStock()
                        this.updateDialogVisible = false
                        this.$message.success("修改成功!")
                        this.getKeyWord()
                    }
                })
            },
            handleDel(index, row) {
                this.$confirm('此操作将删除商品 [ ' + row.label+row.name + ' ] 是否继续?',
                    '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest("/noRight/goodMain/" + row.id).then(resp => {
                        if (resp) {
                            this.initStock()
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            this.initStock()
            },
            showVisible(index, data) {
                this.updateDialogVisible = true
                Object.assign(this.updateGood, data)
            },
            showAdd() {
                this.addDialogVisible = true
                this.addGood = {}
            },
            handleChange(row) {
                Object.assign(this.updateGood,row)
                this.handleUpate()
            },
            tableRowClassName({row, rowIndex}) {
                if (row.diff < 15) {
                    return 'warning-row';
                } else if (row.diff >25) {
                    return 'success-row';
                }else if (row.diff ==-1){
                    return '';
                }
                return '';
            },
            initStock() {
                this.getRequest('/noRight/goodMain/').then(resp => {
                    if (resp) {
                        this.tableData = resp.data
                        this.cData.bak = resp.data
                    }
                })
                this.getRequest("/noRight/cache/goodLabel").then(resp=>{
                    if (resp) {
                        this.options=resp.data
                    }
                })
            }
        },
    }
</script>
