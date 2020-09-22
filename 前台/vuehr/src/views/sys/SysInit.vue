<template>
    <div>

        <el-button @click="refresh">缓存刷新</el-button>
    </div>
</template>

<script>
    export default {
        name: "SysInit",
        data(){
            return{

            }
        },
        methods:{
            refresh(){
                this.getRequest("/noRight/cache/refresh").then(resp=>{
                    if (resp) {
                        this.$message.success("刷新成功");
                    }
                })
            }
        }
    }
</script>

<style scoped>

</style>