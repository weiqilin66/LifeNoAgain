<template>
    <div>
        <!--标签页样式-->
        <el-tabs v-model="activeName" >
            <el-tab-pane label="数据查询" name="table1"><table1></table1></el-tab-pane>
            <el-tab-pane label="设计中..." name="table2"><table2></table2></el-tab-pane>

        </el-tabs>
    </div>
</template>

<script>
    import table1 from '../../components/sta/Table1'
    import table2 from '../../components/sta/Table2'
    export default {
        name: "StaAll",
        data(){
            return{
                activeName: 'table1'
            }
        },
        components:{
            table1,
            table2
        }
    }
</script>

<style scoped>

</style>