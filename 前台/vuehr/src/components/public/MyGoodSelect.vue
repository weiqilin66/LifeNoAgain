<template>
    <!--封装-->
    <div>
        <el-select v-model="gid" placeholder="请选择标题" style="margin-left: 10px" >
            <el-option
                    v-for="(item,index) in options"
                    :key="index+'*'"
                    :label="item.label+'-'+item.name"
                    :value="item.id"><!--绑定整个item下拉框显示的值会无响应-->
            </el-option>
        </el-select>
    </div>
</template>
<script>
    export default {
        name: "MyGoodSelect",
        data() {
            return {
                gid:'',
                options:[],

            }
        },
        mounted() {
            this.options = this.$store.state.goodMainList
        },
        watch: {
            gid(val) {

                this.$emit('resp',val)
            }
        },

    }
</script>

<style scoped>

</style>