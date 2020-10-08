<template>
    <!--

        1爬取的商品列表
        2加工的关键字控制

        编辑添加合一
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
                    style="width:100%;margin-top: 5px"
                    border
                    :row-class-name="tableRowClassName">
                <el-table-column
                        prop="label"
                        label="☆"
                        align="center"
                        width="50">
                </el-table-column>
                <el-table-column
                        prop="name"
                        label="名称"
                        align="center"
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="stock"
                        label="库存"
                        sortable
                        align="center"
                        width="80">
                </el-table-column>
                <!--3新游 2优秀 1冷门 0封杀-->
                <el-table-column
                        prop="advance"
                        label="优先级"
                        sortable
                        align="center"
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="lastUpdate"
                        label="最后更新时间"
                        sortable
                        align="center"
                        width="200">
                    <!--花括号中无法调用自定义函数 解决：传入前format-->
                    <!--<template slot-scope="scope">
                        {{this.formatDateTime(scope.row.lastUpdate)}}
                    </template>-->
                </el-table-column>
                <el-table-column
                        prop="totalSales"
                        label="在售首页总销量"
                        sortable
                        align="center"
                        width="200">
                </el-table-column>
                <el-table-column
                        label="启用">
                    <template slot-scope="scope">
                        <el-switch
                                @change="changeEnabled(scope.row)"
                                v-model="scope.row.enabled">
                        </el-switch>
                    </template>
                </el-table-column>
                <el-table-column label="操作" align="center"
                                 width="250px">
                    <template slot-scope="scope">
                        <el-button @click="showVisible(scope.index,scope.row)">编辑</el-button>
                        <el-button type="danger" @click="handleDel(scope.index,scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!--编辑添加弹窗-->
            <el-dialog
                    title="编辑"
                    :visible.sync="dialogVisible"
                    width="40%">
                <div class="updateVisible">
                    <table>
                        <tr v-show="flag==='add'">
                            <td>
                                <el-tag size="normal">名称</el-tag>
                            </td>
                            <td >
                                <MyGoodSelect @resp="getGood"/>
                            </td>
                        </tr>

                        <tr style="margin-top: 3px">
                            <td>
                                <el-tag size="normal">优先级</el-tag>
                            </td>
                            <td>
                                <el-input style="width: 60%;margin-left: 10px" v-model="updateGood.advance"/>
                                <span style="margin-left: 8px">3新2优1冷0封杀</span>
                            </td>
                        </tr>
                        <tr></tr>
                    </table>
                </div>
                <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="beforeAdd(flag)">确 定</el-button>
            </span>
            </el-dialog>

        </div>

    </div>
</template>
<style>
    .el-table .warning-row {
        background: #fdd597;
    }

    .el-table .success-row {
        background: #d4fdef;
    }
</style>
<script>
    import MyInput from "../public/MyInput";
    import MyGoodSelect from "../public/MyGoodSelect";
    export default {
        name: "StockCrawl",
        components:{
            MyInput,
            MyGoodSelect
        },
        data() {
            return {
                cData:{
                    bak:[],
                    kw:'name' //检索字段名
                },
                flag:'',
                size:'',
                total:0,
                num: 1,
                goodName: '',
                dialogVisible: false,
                addDialogVisible: false,
                updateGood: {
                    gid:undefined,
                    label:'',
                    name:'',
                    stock:0,
                    advance:2,
                    lastUpdate: null,
                    totalSales:0,
                    enabled:true,
                },
                tableData: [],
                tableDataBak: [],
            }
        },
        mounted() {
            this.initStock()
        },
        methods: {
            getGood(data){
                this.updateGood.gid = data
            },
            changeEnabled(row) {
                row.finished = row.enabled
                this.putRequest("/stock/crawl/",row).then(resp => {
                    if (resp) {
                        this.$message.success('修改成功!')
                        // this.initStock()
                    }
                })
            },
            getCData(data){
                this.tableData=data
            },
            beforeAdd(flag){
                if (flag==='update'){
                    this.handleUpateAdd('update')

                }else {
                    this.postRequest("/stock/crawl/check", this.updateGood).then(resp => {
                        if (resp.message) {
                            this.$confirm('已存在,是否继续添加该商品','提示'
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

                            this.handleUpateAdd('add')
                        }
                    })
                }

            },

            handleUpateAdd(flag) {
                if (flag==='add') {
                    this.postRequest('/stock/crawl/',this.updateGood).then(resp=>{
                        if (resp) {
                            this.initStock()
                            this.dialogVisible = false
                            this.$message.success("添加成功!")
                        }
                    })
                }else {
                    this.putRequest('/stock/crawl/', this.updateGood).then(resp => {
                        if (resp) {
                            this.initStock()
                            this.dialogVisible = false
                            this.$message.success("修改成功!")
                        }
                    })
                }

            },
            handleDel(index, row) {
                this.$confirm('此操作将删除商品 [ ' + row.name + ' ] 是否继续?',
                    '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest("/stock/crawl/" + row.id).then(resp => {
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
                this.dialogVisible = true
                this.flag='update'
                this.updateGood={}
                Object.assign(this.updateGood, data)
            },
            showAdd() {
                this.dialogVisible = true
                this.updateGood = {}
                this.flag='add'
            },
            //库存预警
            tableRowClassName({row, rowIndex}) {
                if (row.stock > 3) {//大于3太多
                    return 'warning-row';
                } else if (row.stock <=3 && row.stock>=1) {
                    return 'success-row';
                }else if (row.stock ==-1){
                    return '';
                }
                return '';
            },
            initStock() {
                this.getRequest('/stock/crawl/').then(resp => {
                    if (resp) {
                        //格式化
                        // resp.data.forEach(item=>{
                        //     if (item.lastUpdate) {
                        //         item.lastUpdate = this.formatDateTime(item.lastUpdate)
                        //     }
                        // })
                        this.tableData = resp.data
                        this.cData.bak = resp.data
                    }
                })
            }
        },
    }
</script>
