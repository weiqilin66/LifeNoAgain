<template>
    <div>
        <div style="display: flex;">
<!--            <el-input placeholder="输入商品名称自动检索... " v-model="goodName" style="width: 400px;margin-right: 5px"/>-->
            <MyInput :cData="cData" @resp="getCData"/>
            <el-button type="warning" icon="el-icon-search" @click="showAdd">添加商品</el-button>
            <el-button type="success" icon="el-icon-search" @click="initStock">刷新数据</el-button>

        </div>
        <div>
            <el-table
                    :data="tableData"
                    height="1150"
                    style="width:100%;margin-top: 5px"
                    :row-class-name="tableRowClassName">
                <el-table-column
                        prop="label"
                        label="☆"
                        align="center"
                        width="50">
                </el-table-column>
                <el-table-column
                        prop="kw"
                        label="商品名称"
                        align="center"
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="price"
                        label="回收价"
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
                        prop="diff"
                        label="差价"
                        sortable
                        align="center"
                        width="80">
                </el-table-column>
                <el-table-column
                        prop="price2"
                        label="猎人价"
                        align="center"
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="title"
                        label="商品标题"
                        align="center"
                        width="400">
                </el-table-column>
                <el-table-column
                        prop="comment"
                        label="备注"
                        align="center"
                        width="100">
                </el-table-column>
                <el-table-column label="操作" align="center"
                                 width="250px">
                    <template slot-scope="scope">
                        <el-button @click="showVisible(scope.index,scope.row)">编辑</el-button>
                        <el-button type="danger" @click="handleDel(scope.index,scope.row)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!--分页-->
            <!--<div style="display: flex;justify-content: flex-end">
                <el-pagination
                        ref="pagination"
                        background
                        @current-change="pageChange"
                        @size-change="sizeChange"
                        :page-sizes="[20,30,50,100]"
                        layout="sizes, prev, pager, next, jumper, ->, total, slot"
                        :total="total">
                </el-pagination>
            </div>-->
            <!--编辑弹窗-->
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
                                <el-select v-model="updateGood.label" placeholder="请选择标题" style="margin-left: 10px" >
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
                            <td width="1000" >
                                <el-input style="width: 100%;margin-left: 10px" v-model="updateGood.title"/>
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
                                <el-tag size="normal">平台</el-tag>
                            </td>
                            <td>
                                <el-select v-model="addGood.label" placeholder="请选择标题" style="margin-left: 10px" >
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
                                <el-input style="width: 50%;margin-left: 10px" v-model="addGood.kw"
                                @change = "selTitle(addGood.kw)"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <el-tag size="normal">商品标题</el-tag>
                            </td>
                            <td>
                            <el-select v-model="addGood.title" placeholder="请选择标题" style="margin-left: 10px" >
                                <el-option
                                        v-for="item in goodTitles"
                                        :key="item"
                                        :label="item"
                                        :value="item">
                                </el-option>
                            </el-select>
                            </td>
                           <!--
                            <td width="1000">
                                <el-input style="width: 50%;margin-left: 10px" v-model="addGood.title"/>
                            </td>-->
                        </tr>
                        <tr style="margin-top: 3px">
                            <td>
                                <el-tag size="normal">回收价</el-tag>
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
    import MyInput from "../../components/public/MyInput";

    export default {
        name: "PerEmp",
        components:{
            MyInput,
        },
        data() {
            return {
                cData:{
                    bak:[],
                    kw:'kw'
                },
                size:'',
                total:0,
                num: 1,
                goodName: '',
                dialogVisible: false,
                addDialogVisible: false,
                options:["NS","PS4"],
                addGood: {
                    label:'',
                    kw:'',
                    title: '',
                    price: '',
                    stock: ''
                },
                goodTitles:['输入商品名称在选择此项'],
                updateGood: {
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
                this.$confirm('此操作将删除商品 [ ' + row.kw + ' ] 是否继续?',
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
