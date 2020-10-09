<template>
    <div>
        <!--标签页样式-->
        <el-tabs v-model="activeName" >
            <el-tab-pane label="Hunter" name="init"><HunterTable></HunterTable></el-tab-pane>
            <el-tab-pane label="底价策略" name="first"><OthersLower></OthersLower></el-tab-pane>
            <el-tab-pane label="仓库" name="2"><Stock></Stock></el-tab-pane>
            <el-tab-pane label="首页销量" name="3"><Sales></Sales></el-tab-pane>


        </el-tabs>
    </div>
</template>

<script>
    /* .vue导入后在components 中注册为组件 */
   import HunterTable from "../../components/stock/HunterTable";
   import OthersLower from "../../components/stock/OthersLower";
   import Stock from "../../components/stock/Stock";
   import Sales from "../../components/stock/Sales";
    export default {
        name: "StockWarning",
        data(){
            return{
                activeName:'init'
            }
        },
        components:{
            Sales,
            HunterTable,
            OthersLower,
            Stock,

        }
    }
</script>

<style scoped>

</style>