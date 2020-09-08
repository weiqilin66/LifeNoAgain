<template>
    <div>
        <!-------------------------------------  tools  ----------------------------------------------->
        <div style="display: flex;justify-content: space-between">
            <div>
                <el-input v-model="keyWord" style="width: 500px;margin-right: 5px" placeholder="请输入员工名字进行搜索..."
                          @keydown.enter.native="initData" clearable @clear="initData"/>
                <el-button type="primary" icon="el-icon-search" @click="doSearch">搜索
                </el-button>
                <el-button type="primary" @click="doDetailSearch">
                    <i class="fa fa-angle-double-down" aria-hidden="true"/>高级搜索
                </el-button>
            </div>
            <div>
                <!--inline-flex 块级元素变成行内元素 避免换行-->
                <el-upload
                        style="display: inline-flex ; margin-right: 8px"
                        action="/employee/basic/upload"
                        :before-upload="beforeUpload"
                        :on-success="uploadSuccess"
                        :on-error="uploadError"
                        :disabled="disableUpload"
                        :show-file-list="false">
                    <el-button type="success" icon="el-icon-upload2" :disabled="disableUpload">
                        {{uploadBtnText}}
                    </el-button>
                </el-upload>

                <el-button type="success" @click="showDownload" icon="el-icon-download">
                    下载数据
                </el-button>
                <el-button type="success" @click="showAddEmp" icon="el-icon-plus">添加用户</el-button>
            </div>
        </div>

        <!------------------------------------  display  -------------------------------------------------->
        <div style="margin-top: 10px">
            <el-table
                    :data="tableData"
                    stripe
                    border
                    style="width: 100%"
                    v-loading="loading"
                    element-loading-text="正在加载..."
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.8)"
                    @selection-change="handleSelectionChange">
                <!--type=selection搭配selection-change-->
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <!--fixed 冻结 align=left居左-->
                <el-table-column
                        prop="name"
                        fixed
                        align="left"
                        label="姓名"
                        width="90">
                </el-table-column>
                <el-table-column
                        prop="workID"
                        label="工号"
                        align="left"
                        width="85">
                </el-table-column>
                <el-table-column
                        prop="gender"
                        label="性别"
                        align="left"
                        width="85">
                </el-table-column>
                <el-table-column
                        prop="birthday"
                        width="95"
                        align="left"
                        label="出生日期">
                </el-table-column>
                <el-table-column
                        prop="idCard"
                        width="150"
                        align="left"
                        label="身份证号码">
                </el-table-column>
                <el-table-column
                        prop="wedlock"
                        width="70"
                        label="婚姻状况">
                </el-table-column>
                <el-table-column
                        prop="nation.name"
                        width="50"
                        label="民族">
                </el-table-column>
                <el-table-column
                        prop="nativePlace"
                        width="80"
                        label="籍贯">
                </el-table-column>
                <el-table-column
                        prop="politicsstatus.name"
                        label="政治面貌">
                </el-table-column>
                <el-table-column
                        prop="email"
                        width="180"
                        align="left"
                        label="电子邮件">
                </el-table-column>
                <el-table-column
                        prop="phone"
                        width="100"
                        align="left"
                        label="电话号码">
                </el-table-column>
                <el-table-column
                        prop="address"
                        width="220"
                        align="left"
                        label="联系地址">
                </el-table-column>
                <el-table-column
                        prop="department.name"
                        width="100"
                        align="left"
                        label="所属部门">
                </el-table-column>
                <el-table-column
                        prop="position.name"
                        width="100"
                        label="职位">
                </el-table-column>
                <el-table-column
                        prop="jobLevel.name"
                        width="100"
                        label="职称">
                </el-table-column>
                <el-table-column
                        prop="engageForm"
                        width="100"
                        align="left"
                        label="聘用形式">
                </el-table-column>
                <el-table-column
                        prop="tiptopDegree"
                        width="80"
                        align="left"
                        label="最高学历">
                </el-table-column>
                <el-table-column
                        prop="specialty"
                        width="150"
                        align="left"
                        label="专业">
                </el-table-column>
                <el-table-column
                        prop="school"
                        width="150"
                        align="left"
                        label="毕业院校">
                </el-table-column>
                <el-table-column
                        prop="beginDate"
                        width="95"
                        align="left"
                        label="入职日期">
                </el-table-column>
                <el-table-column
                        prop="conversionTime"
                        width="95"
                        align="left"
                        label="转正日期">
                </el-table-column>
                <el-table-column
                        prop="beginContract"
                        width="95"
                        align="left"
                        label="合同起始日期">
                </el-table-column>
                <el-table-column
                        prop="endContract"
                        width="95"
                        align="left"
                        label="合同截止日期">
                </el-table-column>
                <el-table-column
                        width="100"
                        align="left"
                        label="合同期限">
                    <template slot-scope="scope">
                        <el-tag>{{scope.row.contractTerm}}</el-tag>
                        年
                    </template>
                </el-table-column>
                <!--操作按钮-->
                <el-table-column
                        fixed="right"
                        width="200"
                        label="操作">
                    <template slot-scope="scope">
                        <el-button @click="showEditEmpView(scope.row)" style="padding: 3px" size="mini">编辑</el-button>
                        <el-button style="padding: 3px" size="mini" type="primary">查看高级资料</el-button>
                        <el-button @click="deleteEmp(scope.row)" style="padding: 3px" size="mini" type="danger">删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <!--分页-->
            <div style="display: flex;justify-content: flex-end">
                <el-pagination
                        ref="pagination"
                        background
                        @current-change="pageChange"
                        @size-change="sizeChange"
                        :page-sizes="[20,30,50,100]"
                        layout="sizes, prev, pager, next, jumper, ->, total, slot"
                        :total="total">
                </el-pagination>
            </div>
            <!------------------------------  dialog  ----------------------------------------------->
            <el-dialog
                    :title="title"
                    :visible.sync="dialogVisible"
                    :close-on-press-escape="false"
                    width="80%">
                <div>
                    <!-- :model要提交的数据 :rules 校验规则-->
                    <el-form :model="emp" ref="empForm" :rules="rules">
                        <el-row>
                            <el-col :span="6">
                                <el-form-item label="姓名:" prop="name">
                                    <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                                              v-model="emp.name"
                                              placeholder="请输入员工姓名"/>
                                </el-form-item>
                            </el-col>
                            <el-col :span="5">
                                <el-form-item label="性别:" prop="gender">
                                    <el-radio-group v-model="emp.gender">
                                        <el-radio label="男">男</el-radio>
                                        <el-radio label="女">女</el-radio>
                                    </el-radio-group>
                                </el-form-item>
                            </el-col>
                            <el-col :span="6">
                                <el-form-item label="出生日期:" prop="birthday">
                                    <el-date-picker
                                            v-model="emp.birthday"
                                            size="mini"
                                            type="date"
                                            value-format="yyyy-MM-dd"
                                            style="width: 150px;"
                                            placeholder="出生日期">
                                    </el-date-picker>
                                </el-form-item>
                            </el-col>
                            <el-col :span="7">
                                <el-form-item label="政治面貌:" prop="politicId">
                                    <el-select v-model="emp.politicId" placeholder="政治面貌" size="mini"
                                               style="width: 200px;">
                                        <el-option
                                                v-for="item in politicsstatus"
                                                :key="item.id"
                                                :label="item.name"
                                                :value="item.id">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :span="6">
                                <el-form-item label="民族:" prop="nationId">
                                    <el-select v-model="emp.nationId" placeholder="民族" size="mini"
                                               style="width: 150px;">
                                        <el-option
                                                v-for="item in nations"
                                                :key="item.id"
                                                :label="item.name"
                                                :value="item.id">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="5">
                                <el-form-item label="籍贯:" prop="nativePlace">
                                    <el-input size="mini" style="width: 120px" prefix-icon="el-icon-edit"
                                              v-model="emp.nativePlace" placeholder="请输入籍贯"/>
                                </el-form-item>
                            </el-col>
                            <el-col :span="6">
                                <el-form-item label="电子邮箱:" prop="email">
                                    <el-input size="mini" style="width: 150px" prefix-icon="el-icon-message"
                                              v-model="emp.email" placeholder="请输入电子邮箱"/>
                                </el-form-item>
                            </el-col>
                            <el-col :span="7">
                                <el-form-item label="联系地址:" prop="address">
                                    <el-input size="mini" style="width: 200px" prefix-icon="el-icon-edit"
                                              v-model="emp.address" placeholder="请输入联系地址"/>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :span="6">
                                <el-form-item label="职位:" prop="posId">
                                    <el-select v-model="emp.posId" placeholder="职位" size="mini" style="width: 150px;">
                                        <el-option
                                                v-for="item in positions"
                                                :key="item.id"
                                                :label="item.name"
                                                :value="item.id">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="5">
                                <el-form-item label="职称:" prop="jobLevelId">
                                    <!-- el-select v-model的值为:value的值 多选时 是个数组 -->
                                    <el-select v-model="emp.jobLevelId" placeholder="职称" size="mini"
                                               style="width: 150px;">
                                        <el-option
                                                v-for="item in joblevels"
                                                :key="item.id"
                                                :label="item.name"
                                                :value="item.id">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="6">
                                <el-form-item label="所属部门:" prop="departmentId">

                                    <el-popover
                                            placement="right"
                                            title="请选择部门"
                                            width="200"
                                            trigger="manual"
                                            v-model="popVisible">

                                        <el-tree default-expand-all :data="allDeps" :props="defaultProps"
                                                 @node-click="handleNodeClick"/>

                                        <div slot="reference"
                                             style="width: 150px;display: inline-flex;font-size: 13px;border: 1px solid #dedede;height: 26px;border-radius: 5px;cursor: pointer;align-items: center;padding-left: 8px;box-sizing: border-box"
                                             @click="showDepView">{{inputDepName}}
                                        </div>

                                    </el-popover>

                                </el-form-item>
                            </el-col>
                            <el-col :span="7">
                                <el-form-item label="电话号码:" prop="phone">
                                    <el-input size="mini" style="width: 200px" prefix-icon="el-icon-phone"
                                              v-model="emp.phone" placeholder="电话号码"/>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :span="6">
                                <el-form-item label="工号:" prop="workID">
                                    <el-input size="mini" style="width: 150px" placeholder="工号"
                                              v-model="emp.workID" disabled/>
                                </el-form-item>
                            </el-col>
                            <el-col :span="5">
                                <el-form-item label="学历:" prop="tiptopDegree">
                                    <el-select v-model="emp.tiptopDegree" placeholder="学历" size="mini"
                                               style="width: 150px;">
                                        <el-option
                                                v-for="item in tiptopDegrees"
                                                :key="item"
                                                :label="item"
                                                :value="item">
                                        </el-option>
                                    </el-select>
                                </el-form-item>
                            </el-col>
                            <el-col :span="6">
                                <el-form-item label="毕业院校:" prop="school">
                                    <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                                              v-model="emp.school" placeholder="毕业院校名称"/>
                                </el-form-item>
                            </el-col>
                            <el-col :span="7">
                                <el-form-item label="专业名称:" prop="specialty">
                                    <el-input size="mini" style="width: 200px" prefix-icon="el-icon-edit"
                                              v-model="emp.specialty" placeholder="请输入专业名称"/>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :span="6">
                                <!--时间控件 value-format="yyyy-MM-dd"指定时间格式-->
                                <el-form-item label="入职日期:" prop="beginDate">
                                    <el-date-picker
                                            v-model="emp.beginDate"
                                            size="mini"
                                            type="date"
                                            value-format="yyyy-MM-dd"
                                            style="width: 130px;"
                                            placeholder="入职日期">
                                    </el-date-picker>
                                </el-form-item>
                            </el-col>
                            <el-col :span="6">
                                <el-form-item label="转正日期:" prop="conversionTime">
                                    <el-date-picker
                                            v-model="emp.conversionTime"
                                            size="mini"
                                            type="date"
                                            value-format="yyyy-MM-dd"
                                            style="width: 130px;"
                                            placeholder="转正日期">
                                    </el-date-picker>
                                </el-form-item>
                            </el-col>
                            <el-col :span="6">
                                <el-form-item label="合同起始日期:" prop="beginContract">
                                    <el-date-picker
                                            v-model="emp.beginContract"
                                            size="mini"
                                            type="date"
                                            value-format="yyyy-MM-dd"
                                            style="width: 130px;"
                                            placeholder="合同起始日期">
                                    </el-date-picker>
                                </el-form-item>
                            </el-col>
                            <el-col :span="6">
                                <el-form-item label="合同终止日期:" prop="endContract">
                                    <el-date-picker
                                            v-model="emp.endContract"
                                            size="mini"
                                            type="date"
                                            value-format="yyyy-MM-dd"
                                            style="width: 150px;"
                                            placeholder="合同终止日期">
                                    </el-date-picker>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row>
                            <el-col :span="8">
                                <el-form-item label="身份证号码:" prop="idCard">
                                    <el-input size="mini" style="width: 180px" prefix-icon="el-icon-edit"
                                              v-model="emp.idCard" placeholder="请输入身份证号码"/>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="聘用形式:" prop="engageForm">
                                    <el-radio-group v-model="emp.engageForm">
                                        <el-radio label="劳动合同">劳动合同</el-radio>
                                        <el-radio label="劳务合同">劳务合同</el-radio>
                                    </el-radio-group>
                                </el-form-item>
                            </el-col>
                            <el-col :span="8">
                                <el-form-item label="婚姻状况:" prop="wedlock">
                                    <el-radio-group v-model="emp.wedlock">
                                        <el-radio label="已婚">已婚</el-radio>
                                        <el-radio label="未婚">未婚</el-radio>
                                        <el-radio label="离异">离异</el-radio>
                                    </el-radio-group>
                                </el-form-item>
                            </el-col>
                        </el-row>
                    </el-form>
                </div>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="dialogVisible = false">取 消</el-button>
                    <el-button type="primary" @click="doAddEmp">确 定</el-button>
                </span>
            </el-dialog>


        </div>
    </div>
</template>

<script>
    export default {
        name: "EmpBasic",
        data() {
            return {
                uploadBtnText: '上传数据',
                uploadBtnIcon: '',
                disableUpload: false,
                inputDepName: '',
                keyWord: '',
                title: '',
                tableData: [],
                loading: false,
                popVisible: false,
                total: 10,
                size: 20,
                page: 1,
                dialogVisible: false,
                /* rules: [],*/
                nations: [],
                politicsstatus: [],
                positions: [],
                joblevels: [],
                allDeps: [],
                tiptopDegrees: ['本科', '大专', '硕士', '博士', '高中', '初中', '小学', '其他'],
                defaultProps: {
                    label: "name",
                    children: 'children'
                },
                // emp: {
                //     id:0,
                //     name: "",
                //     gender: "",
                //     birthday: "",
                //     idCard: "",
                //     wedlock: "",
                //     nationId: null,
                //     nativePlace: "",
                //     politicId: null,
                //     email: "",
                //     phone: "1",
                //     address: "",
                //     departmentId: null,
                //     jobLevelId: null,
                //     posId: null,
                //     engageForm: "",
                //     tiptopDegree: "",
                //     specialty: "",
                //     school: "",
                //     beginDate: "",
                //     workState: null,
                //     workID: "",
                //     contractTerm: null,
                //     conversionTime: "",
                //     //notworkDate: null,
                //     beginContract: "",
                //     endContract: "",
                //     workAge: null
                // },
                emp: {
                    name: "javaboy",
                    gender: "男",
                    birthday: "1989-12-31",
                    idCard: "610122199001011256",
                    wedlock: "已婚",
                    nationId: 1,
                    nativePlace: "陕西",
                    politicId: 13,
                    email: "laowang@qq.com",
                    phone: "18565558897",
                    address: "深圳市南山区",
                    departmentId: null,
                    jobLevelId: 9,
                    posId: 29,
                    engageForm: "劳务合同",
                    tiptopDegree: "本科",
                    specialty: "信息管理与信息系统",
                    school: "深圳大学",
                    beginDate: "2017-12-31",
                    workState: "在职",
                    workID: "00000057",
                    contractTerm: 2,
                    conversionTime: "2018-03-31",
                    notworkDate: null,
                    beginContract: "2017-12-31",
                    endContract: "2019-12-31",
                    workAge: null
                },
                rules: {
                    name: [{required: true, message: '请输入用户名', trigger: 'blur'}],
                    gender: [{required: true, message: '请输入性别', trigger: 'blur'}],
                    birthday: [{required: true, message: '请输入出生日期', trigger: 'blur'}],
                    idCard: [{required: true, message: '请输入身份证号码', trigger: 'blur'}, {
                        pattern: /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/,
                        message: '身份证号码格式不正确',
                        trigger: 'blur'
                    }],
                    wedlock: [{required: true, message: '请输入婚姻状况', trigger: 'blur'}],
                    nationId: [{required: true, message: '请输入您组', trigger: 'blur'}],
                    nativePlace: [{required: true, message: '请输入籍贯', trigger: 'blur'}],
                    politicId: [{required: true, message: '请输入政治面貌', trigger: 'blur'}],
                    email: [{required: true, message: '请输入邮箱地址', trigger: 'blur'}, {
                        type: 'email',
                        message: '邮箱格式不正确',
                        trigger: 'blur'
                    }],
                    phone: [{required: true, message: '请输入电话号码', trigger: 'blur'}],
                    address: [{required: true, message: '请输入员工地址', trigger: 'blur'}],
                    departmentId: [{required: true, message: '请输入部门名称', trigger: 'blur'}],
                    jobLevelId: [{required: true, message: '请输入职称', trigger: 'blur'}],
                    posId: [{required: true, message: '请输入职位', trigger: 'blur'}],
                    engageForm: [{required: true, message: '请输入聘用形式', trigger: 'blur'}],
                    tiptopDegree: [{required: true, message: '请输入学历', trigger: 'blur'}],
                    specialty: [{required: true, message: '请输入专业', trigger: 'blur'}],
                    school: [{required: true, message: '请输入毕业院校', trigger: 'blur'}],
                    beginDate: [{required: true, message: '请输入入职日期', trigger: 'blur'}],
                    workState: [{required: true, message: '请输入工作状态', trigger: 'blur'}],
                    workID: [{required: true, message: '请输入工号', trigger: 'blur'}],
                    contractTerm: [{required: true, message: '请输入合同期限', trigger: 'blur'}],
                    conversionTime: [{required: true, message: '请输入转正日期', trigger: 'blur'}],
                    notworkDate: [{required: true, message: '请输入离职日期', trigger: 'blur'}],
                    beginContract: [{required: true, message: '请输入合同起始日期', trigger: 'blur'}],
                    endContract: [{required: true, message: '请输入合同结束日期', trigger: 'blur'}],
                    workAge: [{required: true, message: '请输入工龄', trigger: 'blur'}],
                }
            }
        },
        mounted() {
            this.initData()
            // ------------------获取sessionStorage中的选项值-------------------------------
            if (!window.sessionStorage.getItem('nations') ||
                !window.sessionStorage.getItem('joblevels') ||
                !window.sessionStorage.getItem('politicsstatus') ||
                !window.sessionStorage.getItem('positions')) {
                this.initNationsAndPoliticAndJob()
            } else {
                this.nations = JSON.parse(window.sessionStorage.getItem('nations'))
                this.joblevels = JSON.parse(window.sessionStorage.getItem('joblevels'))
                this.politicsstatus = JSON.parse(window.sessionStorage.getItem('politicsstatus'))
                this.positions = JSON.parse(window.sessionStorage.getItem('positions'))
                this.allDeps = JSON.parse(window.sessionStorage.getItem('allDeps'))
            }
        },
        methods: {
            //-----------------------  上传  --------------------------------------------
            // :show-file-list="false" 的处理方式 修改按钮样式
            uploadError(err, file, fileList) {
                this.uploadBtnText = '上传数据'
                this.uploadBtnIcon = 'el-icon-upload2'
                this.disableUpload = false
            },
            uploadSuccess(response, file, fileList) {
                this.uploadBtnText = '上传数据'
                this.uploadBtnIcon = 'el-icon-upload2'
                this.disableUpload = false
            },
            // file 要上传的文件
            beforeUpload(file) {
                this.uploadBtnText = '正在导入'
                this.uploadBtnIcon = 'el-icon-loading'
                this.disableUpload = true

            },

            // dep-tree点击事件
            handleNodeClick(data) {
                this.popVisible = false
                this.inputDepName = data.name
                this.emp.departmentId = data.id
            },
            showDepView() {
                this.popVisible = !this.popVisible
            },
            // ---------------------初始化选项数据----------------------------
            initNationsAndPoliticAndJob() {
                this.getRequest("/employee/basic/nations").then(resp => {
                    if (resp) {
                        this.nations = resp
                        window.sessionStorage.setItem('nations', JSON.stringify(this.nations))
                    }
                })
                this.getRequest("/employee/basic/joblevels").then(resp => {
                    if (resp) {
                        this.joblevels = resp
                        window.sessionStorage.setItem('joblevels', JSON.stringify(this.joblevels))

                    }
                })
                this.getRequest("/employee/basic/politicsstatus").then(resp => {
                    if (resp) {
                        this.politicsstatus = resp
                        window.sessionStorage.setItem('politicsstatus', JSON.stringify(this.politicsstatus))

                    }
                })
                this.getRequest("/employee/basic/pos").then(resp => {
                    if (resp) {
                        this.positions = resp
                        window.sessionStorage.setItem('positions', JSON.stringify(this.positions))

                    }
                })
                this.getRequest("/employee/basic/dep").then(resp => {
                    if (resp) {
                        this.allDeps = resp.data
                        window.sessionStorage.setItem('allDeps', JSON.stringify(this.allDeps))
                    }
                })
                /**
                 * bug 方法里的请求是异步的 请求返回前就往下执行了 这个时候不要去使用请求里被赋值的对象
                 */
                // window.sessionStorage 浏览器关闭清除 注销登录得手动移除 浏览器共享

                // console.log('all2:' + JSON.stringify(this.allDeps));  null
                // console.log('get:'+window.sessionStorage.getItem('allDeps')); null
            },
            // ---------------------  分页  ----------------------------
            pageChange(currentPage) {
                this.page = currentPage
                this.initData()
            },
            sizeChange(currentSize) {
                this.size = currentSize
                this.initData()
            },
            // 因为外键问题无法删除,既 employee表id作为其他表的外键 且有某些记录的id值是其他表的外键值 则这些记录无法删除
            deleteEmp(row) {
                this.$confirm('此操作将永久删除[' + row.name + '], 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteRequest("/employee/basic/" + row.id).then(resp => {
                        if (resp) {
                            this.initData();
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            showEditEmpView(row) {
                this.title = '编辑员工'
                this.dialogVisible = true
                // 不用再到后台获取了 init查询时已经获得数据了
                this.emp = row
                //this.inputDepName =
                // this.getRequest("/employee/basic/?id=" + row.id).then(resp => {
                //     if (resp) {
                //         this.emp = resp.data[0]
                //         this.emp.nationId = resp.data[0].nation.id
                //         this.emp.departmentId = resp.data[0].department.id
                //         this.emp.jobLevelId = resp.data[0].jobLevel.id
                //         this.emp.posId = resp.data[0].position.id
                //         this.emp.politicId = resp.data[0].politicsstatus.id
                //         this.inputDepName = resp.data[0].department.name
                //
                //     }
                // })
            },

            handleSelectionChange() {

            },
            emptyEmp() {
                this.emp = {
                    name: "",
                    gender: "",
                    birthday: "",
                    idCard: "",
                    wedlock: "",
                    nationId: null,
                    nativePlace: "",
                    politicId: null,
                    email: "",
                    phone: "",
                    address: "",
                    departmentId: null,
                    jobLevelId: null,
                    posId: null,
                    engageForm: "",
                    tiptopDegree: "",
                    specialty: "",
                    school: "",
                    beginDate: "",
                    workState: null,
                    workID: "",
                    contractTerm: null,
                    conversionTime: "",
                    //notworkDate: null,
                    beginContract: "",
                    endContract: "",
                    workAge: null
                }
            },
            showAddEmp() {
                // 复用dialog 清空emp
                //this.emptyEmp()
                this.title = '添加员工'
                // 自动生成最大工号
                this.getRequest('/employee/basic/max').then(resp => {
                    if (resp) {
                        this.emp.workID = resp.data
                    }
                })
                this.dialogVisible = true

            },
            doAddEmp() {
                // let boolean  = this.$refs.empForm === this.$refs['empForm']
                // console.log(boolean); true 验证确实true

                // 复用 dialog  根据数据是否有id值判断是添加还是修改
                if (this.emp.id == null) { // data里定义对象数据值 可以没有某个属性(前提是没有被v-model引用的话) 空对象也可以接受后台的数据并且添加属性和值
                    // 添加
                    // 表单校验
                    this.$refs['empForm'].validate(valid => {
                        if (valid) {
                            this.postRequest("/employee/basic/", this.emp).then(resp => {
                                if (resp) {
                                    this.dialogVisible = false
                                    this.initData()
                                }
                            })
                        } else {
                            // 校验不通过会自动显示rules里面{message元素}的内容
                            // this.$message.warn("请输入所有必填字段")
                        }
                    })
                } else {
                    // 修改
                    // 表单校验
                    this.$refs['empForm'].validate(valid => {
                        if (valid) {
                            this.putRequest("/employee/basic/", this.emp).then(resp => {
                                if (resp) {
                                    this.dialogVisible = false
                                    this.initData()
                                }
                            })
                        }
                    })
                }

            },
            showDownload() {
                window.open('employee/basic/export', '_parent')
            },
            showUpload() {

            },
            doDetailSearch() {

            },
            doSearch() {
                // 包含分页的检索只能后端做了
                this.initData()

            },
            initData() {
                this.loading = true
                this.getRequest("/employee/basic/?page=" + this.page + '&size=' + this.size + '&keyWord=' + this.keyWord).then(resp => {
                    this.loading = false
                    if (resp) {
                        this.tableData = resp.data
                        this.total = resp.total
                    }
                })
            }
        }
    }
</script>

<style>
    /* 可以设置不同的进入和离开动画 */
    /* 设置持续时间和动画函数 */
    .slide-fade-enter-active {
        transition: all .8s ease;
    }

    .slide-fade-leave-active {
        transition: all .8s cubic-bezier(1.0, 0.5, 0.8, 1.0);
    }

    .slide-fade-enter, .slide-fade-leave-to
        /* .slide-fade-leave-active for below version 2.1.8 */
    {
        transform: translateX(10px);
        opacity: 0;
    }
</style>