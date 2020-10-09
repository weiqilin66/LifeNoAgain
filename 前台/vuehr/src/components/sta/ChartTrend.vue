<template>
    <!--

        价格走势图

    -->
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
                <el-input placeholder="关键字...机型 游戏主名 游戏附名" v-model="kw" style="width: 400px;margin-right: 5px"/>
<!--                @keydown.enter.native="doSearch(kw)"-->
                <el-button type="primary" icon="el-icon-search" @click="reSet" >重置</el-button>
            </div>
        </div>
        <!--宝贝检索标签-->
        <div style="margin-bottom: 100px;display: flex;justify-content: center;flex-wrap: wrap;width: 90%">
            <el-button v-for="(item,index) in btnList" :type="item.type" :key="index"
                       style="margin-top: 3px" @click="changeChart2(item)" plain>
                <a id="toChart">{{item.name}}</a>
            </el-button>
        </div>
        <!--库存表-->
        <el-table
                v-show="stockTable.length >0"
                :data="stockTable"
                stripe
                style="width: 100%;margin-bottom: 50px">
            <el-table-column
                    prop="label"
                    label="商品"
                    width="180">
            </el-table-column>
            <el-table-column
                    prop="name"
                    label="商品"
                    width="180">
            </el-table-column>
            <el-table-column
                    prop="price"
                    label="价格"
                    width="180">
                <template slot-scope="scope">
                    <el-input v-model="scope.row.price" @blur="doUpdate(scope.row)"/>
                </template>
            </el-table-column>

            <el-table-column
                    prop="stock"
                    label="库存"
                    width="180">
            </el-table-column>
            <el-table-column
                    prop="comment"
                    label="建议"
                    width="180">
            </el-table-column>
            <!--            <el-table-column-->
            <!--                    label="地址">-->
            <!--                <template slot-scope="scope">-->
            <!--                    <a :href="scope.row.detailUrl" target="_blank">{{scope.row.detailUrl}}</a>-->
            <!--                </template>-->
            <!--            </el-table-column>-->
        </el-table>
        <el-table
                v-show="objList.length>0"
                :data="objList"
                stripe
                style="width: 100%;margin-bottom: 50px">
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
        <div style="display: flex;justify-content: space-between">
            <div style="height: 1200px;width:800px">
                <div id="echarts" ref="echarts"></div>
            </div>
            <div style="height: 1200px;width:800px">
                <div id="echarts2" ref="echarts2"></div>
            </div>
        </div>

            <div><a href="#toChart">-</a></div>
    </div>
</template>
<style scoped>
    #echarts {
        width: 100%;
        height: 70%;

    }
    #echarts2 {
        width: 100%;
        height: 70%;
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
                startDate:'',
                endDate:'',
                startDate2:'',
                stockTable:[],
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
                        data: ['皮卡皮电玩'],
                        width:'90%',
                        //height:'150%',
                        selector: [
                            {
                                type: 'all or inverse',
                                // 可以是任意你喜欢的 title
                                title: '全选'
                            },
                            // {
                            //     type: 'inverse',
                            //     title: '反选'
                            // }
                        ]
                    },
                    // 工具(下载...)
                    toolbox: {
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    // 上下左右位置 top 图例和图的距离
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        top:100,
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
                            name: '皮卡皮电玩',//toolTip提示使用
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
                },
                chartData2: {
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
                    // legend: {
                    //     data: ['皮卡皮电玩'],
                    //     width:'90%',
                    //     //height:'150%',
                    //     selector: [
                    //         {
                    //             type: 'all or inverse',
                    //             // 可以是任意你喜欢的 title
                    //             title: '全选'
                    //         },
                    //         // {
                    //         //     type: 'inverse',
                    //         //     title: '反选'
                    //         // }
                    //     ]
                    // },
                    // 工具(下载...)
                    toolbox: {
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    // 上下左右位置 top 图例和图的距离
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        top:100,
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
                            name: '皮卡皮电玩',//toolTip提示使用
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
            this.drawLine2() //必须第一个显示此组件 否则初始化失败
        },
        methods: {
            doUpdate(row){
                this.putRequest('/stock/stock1/', row).then(resp => {
                    if (resp) {
                        this.$message.success("修改成功!")
                    }
                })
            },
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
            drawLine2() {
                let myChart = this.echarts.init(this.$refs['echarts2']);
                let option = this.chartData2
                myChart.setOption(option);
            },
            getSalesEcharts(item){
                this.chartData2.title['text'] = item.name
                const params = '?gid='+ item.gid + '&startDate=' + this.startDate2+ '&endDate=' + this.endDate
                this.getRequest('/statistics/chart/bySales'+params).then(resp=>{
                    if (resp) {
                        this.chartData2.xAxis[0].data = resp.data.xAxis
                        this.chartData2.series[0].data = resp.data.sales
                        //重新绘制图表加载数据
                        this.drawLine2()
                    }
                })
            },
            getPriceEcharts(item){
                // 横坐标日期
                this.chartData.xAxis[0].data = this.etlDate
                this.chartData.title['text'] = item.name
                this.getRequest('/statistics/chart/byTitle?id=' + item.id
                    + '&startDate=' + this.startDate+ '&endDate=' + this.endDate).then(resp => {
                    if (resp) {
                        if (resp['error']!=null) {
                            resp.error.forEach(error=>{
                                this.$message.error(error)
                            })
                        }
                        //旺旺名
                        let shopNameTitle=[]
                        this.stockTable=[]
                        resp.shops.forEach(s =>{
                            shopNameTitle.push(s.comment)
                        })
                        //便利key店铺名
                        const shops = shopNameTitle
                        // 图例
                        this.chartData.legend.data = shopNameTitle
                        this.chartData.legend.data.unshift('','')

                        //线实例
                        this.chartData.series=[]
                        shops.forEach(shop =>{
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
                        //重新绘制图表加载数据
                        this.drawLine()
                        if (resp['stockTable']) {
                            this.stockTable.push(resp['stockTable'])
                        }
                        this.objList=resp.objList

                    }
                })
            },
            changeChart2(item) {
                this.getXDate()
                window.scrollTo(0, document.documentElement.clientHeight);//滚动到底部定位视图
                this.getPriceEcharts(item);
                this.getSalesEcharts(item);
            },
            // 获取 x轴的日期数组
            getXDate: function (num) {
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
                const day = 1000*3600*24
                this.endDate = this.getyyyyMMdd(new Date())
                let startDate = new Date().getTime();
                const startDate1 = startDate- day * num
                const startDate2 = startDate- day * 90 //销量趋势显示近3个月
                this.startDate = this.getyyyyMMdd(new Date(startDate1))
                this.startDate2 = this.getyyyyMMdd(new Date(startDate2))
                //console.log('折线图开始时间： ' +this.startDate)
                //console.log('折线图结束时间： ' +this.endDate)

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

