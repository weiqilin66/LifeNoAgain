<template>
    <div>
        <!--标签页样式-->
        <el-tabs v-model="activeName" >
            <el-tab-pane label="定义" name="init"><good-manage></good-manage></el-tab-pane>
            <el-tab-pane label="爬虫" name="first"><Crawl></Crawl></el-tab-pane>
            <el-tab-pane label="ETL" name="forth"><KeyWord></KeyWord></el-tab-pane>
            <el-tab-pane label="趋势" name="fifth"><ChartTrend></ChartTrend></el-tab-pane>
            <el-tab-pane label="仓库" name="second"><Stock></Stock></el-tab-pane>

<!--            <el-tab-pane label="咸鱼" name="third"><FishFocus></FishFocus></el-tab-pane>-->

        </el-tabs>
    </div>
</template>

<script>
    /* .vue导入后在components 中注册为组件 */
    import ChartTrend from "../../components/sta/ChartTrend";
    import KeyWord from "../../components/stock/KeyWord";
    import Stock from "../../components/stock/Stock";
    import Crawl from "../../components/stock/Crawl.vue";
    import FishFocus from "../../components/stock/FishFocus.vue";
    import GoodManage from "../../components/stock/GoodManage";
    export default {
        name: "StockAndCrawl",
        data(){
            return{
                activeName:'fifth'
            }
        },
        components:{
            ChartTrend,
            GoodManage,
            Stock,
            Crawl,
            FishFocus,
            KeyWord
        }
    }
</script>

<style scoped>

</style>