<template>
    <div>
        <el-input placeholder="输入商品名称自动检索... " v-model="goodName" style="width: 400px;margin-right: 5px"/>
    </div>
</template>
<!-- 使用
<MyInput :cData="cData" @resp="getCData"/>

初始化查询备份一份全量 参数为全量bak , 过滤的行数据的关键字kw,如果过滤的是goodName字段则 kw:'goodName'
cData:{
    bak:[],
    kw:'kw'
}
this.$emit('resp', this.tableData2)  this.tableData2回调函数getCData的参数data值
-->
<script>
    export default {
        name: "Input-wq",
        props: ['cData'],
        data() {
            return {
                goodName: '',
                tableData:[]
            }
        },
        watch: {
            goodName(val) {
                let tmp = []
                if (val) {
                    this.cData.bak.forEach(item => {
                        if (item[this.cData.kw].includes(val)) {
                            tmp.push(item)
                        }
                    })
                    this.tableData = tmp
                    this.$emit('resp', this.tableData)
                } else {
                    this.$emit('resp', this.cData.bak)
                }
            }
        },
        computed: {
            // tableData2() {
            //     return this.tableData
            // }
        }
    }
</script>

<style scoped>

</style>