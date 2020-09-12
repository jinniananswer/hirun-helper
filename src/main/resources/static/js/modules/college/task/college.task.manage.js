require(['vue','ELEMENT','ajax', 'vxe-table', 'vueselect', 'org-orgtree','house-select', 'util', 'cust-info', 'order-info', 'order-worker', 'order-search-employee', 'order-file-upload', 'upload-file'], function(Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util, custInfo, orderInfo, orderWorker, orderSearchEmployee, orderFileUpload) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            var checkChaptersExercisesNumber = (rule, value, callback) => {
                let studyExercisesNumber = this.courseChaptersDetails.exercisesNumber;
                if (studyExercisesNumber != undefined && studyExercisesNumber != 'undefined' && studyExercisesNumber != null && studyExercisesNumber != 'null'){
                    if (!value) {
                        return callback(new Error('学习内容有习题次数，章节信息习题次数不能为空'));
                    }else {
                        if (!Number.isInteger(value)){
                            callback(new Error('章节习题次数只能是数字'));
                        }else {
                            if (value.toString().length > 2){
                                callback(new Error('章节习题次数请设置成100次以内'));
                            }else if(value > studyExercisesNumber){
                                callback(new Error('章节习题次数请不能大于学习内容习题次数'));
                            }else {
                                callback();
                            }
                        }
                    }
                }
            };
            return {
                queryCond: {
                    taskType: '',
                    studyId: '',
                    studyName: '',
                    studyTaskId: '',
                    limit: 20,
                    page: 1,
                    count: null
                },
                studyTaskRules: {
                    studyId: [
                        { required: true, message: '请先选择学习内容', trigger: 'change' }
                    ],
                    studyName: [
                        { required: true, message: '学习内容名称不能为空', trigger: 'blur' }
                    ],
                    studyType: [
                        { required: true, message: '学习内容类型不能为空', trigger: 'blur' }
                    ],
                    studyOrder: [
                        { required: true, message: '学习顺序不能为空', trigger: 'blur' },
                        { type: 'number', message: '学习顺序必须为数字值', trigger: 'blur'}
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
                    ],
                    exercisesNumber: [
                        { type: 'number', message: '习题次数必须为数字值', trigger: 'blur'}
                    ],
                    passScore: [
                        { type: 'number', message: '考试合格分数必须为数字值', trigger: 'blur'}
                    ]
                },
                courseChaptersRules: {
                    chaptersStudyOrder: [
                        { required: true, message: '章节学习顺序不能为空', trigger: 'blur' },
                        { type: 'number', message: '章节学习顺序必须为数字值', trigger: 'blur'}
                    ],
                    chaptersType: [
                        { required: true, message: '章节类型不能为空', trigger: 'blur' }
                    ],
                    studyTime: [
                        { required: true, message: '学习时间不能为空', trigger: 'blur'},
                        { type: 'number', message: '学习时间必须为数字值', trigger: 'blur'}
                    ],
                    studyModel: [
                        { required: true, message: '学习模式不能为空', trigger: 'blur' },
                        { type: 'number', message: '学习模式必须为数字值', trigger: 'blur'}
                    ],
                    exercisesNumber: [
                        { validator: checkChaptersExercisesNumber, trigger: 'blur'  }
                    ],
                    passScore: [
                        { type: 'number', message: '考试合格分数必须为数字值', trigger: 'blur'}
                    ]
                },
                studyTaskInfo: [],
                studyTaskTypes: [],
                taskCoursewareTypes: [],
                addStudyTaskDialogVisible: false,
                editStudyTaskDialogVisible: false,
                addChaptersDialogVisible: false,
                editChaptersDialogVisible: false,
                courseChaptersDetails: [],
                courseChaptersItem: {},
                addStudyTaskInfo: {},
                addChaptersInfo: {},
                editChaptersInfo: {},
                editChaptersInfoSource: {},
                editStudyTaskInfo: {},
                selectCurrent: {},
                studyChaptersList: [],
                studyInfos: [],
                showUpload: 'display:none',
                showTree: 'display:block',
                defaultProps: {
                    children: 'children',
                    label: 'studyName'
                },
                studyId: '',
                studyName: '',
                studyType: '',
                courseStudyModelTypes: [],
                chaptersTypes: [],
                taskCoursewareType: ''
            }
        },
        mounted: function() {
            this.studyTaskTypes = [
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
                ajax.get('api/CollegeStudyTaskCfg/queryCollegeStufyByPage', this.queryCond, function(responseData){
                    that.studyTaskInfo = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;
                });
            },
            handleCourseTaskSelectionChange(val) {
                this.multipleSelection = val;
            },
            addStudyTask: function(){
                let that = this;
                that.addStudyTaskDialogVisible = true;
                that.addStudyTaskInfo = {}
            },
            handleNodeClick: function(data) {
                if (data.courseFlag){
                    this.addStudyTaskInfo = data;
                    this.addStudyTaskInfo.studyType = this.studyType;
                    this.studyId = data.studyId;
                    this.studyName = data.studyName;
                }else {
                    this.addStudyTaskInfo = {};
                    this.addStudyTaskInfo.studyType = this.studyType;
                    this.studyId = '';
                    this.studyName = '';
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
                    studyId: this.studyId,
                    chaptersName: this.studyName,
                    chaptersStudyOrder: 1,
                    chaptersType: 0,
                    studyTime: 1,
                    studyModel: 1
                };
                let that = this;
                if (that.studyId == '' || that.studyId == null || that.studyId == undefined || that.studyId == 'undefined' || that.studyId == 'null'){
                    this.$message('请先选择课程！');
                    return;
                }
                let chaptersNum = 1;
                if (null != that.addStudyTaskInfo.courseChaptersList && undefined != that.addStudyTaskInfo.courseChaptersList && that.addStudyTaskInfo.courseChaptersList.length > 0){
                    chaptersNum = that.addStudyTaskInfo.courseChaptersList.length + 1;
                }
                this.courseChaptersItem.chaptersName = that.studyName + '章节' + chaptersNum;
                that.$refs.courseChaptersDetails.insertAt(that.courseChaptersItem, 0);
                that.courseChaptersDetails.push(this.courseChaptersItem)
                that.addStudyTaskInfo.courseChaptersList = that.courseChaptersDetails
            },
            changeEvent ({ row }, evnt) {

            },
            submitAdd: function (courseChaptersDetails) {
                this.$refs.addStudyTaskInfo.validate((valid) => {
                    if(valid){
                        let that = this;
                        this.$confirm('是否保存新增课程任务?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            that.addStudyTaskInfo.courseChaptersList = courseChaptersDetails
                            ajax.post('api/CollegeStudyTaskCfg/addStudyTaskCfg', that.addStudyTaskInfo, function(responseData){
                                that.$message({
                                    showClose: true,
                                    message: '课程任务新增成功',
                                    type: 'success'
                                });
                                that.addStudyTaskDialogVisible = false;
                            });
                        })
                    }
                });
            },
            cancel: function () {
                let that = this;
                that.addStudyTaskDialogVisible = false;
                that.addStudyTaskInfo = {};
                that.courseChaptersDetails = [];
                that.editStudyTaskDialogVisible = false;
                that.addChaptersDialogVisible = false;
                that.editChaptersDialogVisible = false;
            },
            changeTaskCoursewareType: function (val) {
                this.studyType = val
                if (1 == val){
                    let that = this
                    that.showUpload = 'display:block'
                    that.showTree = 'display:none'
                    that.courseChaptersDetails = [];
                    that.addStudyTaskInfo = {};
                    that.studyId = '';
                    that.studyName = '';
                }else if (0 == val){
                    this.$nextTick(()=>{
                        this.$refs.addStudyTaskInfo.resetFields();
                    });
                    this.showUpload = 'display:none'
                    this.showTree = 'display:block'
                    let that = this;
                    ajax.get('api/organization/course/qeuryCourseTree', null, function(responseData){
                        that.studyInfos = responseData;
                        that.taskCoursewareType = val;
                    });
                    /*if (null == that.studyInfos || [] == that.studyInfos || undefined == that.studyInfos || that.studyInfos.length == 0){
                        ajax.get('api/organization/course/qeuryCourseTree', null, function(responseData){
                            that.studyInfos = responseData;
                            that.taskCoursewareType = val;
                        });
                    }*/
                }
            },
            handleClick() {

            },
            handleSuccess: function (fileList) {
                alert("fileId: " + JSON.stringify(fileList));
            },
            deleteStudyTaskBatch: function () {
                let val = this.multipleSelection
                if(val == undefined || val == 'undefined' || val.length <= 0){
                    this.$message({
                        showClose: true,
                        duration: 3000,
                        message: '您未选择需要删除的学习任务配置！请选择后再点击删除。',
                        center: true
                    });
                    return;
                }
                this.$confirm('是否删除选中的学习任务?本次删除同时会连带删除选中学习任务的章节信息配置！', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let that = this
                    ajax.post('api/CollegeStudyTaskCfg/deleteStudyTaskBatch', val, function(responseData){
                        that.multipleSelection.forEach((deleteStufyInfo) => {
                            let studyTaskInfoList = that.studyTaskInfo;
                            for(let i = 0 ; i < studyTaskInfoList.length ; i++){
                                if(studyTaskInfoList[i]._XID == deleteStufyInfo._XID){
                                    that.studyTaskInfo.splice(i, 1);
                                    break;
                                }
                            }
                        })
                        that.$message({
                            message: '学习任务删除成功',
                            type: 'success'
                        });
                    },null, true);
                })
            },
            deleteStudyTaskRow: function (row) {
                let deleteStudyName = row.studyName;
                this.$confirm('是否删除' + deleteStudyName + '学习任务配置?本次删除同时会连带删除章节信息配置！', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let that = this
                    ajax.post('api/CollegeStudyTaskCfg/deleteStudyTaskByRow', row, function(responseData){
                        let studyTaskInfoList = that.studyTaskInfo;
                        for(let i = 0 ; i < studyTaskInfoList.length ; i++){
                            if(studyTaskInfoList[i]._XID == row._XID){
                                that.editStudyTaskInfo.splice(i, 1);
                                break;
                            }
                        }
                    },null, true);
                    that.$message({
                        message: '学习任务删除成功',
                        type: 'success'
                    });
                })
            },
            editStudyTask: function (row) {
                let that = this;
                that.editStudyTaskInfo = JSON.parse(JSON.stringify(row));
                that.editStudyTaskDialogVisible = true;
            },
            submitStudyTaskEdit: function (editStudyTaskInfo) {
                this.$confirm('是否提交修改?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let that = this
                    ajax.post('api/CollegeStudyTaskCfg/updateStudyTaskByDTO', editStudyTaskInfo, function(responseData){
                        let studyTaskInfoList = that.studyTaskInfo;
                        for(let i = 0 ; i < studyTaskInfoList.length ; i++){
                            if(studyTaskInfoList[i]._XID == editStudyTaskInfo._XID){
                                studyTaskInfoList[i] = editStudyTaskInfo;
                                that.editStudyTaskDialogVisible = false;
                                break;
                            }
                        }
                        that.$message({
                            message: '学习任务修改成功',
                            type: 'success'
                        });
                    },null, true);
                })
            },
            addChapters: function (studyTaskInfo) {
                let that = this;
                that.addChaptersDialogVisible = true;
                that.addChaptersInfo = JSON.parse(JSON.stringify(studyTaskInfo));
            },
            submitAddChapters: function () {
                this.$confirm('是否提交保存?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let that = this
                    ajax.post('api/CollegeCourseChaptersCfg/addChapters', that.addChaptersInfo, function(responseData){
                        that.addChaptersDialogVisible = false;
                        that.$message({
                            message: '章节配置新增成功',
                            type: 'success'
                        });
                    },null, true);
                })
            },
            editChapters: function (row) {
                let that = this;
                that.editChaptersDialogVisible = true;
                that.editChaptersInfoSource = row
                that.editChaptersInfo = JSON.parse(JSON.stringify(row));
            },
            submitEditChapters: function (editChaptersInfo) {
                this.$confirm('是否提交修改?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let that = this
                    ajax.post('api/CollegeCourseChaptersCfg/editChapters', editChaptersInfo, function(responseData){
                        that.editChaptersInfoSource = editChaptersInfo
                        that.editChaptersDialogVisible = false;
                        that.$message({
                            message: '学习任务修改成功',
                            type: 'success'
                        });
                    },null, true);
                })
            }
        },

        /*mounted () {
            this.init();
        }*/
    });

    return vm;
});