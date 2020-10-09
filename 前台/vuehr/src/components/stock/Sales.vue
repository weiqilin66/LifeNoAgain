<template>
    <!--

        销量预警

    -->
    <div>
        <div style="display: flex;">
            <MyInput :cData="cData" @resp="getCData"/>
<!--            <el-button type="success" icon="el-icon-search" @click="initStock">刷新数据</el-button>-->
            <el-button type="warning" icon="el-icon-search" @click="refresh">刷新预警</el-button>
            <el-tag size ="normal" style="margin-left: 10px" type="info">预警首页总销量</el-tag>

        </div>
        <div>
            <el-table
                    :data="tableData"
                    height="1150"
                    stripe
                    style="width:100%;margin-top: 15px"
                    :row-class-name="tableRowClassName">
                <el-table-column
                        prop="name"
                        label="商品"
                        align="center"
                        width="300">
                </el-table-column>
                <el-table-column
                        prop="typeName"
                        label="走势"
                        align="center"
                        width="300">
                </el-table-column>

            </el-table>
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
        name: "HunterTable",
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
                tableData: [],
                tableDataBak: [],
                options:[]
            }
        },
        mounted() {
            this.initStock()
        },
        methods: {
            refresh(){
                this.getRequest("/warning/accept?msg="+'warning001').then(resp=>{
                    if (resp) {
                        this.initStock();
                    }
                })
            },
            tableRowClassName({row, rowIndex}) {
                if (row.type ==='1') {
                    return 'warning-row';
                } else if (row.type ==='2') {
                    return 'success-row';
                }else if (row.type ==='3'){
                    return '';
                }
                return '';
            },
            getCData(data){
                this.tableData=data
            },
            initStock() {
                this.getRequest("/warning/?msg="+'sales').then(resp=>{
                    if (resp) {
                        this.tableData = resp.data
                        this.cData.bak = resp.data
                    }
                })
            }
        },
    }
</script>
