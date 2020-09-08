<template>
    <div>
        <div style="display: flex;justify-content: center;margin-bottom: 5px;margin-right: 170px">

            <div style="margin-right: 5px">
                <el-radio-group v-model="days">
                    <el-radio :label="7">近7天</el-radio>
                    <el-radio :label="15">半月</el-radio>
                    <el-radio :label="30">整月</el-radio>
                </el-radio-group>
            </div>
            <div>
                <el-input placeholder="关键字...机型 游戏主名 游戏附名" v-model="kw" style="width: 400px;margin-right: 5px"
                          />
<!--                @keydown.enter.native="doSearch(kw)"-->
                <el-button type="primary" icon="el-icon-search" @click="reSet" >重置</el-button>
            </div>
        </div>
        <div style="margin-bottom: 100px;display: flex;justify-content: center;flex-wrap: wrap;width: 90%">

            <el-button v-for="(item,index) in btnList" :type="item.type" :key="index"
                       style="margin-top: 3px" @click="changeChart2(item.name)" plain>
                <a id="toChart">{{item.name}}</a>
            </el-button>
        </div>
        <el-table
                v-show="objList.length!==0"
                :data="objList"
                stripe
                style="width: 100%">
            <el-table-column
                    prop="shop"
                    label="店铺"
                    width="180">
            </el-table-column>
            <el-table-column
                    prop="price"
                    label="价格"
                    width="180">
            </el-table-column>
            <el-table-column
                    prop="sales"
                    label="销量"
                    width="180">
            </el-table-column>

            <el-table-column
                    label="地址">
                <template slot-scope="scope">
                    <a :href="scope.row.detailUrl" target="_blank">{{scope.row.detailUrl}}</a>
                </template>
            </el-table-column>
        </el-table>
        <div id="echarts" ref="echarts"></div>
        <div><a href="#toChart">-</a></div>
    </div>

</template>
<style scoped>
    #echarts {
        height: 900px;
    }
</style>
<script>
    export default {
        name: "ChartTrend",
        data() {
            return {
                kw: '',
                days: 7,
                // btnList:[], computed的值不能在此处定义
                objList:[],
                etlDate: [],
                chartData: {
                    // 配置标题
                    title: {
                        text: '欢迎帅奇'
                    },
                    // 提示
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'cross',
                            label: {
                                backgroundColor: '#6a7985'
                            }
                        }
                    },
                    // 图例
                    legend: {
                        data: ['宁波老猎人电玩'],
                        width:'70%',
                        selector: [
                            {
                                type: 'all or inverse',
                                // 可以是任意你喜欢的 title
                                title: '全选'
                            },
                            {
                                type: 'inverse',
                                title: '反选'
                            }
                        ]
                    },
                    // 工具(下载...)
                    toolbox: {
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    // 位置
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis: [//横坐标
                        {
                            type: 'category',
                            boundaryGap: false,
                            data: ['周一','周二','周三']
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value',
                        }
                    ],
                    series: [
                        {
                            name: '宁波老猎人电玩',//toolTip提示使用
                            type: 'line',// 类型 折线图
                            areaStyle: {}, //填充颜色
                            label: {    // 显示数据
                                normal: {
                                    show: false,
                                    position: 'top'
                                }
                            },
                            data: [0,220,0]
                        }
                    ]
                }
            }
        },
        watch:{
    /*
        watch 监听对象属性写法
            'listQuery.orgName'():{

            }
        自定义组件 同步 自定义方法名.sync eg:sonProp.sync  子组件的sonProp发生变化 父组件动态响应
        $emit(event,[arg])触发event事件,后面数组参数
        eg:
            <input-org
            :sonProp = "status"      父组件传递属性名sonProp 属性值为status  子组件中 data()外定义 props:['sonProp']获取到变量status
            @eventName = "getData"    子组件通过$emit(eventName,参数) 来调用父组件中的getData方法 ,通过传参给getData实现给父组件传参
            >
    */
            // 监视关键字筛选store中的btnList
            kw(val){
                let temList = this.$store.state.bakBtnList
                if (val) {//为空重置btnList
                    let tem2 = this.$store.state.btnList
                    let tem3 = []
                    tem2.forEach(item=>{
                        if (item.name.includes(val)) {
                            tem3.push(item)
                        }
                    })
                    this.$store.commit("initBtnList",tem3)
                }else {
                    this.$store.commit("initBtnList",temList)

                }



            }
        },
        computed:{
          btnList(){
              return this.$store.state.btnList
          }
        },
        mounted() {
            this.drawLine() //必须第一个显示此组件 否则初始化失败
        },
        methods: {
            reSet(){
              this.kw=''
            },
            drawLine() {
                // 基于准备好的dom，初始化echarts实例
                // let myChart = this.echarts.init(document.getElementById('echarts'));
                let myChart = this.echarts.init(this.$refs['echarts']);
                let option = this.chartData
                myChart.setOption(option);
            },
            doSearch(kw) {
                this.changeChart2(kw)
            },
            changeChart2(kw) {
                window.scrollTo(0, document.documentElement.clientHeight);//滚动到底部定位视图
                this.getXDate()
                // 横坐标日期
                this.chartData.xAxis[0].data = this.etlDate
                this.chartData.title['text'] = kw
                this.getRequest('/statistics/chart/byTitle?title=' + kw + '&date=' + this.days).then(resp => {
                    if (resp) {
                        if (resp.error != null) {//错误检索规则报错
                            this.$message.error(resp.error);
                            this.kw = ''
                            return
                        }
                        // 图例
                        const shops = resp.shops
                        this.chartData.legend.data = shops
                        this.chartData.series=[]
                        shops.forEach(shop=>{
                            let sery = {
                                name: shop,
                                type: 'line',
                                areaStyle: {},
                                label: {
                                    normal: {
                                        show: true,
                                        position: 'top'
                                    }
                                },
                                data: []
                            }
                            // 变量作为属性名使用 由 . 改成 []
                            sery.data=resp[shop]
                            this.chartData.series.push(sery)
                            // console.log(sery);
                        })

                        this.drawLine()//重新绘制图表加载数据
                        this.objList=resp.objList
                    }
                })
            },
            // 获取 x轴的日期数组
            getXDate(num) {
                this.etlDate = []
                if (this.days === 0) {
                    num = 15
                } else {
                    num = this.days
                }
                for (let i = 0; i < num; i++) {
                    const start = new Date();
                    start.setDate(start.getDate() - i);
                    this.etlDate.push(this.getyyyyMMdd(start))
                }
            }
            /* // 同步实例
                        async changeChart(kw) {
                            this.etlDate = []
                            if (this.selDate.length === 0) {//默认近15天数据

                                for (let i=0;i<15;i++){
                                    const start = new Date();
                                    start.setDate(start.getDate() -i);
                                    this.etlDate.push(this.getyyyyMMdd(start))
                                }
                                console.log('etl日期区间:'+this.etlDate);
                            }else{//选择日期
                                const start = this.selDate[0]
                                const end =this.selDate[1]
                                const res = end -start
                                console.log('日期区间: '+res);
                                const nextDay = start.setDate(start.getDate() -1);

                                if(start === end){
                                    this.etlDate=this.selDate[0]
                                }else {
                                    this.etlDate.push(start)
                                    if (nextDay!==end){//下一天不是结束日
                                        this.etlDate.push(nextDay)
                                    }
                                    this.etlDate.push(end)
                                }
                            }


                            // 横坐标日期
                            this.chartData.xAxis[0].data = this.etlDate
                            let x = 0
                            // 初始化
                            const resp = await Axios.get('/statistics/chart/init?title=' + kw + '&date=' +  this.etlDate[0])
                            this.initChartAsync(kw, resp)
                            // 遍历数据
                            for (const date of this.etlDate) {
                                if(x===0){
                                    x++
                                    continue
                                }
                                console.log('数据日期:'+date);
                                const resp2 = await Axios.get('/statistics/chart/init?title=' + kw + '&date=' + date)
                                console.log('数据:'+resp2);
                                this.getNextDataAsync(resp2)
                            }

                            this.drawLine()
                            // 异步请求 等待3秒再绘图
                            // setTimeout(this.drawLine(),3000)

                        },
            */

        },

    }
</script>

