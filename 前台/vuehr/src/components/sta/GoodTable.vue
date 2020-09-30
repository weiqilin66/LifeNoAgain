<template>
    <!--

        全量数据搜索

    -->
    <div>
        <div>
            <el-input placeholder="搜索的店铺..." v-model="shopName" style="width: 300px;margin-right: 5px" />
            <el-input placeholder="搜索的宝贝..." v-model="title" style="width: 300px;margin-right: 5px" />
            <el-input  v-model="etlDate" style="width: 300px;margin-right: 5px" disabled/>
            <el-button type="primary" icon="el-icon-search" @click="doSearch">搜索</el-button>
        </div>

        <div>
            <el-table
                    :data="tableData"
                    style="width: 90%"
            >
                <el-table-column
                        prop="kw"
                        label="关键词"
                        sortable
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="shop"
                        label="店铺名称"
                        sortable
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="title"
                        label="宝贝标题"
                        sortable
                        width="280">
                </el-table-column>
                <el-table-column
                        prop="price"
                        label="价格"
                        sortable
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="sales"
                        label="销量"
                        sortable
                        width="120">
                </el-table-column>
                <el-table-column
                        prop="freight"
                        label="运费"
                        sortable
                        width="80">
                </el-table-column>
                <el-table-column
                        prop="etlDate"
                        label="日期"
                        sortable
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="etlTime"
                        label="时间"
                        sortable
                        width="100">
                </el-table-column>
                <el-table-column
                        prop="comment1"
                        label="备注1"
                        sortable
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="comment2"
                        label="备注2"
                        sortable
                        width="180">
                </el-table-column>
                <el-table-column
                        prop="comment3"
                        label="备注3"
                        sortable
                        width="180">
                </el-table-column>
            </el-table>

        </div>
    </div>
</template>

<script>
    export default {
        name: "table1",
        data() {
            return {
                shopName: '',
                title: '',
                etlDate:'',
                tableData: [],
            }
        },
        mounted() {
            // this.initData()
        },
        methods: {
            doSearch() {
                const params = '?title='+this.title+'&shop='+this.shop
                this.getRequest("/statistics/table/search"+params).then(resp=>{
                    this.tableData = resp.data
                    this.etlDate = this.tableData[0].etlDate
                })
            },
        },
        components: {}

    }
</script>

<style scoped>

</style>