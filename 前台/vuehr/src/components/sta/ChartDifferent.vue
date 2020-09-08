<template>
    <div>
        <div>
            <el-input placeholder="输入店铺名称 ... " v-model="shop" style="width: 400px;margin-right: 5px"
                      @keydown.enter.native="doSearch(shop)"/>
<!--
            <el-button type="primary" icon="el-icon-search" @click="doSearch(shop)">搜索</el-button>
-->
            <el-button type="success" icon="el-icon-search" @click="initShop">整理数据</el-button>
            <el-button type="warning" icon="el-icon-search" @click="reset">重置</el-button>
            <el-button type="danger" icon="el-icon-search" @click="fuckHunter" >猎杀猎人</el-button>
            <el-button type="primary" icon="el-icon-search" @click="refresh" v-loading="loading" >刷新</el-button>
        </div>
        <!--按店铺整理-->
        <div style="margin-top: 10px ; width:80%" v-show="showCollapse">
            <el-collapse v-model="activeName" accordion><!--name传入change事件-->
                <el-collapse-item :title="shopName+'    '+shopMap[shopName]+'组'" :name="shopName"
                                  v-for="(shopName,index) in shopList" :key="index" @click.native="change(shopName)">
                    <!--加事件解决只有首次点击触发事件-->
                    <el-row style="margin-top: 5px;" v-if="dataList2.length!==0">
                        <el-col :span="11" v-for="(item, index) in dataList2" :key="index" :offset="1">
                            <el-card :body-style="{ padding: '7px' }">
                                <div style="margin-bottom: 5px;display: flex;justify-content: space-between">
                                    <span style="font-size: 20px;font-weight: bold">{{item.goodName}}</span>
                                    <span :style="diffStyle(item.diff)">{{item.diff}}</span>
                                </div>
                                <div style="display: flex;justify-content: space-between">
                                    <!--max-->
                                    <div style="margin-left: 20px">
                                        <el-image
                                                style="width: 150px; height: 150px"
                                                :src="item.max?item.max.imgUrl:''"
                                                fit="fill">
                                            <div slot="error" class="image-slot"
                                                 style="display: flex;justify-content: center ;align-items: center;">
                                                <i class="el-icon-picture-outline"></i>
                                            </div>
                                        </el-image>
                                        <div>
                                            <div style="width: 160px">
                                                <a :href="item.max?item.max.detailUrl:''" target="_blank"
                                                   :title="item.max?item.max.sales:''"
                                                   style="font-size: 10px">{{item.max?item.max.title:''}}</a>
                                            </div>

                                            <div>
                                                <span style="font-size: 10px">店铺:{{item.max?item.max.shop:''}}</span>
                                            </div>
                                            <div>
                                                <span style="font-family: 黑体">价格: {{item.max?item.max.price:''}}元</span>
                                            </div>
                                        </div>
                                    </div>
                                    <!--min-->
                                    <div style="margin-right: 20px">
                                        <el-image
                                                style="width: 150px; height: 150px"
                                                :src="item.min?item.min.imgUrl:''"
                                                fit="fill">
                                            <div slot="error" class="image-slot"
                                                 style="display: flex;justify-content: center ;align-items: center;">
                                                <i class="el-icon-picture-outline"></i>
                                            </div>
                                        </el-image>
                                        <div>
                                            <div style="width: 160px">
                                                <a :href="item.min?item.min.detailUrl:''" target="_blank"
                                                   :title="item.min?item.min.sales:''"
                                                   style="font-size: 10px">
                                                    {{item.min?item.min.title:''}}</a>
                                            </div>

                                            <div>
                                                <span style="font-size: 10px">店铺:{{item.min?item.min.shop:''}}</span>
                                            </div>
                                            <div>
                                                <span style="font-family: 黑体">价格: {{item.min?item.min.price:''}}元</span>
                                            </div>
                                        </div>
                                    </div>
                                    <!--other max min-->
                                    <div>
                                        <div style="width: 100px;">
                                            <div v-if="item.otherMax != null">
                                                <!--bug!!! v-for不加冒号 否则检测x 为data中的数值未定义-->
                                                <div v-for="(x, index) in item.otherMax">
                                                    <div>
                                                        <span style="font-size: 10px">店铺:{{x.shop}}</span>
                                                    </div>
                                                    <div>
                                            <span style="font-family: 黑体"><a :href="x.detailUrl" target="_blank"
                                                                             :title="x.sales">价格: {{x.price}}元</a></span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div style="width: 100px;">
                                            <div v-if="item.otherMin != null">
                                                <!--bug!!! v-for不加冒号 否则检测x 为data中的数值未定义-->
                                                <div v-for="(x, index) in item.otherMin">
                                                    <div>
                                                        <span style="font-size: 10px">店铺:{{x.shop}}</span>
                                                    </div>
                                                    <div>
                                            <span style="font-family: 黑体"><a :href="x.detailUrl" target="_blank"
                                                                             :title="x.sales">价格: {{x.price}}元</a></span>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                    </div>
                                </div>
                            </el-card>
                        </el-col>
                    </el-row>

                </el-collapse-item>
            </el-collapse>
        </div>
        <!--未整理数据-->
        <div style="margin-top: 8px" v-show="showCard"><!--v-show一开始就会渲染 v-if为true时才会渲染-->
            <el-row style="margin-top: 5px;" v-if="dataList.length!==0"><!--v-if添加在v-for父组件-->
                <el-col :span="11" v-for="(item, index) in dataList" :key="index" :offset="1">
                    <el-card :body-style="{ padding: '7px' }">
                        <div style="margin-bottom: 5px;display: flex;justify-content: space-between">
                            <span>{{item.goodName}}</span><span>{{item.diff}}</span>
                        </div>
                        <div style="display: flex;justify-content: space-between">
                            <!--max-->
                            <div style="margin-left: 20px">
                                <el-image
                                        style="width: 150px; height: 150px"
                                        :src="item.max?item.max.imgUrl:''"
                                        fit="fill">
                                    <div slot="error" class="image-slot"
                                         style="display: flex;justify-content: center ;align-items: center;">
                                        <i class="el-icon-picture-outline"></i>
                                    </div>
                                </el-image>
                                <div>
                                    <div style="width: 160px">
                                        <a :href="item.max?item.max.detailUrl:''" target="_blank" :title="item.max.sales"
                                           style="font-size: 10px">{{item.max?item.max.title:''}}</a>
                                    </div>

                                    <div>
                                        <span style="font-size: 10px">店铺:{{item.max?item.max.shop:''}}</span>
                                    </div>
                                    <div>
                                        <span style="font-family: 黑体">价格: {{item.max?item.max.price:''}}元</span>
                                    </div>
                                </div>
                            </div>
                            <!--min-->
                            <div style="margin-right: 20px">
                                <el-image
                                        style="width: 150px; height: 150px"
                                        :src="item.min?item.min.imgUrl:''"
                                        fit="fill">
                                    <div slot="error" class="image-slot"
                                         style="display: flex;justify-content: center ;align-items: center;">
                                        <i class="el-icon-picture-outline"></i>
                                    </div>
                                </el-image>
                                <div>
                                    <div style="width: 160px">
                                        <a :href="item.min?item.min.detailUrl:''" target="_blank" :title="item.min.sales"
                                           style="font-size: 10px">
                                            {{item.min?item.min.title:''}}</a>
                                    </div>

                                    <div>
                                        <span style="font-size: 10px">店铺:{{item.min?item.min.shop:''}}</span>
                                    </div>
                                    <div>
                                        <span style="font-family: 黑体">价格: {{item.min?item.min.price:''}}元</span>
                                    </div>
                                </div>
                            </div>
                            <!--other max min-->
                            <div>
                                <div style="width: 100px;">
                                    <div v-if="item.otherMax != null">
                                        <!--bug!!! v-for不加冒号 否则检测x 为data中的数值未定义-->
                                        <div v-for="(x, index) in item.otherMax">
                                            <div>
                                                <span style="font-size: 10px">店铺:{{x.shop}}</span>
                                            </div>
                                            <div>
                                            <span style="font-family: 黑体"><a :href="x.detailUrl" target="_blank"
                                                                             :title="x.sales">价格: {{x.price}}元</a></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div style="width: 100px;">
                                    <div v-if="item.otherMin != null">
                                        <!--bug!!! v-for不加冒号 否则检测x 为data中的数值未定义-->
                                        <div v-for="(x, index) in item.otherMin">
                                            <div>
                                                <span style="font-size: 10px">店铺:{{x.shop}}</span>
                                            </div>
                                            <div>
                                            <span style="font-family: 黑体"><a :href="x.detailUrl" target="_blank"
                                                                             :title="x.sales">价格: {{x.price}}元</a></span>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </el-card>
                </el-col>
            </el-row>
        </div>

    </div>
</template>

<script>
    export default {
        name: "ChartDifferent",
        data() {
            return {
                dataList: [],
                dataList2: [],
                activeName: -1,
                otherMaxList: [],
                otherMinList: [],
                loading: false,
                btnList: [],
                shop: '',
                shopList: [],
                shopMap: [],
                showCard: false,
                showCollapse: false
            }
        },
        mounted() {
            this.initData()
        },
        watch: {},
        methods: {
            refresh(){
                this.getRequest("/statistics/chart/remove").then(resp=>{
                    this.loading=true
                    if (resp) {
                        this.$message.success("清除缓存成功,ETL-START")
                        this.getRequest('/statistics/chart/maxAndMin').then(resp => {
                            this.loading = true
                            if (resp) {
                                this.dataList = resp.data
                                this.loading = false
                                this.showCard = true
                                this.$message.success("ETL-END")

                            }
                        })
                    }
                })
            },
            fuckHunter(){
                this.getRequest('/statistics/chart/maxAndMin?shop='+'hunter').then(resp => {
                    this.loading = true
                    if (resp) {
                        this.dataList = resp.data
                        this.loading = false
                        this.showCard = true
                        this.showCollapse = false
                    }
                })
            },
            diffStyle(diff){
                if (diff >= 25) {
                    return 'font-size: 20px;font-weight: bold;color: red;'
                }
            },
            doSearch(shopName) {
                this.getRequest('/statistics/chart/byShop?shopName=' + shopName).then(resp => {
                    if (resp) {
                        this.shopList = resp.data.list
                    }
                })
            },
            change(shopName) {
                // change事件只出发开始的点击事件,不触发关闭
                //通过修饰符native对标签进行处理，使其作为普通的HTML结构就可以绑定上事件 @click.native  = method
                if (shopName) {
                    this.getRequest('/statistics/chart/byShop?shopName=' + shopName).then(resp => {
                        if (resp) {
                            this.dataList2 = resp.data
                        }
                    })
                }
            },
            reset() {
                this.showCard = true
                this.showCollapse = false
            },
            //统计店铺及符合规则宝贝数
            initShop() {
                this.getRequest('/statistics/chart/thief').then(resp => {
                    if (resp) {
                        this.shopList = resp.data.list
                        this.shopMap = resp.data.map
                        this.showCard = false
                        this.showCollapse = true
                    }
                })
            },
            getMaxMin() {
                this.getRequest('/statistics/chart/maxAndMin').then(resp => {
                    this.loading = true
                    if (resp) {
                        this.dataList = resp.data
                        this.loading = false
                        this.showCard = true
                    }
                })
            },
            getKw() {
                this.getRequest("/statistics/chart/goodList").then(resp => {
                    if (resp) {
                        this.btnList = resp.data
                        //趋势图使用 存入store
                        this.$store.commit("initBtnList", this.btnList)
                        this.$store.commit("backBtnList",this.btnList)
                    }
                })
            },
            initData() {
                this.getMaxMin()
                this.getKw()
            }
        }
    }
</script>

<style scoped>

</style>