require(['vue','ELEMENT','ajax', 'vxe-table', 'vueselect', 'org-orgtree','house-select', 'util', 'cust-info', 'order-info', 'order-worker', 'order-search-employee', 'order-file-upload', 'upload-file'], function(Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util, custInfo, orderInfo, orderWorker, orderSearchEmployee, orderFileUpload) {
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
                        { required: true, message: '请先选择课程', trigger: 'change' }
                    ],
                    courseName: [
                        { required: true, message: '课程名称不能为空', trigger: 'blur' }
                    ],
                    courseType: [
                        { required: true, message: '课程类型不能为空', trigger: 'blur' }
                    ],
                    courseStudyOrder: [
                        { required: true, message: '课程学习顺序不能为空', trigger: 'blur' },
                        { type: 'number', message: '课程学习顺序必须为数字值', trigger: 'blur'}
                    ],
                    staffRank: [
                        { required: true, message: '员工职级不能为空', trigger: 'blur' },
                        { type: 'number', message: '员工职级必须为数字值', trigger: 'blur'}
                    ],
                    taskType: [
                        { required: true, message: '任务类型不能为空', trigger: 'blur'}
                    ],
                    studyTime: [
                        { required: true, message: '学习时间不能为空', trigger: 'blur' },
                        { type: 'number', message: '学习时间必须为数字值', trigger: 'blur'}
                    ]
                },
                courseTaskInfo: [],
                courseTaskTypes: [],
                taskCoursewareTypes: [],
                addCourseTaskDialogVisible: false,
                courseChaptersDetails: [],
                courseChaptersItem: {},
                addCourseTaskInfo: {},
                selectCurrent: {},
                courseInfos: [],
                showUpload: '',
                showTree: '',
                defaultProps: {
                    children: 'children',
                    label: 'courseName'
                },
                courseId: '',
                courseName: '',
                courseStudyModelTypes: [],
                chaptersTypes: [],
                taskCoursewareType: ''
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
            this.taskCoursewareTypes = [
                {value : "0", label : "选择课程"},
                {value : "1", label : "上传课件"}
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
                let that = this;
                that.addCourseTaskDialogVisible = true;
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
                this.$refs.addCourseTaskInfo.validate((valid) => {
                    if(valid){
                        let that = this;
                        this.$confirm('是否保存新增课程任务?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            that.addCourseTaskInfo.courseChaptersList = courseChaptersDetails
                            ajax.post('api/CollegeCourseTaskCfg/addCourseTaskCfg', that.addCourseTaskInfo, function(){
                                this.$message({
                                    showClose: true,
                                    message: '课程任务新增成功',
                                    type: 'success'
                                });
                            });
                        })
                    }
                });
            },
            cancel: function () {
                let that = this;
                that.addCourseTaskDialogVisible = false;
                that.addCourseTaskInfo = [];
                that.courseChaptersDetails = [];
            },
            changeTaskCoursewareType: function (val) {
                if (1 == val){
                    this.$refs.upload.display = '';
                    this.$refs.tree.style = 'display:none';
                }else if (0 == val){
                    this.$nextTick(()=>{
                        this.$refs.addCourseTaskInfo.resetFields();
                    });
                    let that = this;
                    if (null == that.courseInfos || [] == that.courseInfos || undefined == that.courseInfos || that.courseInfos.length == 0){
                        ajax.get('api/organization/course/qeuryCourseTree', null, function(responseData){
                            that.courseInfos = responseData;
                            that.taskCoursewareType = val;
                        });
                    }
                }
            },
            handleClick() {

            },
            handleSuccess: function (response, file, fileList) {
                alert("fileId: " + this.$refs.upload.fileId);
            },
        },

        /*mounted () {
            this.init();
        }*/
    });

    return vm;
});