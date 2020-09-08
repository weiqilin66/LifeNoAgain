<template>
    <div>
        <!--标签页样式-->
        <el-tabs v-model="activeName" >
            <el-tab-pane label="部门管理" name="first"><DepMana></DepMana></el-tab-pane>
            <el-tab-pane label="职位管理" name="second"><PosMana></PosMana></el-tab-pane>
            <el-tab-pane label="职称管理" name="third"><JobLevelMana></JobLevelMana></el-tab-pane>
            <el-tab-pane label="奖惩规则" name="fourth"><BcMana></BcMana></el-tab-pane>
            <el-tab-pane label="权限组" name="fifth"><PermissMana></PermissMana></el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
    /* .vue导入后在components 中注册为组件 */
    import BcMana from "../../components/sys/basic/BcMana";
    import DepMana from "../../components/sys/basic/DepMana";
    import JobLevelMana from "../../components/sys/basic/JobLevelMana";
    import PermissMana from "../../components/sys/basic/PermissMana";
    import PosMana from "../../components/sys/basic/PosMana";
    export default {
        name: "SysBasic",
        data(){
            return{
                activeName:'first'
            }
        },
        components:{
            BcMana,
            DepMana,
            JobLevelMana,
            PermissMana,
            PosMana
        }
    }
</script>

<style scoped>

</style>