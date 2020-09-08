<template>
    <div>
        <!--标签页样式-->
        <el-tabs v-model="activeName" type="card" >
            <el-tab-pane label="价格走势" name="ChartTrend"><ChartTrend></ChartTrend></el-tab-pane>
            <el-tab-pane label="差价分析" name="ChartDif"><ChartDifferent></ChartDifferent></el-tab-pane>

        </el-tabs>
    </div>
</template>

<script>
    import ChartDifferent from "../../components/sta/ChartDifferent";
    import ChartTrend from "../../components/sta/ChartTrend";
    export default {
        name: "staChart",
        data(){
            return{
                activeName: 'ChartTrend'
            }
        },
        components:{
            ChartDifferent,
            ChartTrend
        }
    }
</script>

<style scoped>

</style>