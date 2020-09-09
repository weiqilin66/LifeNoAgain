<template>
    <!--封装-->
    <div>
        <el-input placeholder="输入商品名称自动检索... " v-model="searchName" style="width: 400px;margin-right: 5px"/>
    </div>
</template>
<!-- 使用
<MyInput :cData="cData" @resp="getCData"/>

初始化查询备份一份全量
参数为全量bak ,过滤参数kw,如果过滤的是goodName字段则 kw:'goodName'
cData:{
    bak:[],
    kw:'kw'
}
this.$emit('resp', this.tableData2)  this.tableData2回调函数getCData的参数data值
-->
<script>
    export default {
        name: "MyInput",
        props: ['cData'],//和传入的：方法名一致
        data() {
            return {
                searchName: '',
                tableData:[]
            }
        },
        watch: {
            searchName(val) {
                let tmp = []
                if (val) {
                    this.cData.bak.forEach(item => {
                        if (item[this.cData.kw].includes(val)) {
                            tmp.push(item)
                        }
                    })
                    this.tableData = tmp
                    this.$emit('resp', this.tableData)//回调函数param1：@回调函数名(@resp) param2回调的数据
                } else {
                    this.$emit('resp', this.cData.bak)//无输入返回备份的全量
                }
            }
        },

    }
</script>

<style scoped>

</style>