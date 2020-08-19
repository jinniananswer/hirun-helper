require(['vue','ELEMENT','ajax', 'vxe-table', 'vueselect', 'org-orgtree','house-select', 'util', 'cust-info', 'order-info', 'order-worker', 'order-search-employee'], function(Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util, custInfo, orderInfo, orderWorker, orderSearchEmployee) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                queryCond: {
                    taskType: '',
                    courseId: '',
                    courseName: '',
                    courseTaskId: '',
                    limit: 20,
                    page: 1,
                    count: null
                },
                courseTaskRules: {
                    courseId: [
                        { required: true, message: '课程ID不能为空', trigger: 'change' }
                    ],
                    courseName: [
                        { required: true, message: '课程名称不能为空', trigger: 'blur' }
                    ],
                    courseType: [
                        { required: true, message: '课程类型不能为空', trigger: 'blur' }
                    ],
                    courseStudyOrder: [
                        { required: true, message: '课程学习顺序不能为空', trigger: 'blur' }
                    ],
                    item: [
                        { required: true, message: '提成项目不能为空', trigger: 'blur' }
                    ],
                    totalRoyalty: [
                        { required: true, message: '总提成不能为空', trigger: 'blur'},
                        { type: 'number', message: '总提成必须为数字值', trigger: 'blur'}
                    ],
                    alreadyFetch: [
                        { required: true, message: '已提不能为空', trigger: 'blur'},
                        { type: 'number', message: '已提必须为数字值', trigger: 'blur'}
                    ],
                    salaryMonth: [
                        { required: true, message: '发放月份不能为空', trigger: 'blur' }
                    ]
                },
                courseTaskInfo: [],
                courseTaskTypes: [],
                addCourseTaskDialogVisible: false,
                courseChaptersDetails: [],
                courseChaptersItem: {},
                addCourseTaskInfo: {},
                selectCurrent: {},
                courseInfos: [],
                defaultProps: {
                    children: 'children',
                    label: 'courseName'
                },
                courseId: '',
                courseName: '',
                courseStudyModelTypes: [],
                chaptersTypes: [],
            }
        },
        mounted: function() {
            this.courseTaskTypes = [
                {value : "1", name : "固定任务"},
                {value : "2", name : "活动任务"}
            ];
            this.courseStudyModelTypes = [
                {value : "0", label : "分批学习"},
                {value : "1", label : "同时学习"}
            ];
            this.chaptersTypes = [
                {value : "0", label : "章节类型A"},
                {value : "1", label : "章节类型B"}
            ];
        },
        methods: {
            query: function() {
                let that = this;
                ajax.get('api/CollegeCourseTaskCfg/queryCollegeCourseByPage', this.queryCond, function(responseData){
                    that.courseTaskInfo = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;
                });
            },
            handleCourseTaskSelectionChange(val) {
                this.multipleSelection = val;
            },
            fixedTaskRelease: function(){
                let val = [];
                this.multipleSelection.forEach(function (e, index, array) {
                    val.push()
                })
                this.multipleSelection.forEach((selection) => {
                    val.push(selection.employeeId)
                })
                ajax.post('api/CollegeEmployeeTask/fixedTaskReleaseByEmployeeList', val, function(responseData){

                },null, true);
            },
            addCourseTask: function(){
                this.$nextTick(()=>{
                    this.$refs.addCourseTaskInfo.resetFields();
                });
                let that = this;
                ajax.get('api/organization/course/qeuryCourseTree', null, function(responseData){
                    that.courseInfos = responseData;
                    that.addCourseTaskDialogVisible = true;
                });
            },
            handleNodeClick: function(data) {
                if (data.courseFlag){
                    this.addCourseTaskInfo = data;
                    this.courseId = data.courseId;
                    this.courseName = data.courseName;
                }else {
                    this.addCourseTaskInfo = {};
                    this.courseId = '';
                    this.courseName = '';
                }
            },
            deleteDesign: function(row){
                let courseChaptersInfos = this.courseChaptersDetails;
                for(let i = 0 ; i < courseChaptersInfos.length ; i++){
                    if(courseChaptersInfos[i]._XID == row._XID){
                        this.courseChaptersDetails.splice(i, 1);
                        break;
                    }
                }
            },
            deleteSelectedDesign: function(){
                let data = this.$refs.courseChaptersDetails.getCheckboxRecords();
                if (data == null || data.length <= 0) {
                    this.$message.error('没有选中任何记录，无法删除');
                    return;
                }
                data.forEach((courseChapters) => {
                    let courseChaptersInfos = this.courseChaptersDetails;
                    for(let i = 0 ; i < courseChaptersInfos.length ; i++){
                        if(courseChaptersInfos[i]._XID == courseChapters._XID){
                            this.courseChaptersDetails.splice(i, 1);
                            break;
                        }
                    }
                })
            },
            addDesign: function(){
                this.courseChaptersItem = {
                    courseId: this.courseId,
                    chaptersName: this.courseName,
                    chaptersStudyOrder: 1,
                    chaptersType: 0,
                    studyTime: 1,
                    studyModel: 1
                };
                let that = this;
                if (that.courseId == '' || that.courseId == null || that.courseId == undefined || that.courseId == 'undefined' || that.courseId == 'null'){
                    this.$message('请先选择课程！');
                    return;
                }
                let chaptersNum = 1;
                if (null != that.addCourseTaskInfo.courseChaptersList && undefined != that.addCourseTaskInfo.courseChaptersList && that.addCourseTaskInfo.courseChaptersList.length > 0){
                    chaptersNum = that.addCourseTaskInfo.courseChaptersList.length + 1;
                }
                this.courseChaptersItem.chaptersName = that.courseName + '章节' + chaptersNum;
                that.$refs.courseChaptersDetails.insertAt(that.courseChaptersItem, 0);
                that.courseChaptersDetails.push(this.courseChaptersItem)
                that.addCourseTaskInfo.courseChaptersList = that.courseChaptersDetails
            },
            changeEvent ({ row }, evnt) {

            },
            submitAdd: function (addCourseTaskInfo, courseChaptersDetails) {
                let that = this;
                alert(JSON.stringify(that.addCourseTaskInfo.courseChaptersList))
                that.addCourseTaskInfo.courseChaptersList = courseChaptersDetails
                alert(JSON.stringify(addCourseTaskInfo))
            }
        },

        /*mounted () {
            this.init();
        }*/
    });

    return vm;
});