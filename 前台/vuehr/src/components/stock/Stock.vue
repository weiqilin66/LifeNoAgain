<template>
    <!--

        商品库存

    -->
    <div>
        <div style="display: flex;">
            <MyInput :cData="cData" @resp="getCData"/>
            <el-button type="warning" icon="el-icon-search" @click="showAdd">添加商品</el-button>
            <el-button type="primary" icon="el-icon-search" @click="initStock">刷新数据</el-button>
            <el-button type="warning" icon="el-icon-search" @click="refresh">刷新预警</el-button>

        </div>
        <div>
            <el-table
                    :data="tableData"
                    style="width:100%;margin-top: 5px"
                    show-summary
                    :summary-method="getSummaries"
                    :row-class-name="tableRowClassName"><!--合计不能有固定高度-->
                <el-table-column
                        prop="label"
                        label="☆"
                        align="center"
                        width="50">
                </el-table-column>
                <el-table-column
                        prop="name"
                        label="商品名称"
                        align="center"
                        width="180">
                </el-table-column>

                <el-table-column
                        label="库存"
                        sortable
                        align="center"
                        prop="stock"
                        width="280">
                    <template slot-scope="scope">
                        <el-input-number v-model="scope.row.stock" @change="handleChange(scope.row)"
                                         :min="0" :max="9999"/>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="total"
                        label="总额"
                        align="center"
                        width="80">
                </el-table-column>
                <el-table-column
                        prop="price"
                        label="我的价格"
                        align="center"
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="comment"
                        label="建议价"
                        align="center"
                        width="400">
                </el-table-column>

                <el-table-column label="操作" align="center"
                                 width="250px">
                    <template slot-scope="scope">
                        <el-button @click="showVisible(scope.index,scope.row)">编辑</el-button>
                        <el-button type="danger" @click="handleDel(scope.index,scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!--编辑弹窗-->
            <el-dialog
                    title="编辑"
                    :visible.sync="dialogVisible"
                    width="30%">
                <div class="updateVisible">
                    <table>
                        <tr style="margin-top: 3px">
                            <td>
                                <el-tag size="normal">名称</el-tag>
                            </td>
                            <td>
                                <el-input style="width: 80%;margin-left: 10px"  disabled
                                          v-model="updateGood.label+updateGood.name"/>
                            </td>
                        </tr>
                        <tr style="margin-top: 3px">
                            <td>
                                <el-tag size="normal">价格</el-tag>
                            </td>
                            <td>
                                <el-input style="width: 80%;margin-left: 10px" v-model="updateGood.price"/>
                            </td>
                        </tr>
                        <tr style="margin-top: 3px">
                            <td>
                                <el-tag size="normal">库存</el-tag>
                            </td>
                            <td>
                                <el-input style="width: 80%;margin-left: 10px" v-model="updateGood.stock"
                                          @keydown.enter.native="handleUpate"/>
                            </td>
                        </tr>
                        <tr style="margin-top: 3px">
                            <td>
                                <el-tag size="normal">备注</el-tag>
                            </td>
                            <td>
                                <el-input style="width: 80%;margin-left: 10px" v-model="updateGood.comment"
                                          @keydown.enter.native="handleUpate"/>
                            </td>
                        </tr>
                    </table>
                </div>
                <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
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
                                <el-tag size="normal">商品标题</el-tag>
                            </td>
                            <td>
                                <MyGoodSelect @resp="getGood"/>
                            </td>

                        </tr>
                        <tr style="margin-top: 3px">
                            <td>
                                <el-tag size="normal">价格</el-tag>
                            </td>
                            <td>
                                <el-input style="width: 50%;margin-left: 10px" v-model="addGood.price"/>
                            </td>
                        </tr>
                        <tr style="margin-top: 3px">
                            <td>
                                <el-tag size="normal">库存</el-tag>
                            </td>
                            <td>
                                <el-input style="width: 50%;margin-left: 10px" v-model="addGood.stock"
                                          @keydown.enter.native="handleAdd"/>
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
        name: "Stock",
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
                dialogVisible: false,
                addDialogVisible: false,
                addGood: {
                    gid:'',
                    label:'',
                    name:'',
                    price: '',
                    stock: '',
                    comment:''
                },
                updateGood: {
                    gid:'',
                    label:'',
                    kw:'',
                    title: '',
                    price: '',
                    stock: '',
                    comment:''
                },
                tableData: [],
                tableDataBak: [],
            }
        },
        mounted() {
            this.initStock()
        },
        methods: {
            refresh(){
                this.getRequest("/price/?msg="+'setLow').then(resp=>{
                    if (resp) {
                        this.initStock();
                    }
                })
            },
            //自定义统计
            getSummaries(param) {
                const { columns, data } = param;
                const sums = [];
                columns.forEach((column, index) => {
                    if (index === 0) {
                        sums[index] = '合计';
                        return;
                    }
                    const values = data.map(item => Number(item[column.property]));
                    if (!values.every(value => isNaN(value))) {
                        sums[index] = values.reduce((prev, curr) => {//pre累计值，curr现在值
                            const value = Number(curr);
                            if (!isNaN(value)) {//不为空做累加
                                return prev + curr;
                            } else {
                                return prev;
                            }
                        }, 0);
                        if (index === 3) {
                            sums[index] += ' 元';
                        }else {
                            sums[index] += ' 件';
                        }

                    } else {
                        sums[index] = '';
                    }
                });
                sums[4]='';
                sums[5]='';
                return sums;
            },
            getGood(data){
                this.addGood.gid = data
            },
            getCData(data){
                this.tableData=data
            },
            pageChange(currentPage) {
                this.page = currentPage
                this.initData()
            },
            sizeChange(currentSize) {
                this.size = currentSize
                this.initData()
            },
            selTitle(kw){
              this.getRequest("/stock/stock1/getTitle?kw="+kw).then(resp=>{
                  if (resp) {
                      this.goodTitles = resp.data
                  }
              })
            },
            beforeAdd(){
                this.postRequest("/stock/stock1/check", this.addGood).then(resp => {
                    if (resp.data) {
                        this.$confirm(resp.data+',是否继续添加该商品','提示'
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
                this.postRequest("/stock/stock1/", this.addGood).then(resp => {
                    if (resp) {
                        this.initStock()
                        this.addDialogVisible = false
                        this.$message.success("添加成功")
                    }
                })
            },
            handleUpate() {
                this.putRequest('/stock/stock1/', this.updateGood).then(resp => {
                    if (resp) {
                        this.initStock()
                        this.dialogVisible = false
                        this.$message.success("修改成功!")
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
                    this.deleteRequest("/stock/stock1/" + row.id).then(resp => {
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
                this.getRequest('/stock/stock1/').then(resp => {
                    if (resp) {
                        this.tableData = resp.data
                        this.cData.bak = resp.data
                    }
                })
            }
        },
    }
</script>
