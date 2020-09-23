<template>
    <!--

        加工关键词

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
                    style="width:100%;margin-top: 15px"
                    border
                    stripe
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
                        prop="base"
                        label="基础词"
                        align="center"
                        width="150">
                </el-table-column>
                <el-table-column
                        prop="include1"
                        label="包含词1"
                        align="center"
                        width="150">
                </el-table-column>
                <el-table-column
                        prop="include2"
                        label="包含词2"
                        align="center"
                        width="150">
                </el-table-column>
                <el-table-column
                        prop="include3"
                        label="包含词3"
                        align="center"
                        width="150">
                </el-table-column>
                <el-table-column
                        prop="enclude1"
                        label="不包含词1"
                        align="center"
                        width="150">
                </el-table-column>
                <el-table-column
                        prop="enclude2"
                        label="不包含词2"
                        align="center"
                        width="150">
                </el-table-column>
                <el-table-column
                        prop="enclude3"
                        label="不包含词3"
                        align="center"
                        width="150">
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
                                <el-tag size="normal">名称</el-tag>
                            </td>
                            <td width="600">
                                <span>{{updateGood.label+updateGood.name}}</span>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <el-tag size="normal">基础词</el-tag>
                            </td>
                            <td width="400">
                                <el-input v-model="updateGood.base"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <el-tag size="normal">包含词1</el-tag>
                            </td>
                            <td width="400">
                                <el-input v-model="updateGood.include1"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <el-tag size="normal">包含词2</el-tag>
                            </td>
                            <td width="400">
                                <el-input v-model="updateGood.include2"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <el-tag size="normal">包含词3</el-tag>
                            </td>
                            <td width="400">
                                <el-input v-model="updateGood.include3"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <el-tag size="normal">不包含词1</el-tag>
                            </td>
                            <td width="400">
                                <el-input v-model="updateGood.enclude1"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <el-tag size="normal">不包含词2</el-tag>
                            </td>
                            <td width="400">
                                <el-input v-model="updateGood.enclude2"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <el-tag size="normal">不包含词3</el-tag>
                            </td>
                            <td width="400">
                                <el-input v-model="updateGood.enclude3"/>
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
                                <el-tag size="normal">名称</el-tag>
                            </td>
                            <td >
                                <MyGoodSelect @resp="getGood"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <el-tag size="normal">基础词</el-tag>
                            </td>
                            <td width="400">
                                <el-input v-model="addGood.base"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <el-tag size="normal">包含词1</el-tag>
                            </td>
                            <td width="400">
                                <el-input v-model="addGood.include1"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <el-tag size="normal">包含词2</el-tag>
                            </td>
                            <td width="400">
                                <el-input v-model="addGood.include2"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <el-tag size="normal">包含词3</el-tag>
                            </td>
                            <td width="400">
                                <el-input v-model="addGood.include3"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <el-tag size="normal">不包含词1</el-tag>
                            </td>
                            <td width="400">
                                <el-input v-model="addGood.enclude1"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <el-tag size="normal">不包含词2</el-tag>
                            </td>
                            <td width="400">
                                <el-input v-model="addGood.enclude2"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <el-tag size="normal">不包含词3</el-tag>
                            </td>
                            <td width="400">
                                <el-input v-model="addGood.enclude3"/>
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
        name: "KeyWord",
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
                    base:'',
                    include1:'',
                    include2:'',
                    include3:'',
                    enclude1:'',
                    enclude2:'',
                    enclude3:''
                },
                updateGood: {
                    label:'',
                    name:'',
                    base:'',
                    include1:'',
                    include2:'',
                    include3:'',
                    enclude1:'',
                    enclude2:'',
                    enclude3:''
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
            getGood(data){
                this.addGood.gid = data
            },
            getCData(data){
                this.tableData=data
            },
            beforeAdd(){
                this.postRequest("/noRight/keyWord/check", this.addGood).then(resp => {
                    if (!resp) {
                        this.$confirm('是否继续添加该商品','提示'
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
                this.postRequest("/noRight/keyWord/", this.addGood).then(resp => {
                    if (resp) {
                        this.initStock()
                        this.addDialogVisible = false
                        this.$message.success("添加成功!")
                    }
                })
            },
            handleUpate() {
                this.putRequest('/noRight/keyWord/', this.updateGood).then(resp => {
                    if (resp) {
                        this.initStock()
                        this.updateDialogVisible = false
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
                    this.deleteRequest("/noRight/keyWord/" + row.id).then(resp => {
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
                const gid = this.addGood.gid
                this.addGood = {}
                this.addGood.gid = gid
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
                this.getRequest('/noRight/keyWord/').then(resp => {
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
