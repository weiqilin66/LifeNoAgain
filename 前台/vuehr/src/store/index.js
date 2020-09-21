import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        routes: [],
        btnList:[],
        bakBtnList:[],
        goodMainList:[]
    },
    mutations: {// 操作state数据
        initRoutes(state, data) {
            state.routes = data;
        },
        initBtnList(state,data){
            state.btnList = data
        },
        backBtnList(state,data){
            state.bakBtnList = data
        },
        initGoodMainList(state,data){
            state.goodMainList = data
        },
    },
    // 做异步 提交mutations
    actions: {}
})