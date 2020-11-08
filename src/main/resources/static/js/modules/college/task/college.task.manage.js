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
                    releaseStatus: '',
                    limit: 20,
                    page: 1,
                    count: null
                },
                studyTaskRules: {
                    taskName: [
                        { required: true, message: '任务名称不能为空', trigger: 'blur' }
                    ],
                    studyType: [
                        { required: true, message: '学习内容类型不能为空', trigger: 'blur' }
                    ],
                    jobType: [
                        { required: true, message: '员工工作类型不能为空', trigger: 'blur' }
                    ],
                    taskType: [
                        { required: true, message: '任务类型不能为空', trigger: 'blur'}
                    ],
                    studyTime: [
                        { required: true, message: '学习时间不能为空', trigger: 'blur' },
                        { type: 'number', message: '学习时间必须为数字值', trigger: 'blur'}
                    ],
                    studyStartType: [
                        { required: true, message: '任务开始方式不能为空', trigger: 'blur' }
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
                courseChaptersDetails: [],
                courseChaptersItem: {},
                addStudyTaskInfo: {},
                editStudyTaskInfo: {},
                selectCurrent: {},
                studyInfos: [],
                showUpload: 'display:none',
                showTree: 'display:block',
                defaultProps: {
                    children: 'children',
                    label: 'studyName'
                },
                showJob: 'display:none',
                showEmployee: 'display:none',
                showStudyModel: 'display:none',
                showTogetherStudyTask: 'display:none',
                showAppointDay: 'display:none',
                studyId: '',
                studyName: '',
                studyType: '',
                courseStudyModelTypes: [],
                chaptersTypes: [],
                taskCoursewareType: '',
                jobRoleInfos: [],
                employeeInfos: [],
                selectJobRoleInfos: [],
                selectEmployeeInfos: [],
                allLabelIdList: [],
                labelIdList: [],
                togetherStudyTaskList: [],
                studyTopicTypeInfo: {},
                releaseExamDialogVisible: false,
                studyTopicTypeInfoDetails: [],
                studyTopicTypeOptions: [],
                examType: '',
                showExercises: 'display:block',
                showExam: 'display:block',
                showTaskValidityTerm: 'display:none',
                showStudyLength: 'display:none',
                showTaskDesc: 'display:none',
                showExercisesNumber: 'display:none',
                showPassScore: 'display:none',
                showTopic: 'display:none',
                showNoTopic: 'display:none',
                examMaxLabel: '最多考试次数'
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
                {value : "1", label : "上传课件"},
                {value : "2", label : "实践任务"},
                {value : "3", label : "答题任务"}
            ];
            this.studyTopicTypeOptions = [
                {value : "1", label : "单选"},
                {value : "2", label : "多选"},
                {value : "3", label : "判断"},
                {value : "4", label : "填空"}
            ];
        },
        methods: {
            query: function() {
                let that = this;
                ajax.get('api/CollegeStudyTaskCfg/queryCollegeStudyByPage', this.queryCond, function(responseData){
                    that.studyTaskInfo = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;
                });
            },
            handleCourseTaskSelectionChange(val) {
                this.multipleSelection = val;
            },
            addStudyTask: function(){
                this.initAddStudyTaskDialogVisible();
                let that = this;
                that.addStudyTaskDialogVisible = true;
                that.addStudyTaskInfo = {}
                if (null == that.jobRoleInfos || [] == that.jobRoleInfos || undefined == that.jobRoleInfos || that.jobRoleInfos.length == 0){
                    ajax.get('api/CollegeStudyTaskCfg/queryJobRoleTransferInfo', null, function(responseData){
                        that.jobRoleInfos = responseData;
                    });
                }
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
                if (null == this.studyType || undefined == this.studyType || '' == this.studyType){
                    this.$message({
                        showClose: true,
                        duration: 3000,
                        message: '请选择任务学习类型后再提交!',
                        center: true
                    });
                    return;
                }

                if (null == this.addStudyTaskInfo.jobType || undefined == this.addStudyTaskInfo.jobType || '' == this.addStudyTaskInfo.jobType){
                    this.$message({
                        showClose: true,
                        duration: 3000,
                        message: '请选择员工工作类型后再提交!',
                        center: true
                    });
                    return;
                }
                if (("0" == this.addStudyTaskInfo.jobType || "1" == this.addStudyTaskInfo.jobType) && (this.selectJobRoleInfos == [] || this.selectJobRoleInfos == undefined || this.selectJobRoleInfos.length == 0)){
                    this.$message({
                        showClose: true,
                        duration: 3000,
                        message: '正式员工或新员工类型请选择员工岗位再提交!',
                        center: true
                    });
                    return;
                }
                if ("3" == this.addStudyTaskInfo.jobType && (this.selectEmployeeInfos == [] || this.selectEmployeeInfos == undefined || this.selectEmployeeInfos.length == 0)){
                    this.$message({
                        showClose: true,
                        duration: 3000,
                        message: '指定员工类型请选择指定的员工再提交!',
                        center: true
                    });
                    return;
                }
                this.addStudyTaskInfo.jobRoleInfos = this.selectJobRoleInfos;
                this.addStudyTaskInfo.employeeInfos = this.selectEmployeeInfos;
                this.addStudyTaskInfo.studyType = this.studyType;
                if (this.studyType == '1'){
                    let fileList = this.$refs.upload.fileList;
                    if (fileList == undefined || fileList == "fileList" || fileList == [] || fileList.length == 0){
                        this.$message({
                            showClose: true,
                            duration: 3000,
                            message: '请上传课件后再提交!',
                            center: true
                        });
                        return;
                    }
                    this.addStudyTaskInfo.studyId = fileList[0].response;
                    this.addStudyTaskInfo.studyName = fileList[0].name;
                }else if (this.studyType == '2'){
                    //判断任务描述
                    if (null == this.addStudyTaskInfo.taskDesc || undefined == this.addStudyTaskInfo.taskDesc || '' == this.addStudyTaskInfo.taskDesc){
                        this.$message({
                            showClose: true,
                            duration: 3000,
                            message: '实践任务，任务描述不能为空',
                            center: true
                        });
                        return;
                    }
                }else if (this.studyType == '3'){
                    this.addStudyTaskInfo.labelIdList = this.labelIdList;
                    this.addStudyTaskInfo.studyTopicTypeInfoDetails = this.studyTopicTypeInfoDetails;
                    let answerTaskName = this.addStudyTaskInfo.taskName;
                    if (undefined == answerTaskName || null == answerTaskName || '' == answerTaskName){
                        this.$message({
                            showClose: true,
                            duration: 3000,
                            message: '请设置任务名称！',
                            center: true
                        });
                        return;
                    }
                    let answerTaskType = this.addStudyTaskInfo.answerTaskType;
                    if (undefined == answerTaskType || null == answerTaskType || '' == answerTaskType){
                        this.$message({
                            showClose: true,
                            duration: 3000,
                            message: '请选择答题任务类型！',
                            center: true
                        });
                        return;
                    }
                    let studyTopicTypeInfoList = this.studyTopicTypeInfoDetails;
                    if (null == studyTopicTypeInfoList || undefined == studyTopicTypeInfoList || studyTopicTypeInfoList == [] || studyTopicTypeInfoList.length <= 0){
                        this.$message({
                            showClose: true,
                            duration: 3000,
                            message: '请设置习题类型和数量。',
                            center: true
                        });
                        return;
                    }
                    if (null == this.labelIdList || undefined == this.labelIdList || [] == this.labelIdList || this.labelIdList.length == 0){
                        this.$message({
                            showClose: true,
                            duration: 3000,
                            message: '请选择题目标签后再提交',
                            center: true
                        });
                        return;
                    }
                    let topicTypes = ',';
                    let allScore = 0;
                    for (let i = 0 ; i < studyTopicTypeInfoList.length; i++){
                        let studyTopicTypeInfo = studyTopicTypeInfoList[i];
                        let type = "";
                        this.studyTopicTypeOptions.forEach((option) => {
                            if (option.value == studyTopicTypeInfo.exercisesType){
                                type = option.label;
                            }
                        });
                        if (undefined == studyTopicTypeInfo.exercisesNumber || null == studyTopicTypeInfo.exercisesNumber || studyTopicTypeInfo.exercisesNumber == 0 || studyTopicTypeInfo.exercisesNumber == '0'){
                            this.$message({
                                showClose: true,
                                duration: 3000,
                                message: '请设置' + type + '习题的数量，且不能为0',
                                center: true
                            });
                            return;
                        }
                        if (topicTypes.indexOf("," + studyTopicTypeInfo.exercisesType + ",") > -1){
                            this.$message({
                                showClose: true,
                                duration: 3000,
                                message: '习题类型' + type + '不能重复设置',
                                center: true
                            });
                            return;
                        }
                        topicTypes = topicTypes + studyTopicTypeInfo.exercisesType + ",";
                    }
                    let that = this;
                    ajax.post('api/CollegeStudyTaskCfg/addStudyTaskCfg', that.addStudyTaskInfo, function(responseData){
                        that.$message({
                            showClose: true,
                            message: '课程任务新增成功',
                            type: 'success'
                        });
                        that.studyTaskInfo.push(responseData);
                        that.addStudyTaskInfo = {};
                        that.courseChaptersDetails = [];
                        that.addStudyTaskDialogVisible = false;
                        that.studyTopicTypeInfoDetails = [];
                        that.studyTopicTypeInfo = {};
                        that.selectJobRoleInfos = [];
                        that.selectEmployeeInfos = [];
                        that.labelIdList = [];
                        that.studyTopicTypeInfoDetails = [];
                    });
                    return;
                }
                //判断学习时长
                if (this.studyType != '2'){
                    if (null == this.addStudyTaskInfo.studyLength || undefined == this.addStudyTaskInfo.studyLength || '' == this.addStudyTaskInfo.studyLength || 0 == this.addStudyTaskInfo.studyLength){
                        this.$message({
                            showClose: true,
                            duration: 3000,
                            message: '学习任务，学习时长不能为空',
                            center: true
                        });
                        return;
                    }
                    if (null == this.addStudyTaskInfo.taskName || undefined == this.addStudyTaskInfo.taskName || '' == this.addStudyTaskInfo.taskName || 0 == this.addStudyTaskInfo.taskName){
                        this.$message({
                            showClose: true,
                            duration: 3000,
                            message: '学习任务，请先选择学习内容',
                            center: true
                        });
                        return;
                    }
                }
                //判断任务开始类型
                let studyStartType = this.addStudyTaskInfo.studyStartType;
                //如果是接上个任务开始，则需要判断是否选择了学习模式
                if (3 == studyStartType){
                    let studyModel = this.addStudyTaskInfo.studyModel;
                    if (null == studyModel || undefined == studyModel || "null" == studyModel || "" == studyModel || "undefined" == studyModel){
                        this.$message({
                            showClose: true,
                            duration: 3000,
                            message: '接上个任务开始学习，请选择学习模式',
                            center: true
                        });
                        return;
                    }
                    //如果是同时学习
                    if (1 == studyModel){
                        let togetherStudyTaskId = this.addStudyTaskInfo.togetherStudyTaskId;
                        if (null == togetherStudyTaskId || undefined == togetherStudyTaskId || "null" == togetherStudyTaskId || "" == togetherStudyTaskId || "undefined" == togetherStudyTaskId){
                            this.$message({
                                showClose: true,
                                duration: 3000,
                                message: '同时学习模式，请选择同时学习任务',
                                center: true
                            });
                            return;
                        }
                    }
                }
                let taskType = this.addStudyTaskInfo.taskType;
                if (2 == taskType){
                    if (null == this.addStudyTaskInfo.taskValidityTerm || undefined == this.addStudyTaskInfo.taskValidityTerm || '' == this.addStudyTaskInfo.taskValidityTerm || 0 == this.addStudyTaskInfo.taskValidityTerm){
                        this.$message({
                            showClose: true,
                            duration: 3000,
                            message: '活动任务任务，任务有效期不能为空',
                            center: true
                        });
                        return;
                    }
                }
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
                                that.studyTaskInfo.push(responseData);
                                that.addStudyTaskInfo = {};
                                that.courseChaptersDetails = [];
                                that.addStudyTaskDialogVisible = false;
                                that.studyTopicTypeInfo = {};
                                that.selectJobRoleInfos = [];
                                that.selectEmployeeInfos = [];
                                that.labelIdList = [];
                                that.studyTopicTypeInfoDetails = [];
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
                that.showJob = 'display:none';
                this.releaseExamDialogVisible = false;
                this.studyTopicTypeInfo = {};
                this.selectJobRoleInfos = [];
                this.labelIdList = [];
                this.studyTopicTypeInfoDetails = [];
            },
            changeTaskCoursewareType: function (val) {
                this.studyType = val
                this.showStudyLength = 'display:none'
                this.showTaskDesc = 'display:none'
                this.showTree = 'display:none'
                this.showUpload = 'display:none'
                this.showExercisesNumber = 'display:block'
                this.showPassScore = 'display:block'
                this.showTopic = 'display:none'
                this.showNoTopic = 'display:block'
                if (1 == val){
                    let that = this
                    that.showUpload = 'display:block'
                    that.showStudyLength = 'display:block'
                    that.courseChaptersDetails = [];
                    that.addStudyTaskInfo = {};
                    that.studyId = '';
                    that.studyName = '';
                }else if (0 == val){
                    this.$nextTick(()=>{
                        this.$refs.addStudyTaskInfo.resetFields();
                    });
                    this.showTree = 'display:block'
                    this.showStudyLength = 'display:block'
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
                }else if (2 == val){
                    this.showTaskDesc = 'display:block'
                    this.showStudyLength = 'display:none'
                    this.showExercisesNumber = 'display:none'
                    this.showPassScore = 'display:none'
                }else if(3 == val){
                    let that = this;
                    if (null == that.allLabelIdList || [] == that.allLabelIdList || undefined == that.allLabelIdList || that.allLabelIdList.length == 0){
                        ajax.get('api/CollegeStudyTaskCfg/queryLabelTransferInfo', null, function(responseData){
                            that.allLabelIdList = responseData;
                        });
                    }
                    this.showTopic = 'display:block'
                    this.showNoTopic = 'display:none'
                }
            },
            handleClick() {

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
                this.$confirm('是否删除选中的学习任务?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let that = this
                    ajax.post('api/CollegeStudyTaskCfg/deleteStudyTaskBatch', val, function(responseData){
                        that.multipleSelection.forEach((deleteStufyInfo) => {
                            let studyTaskInfoList = that.studyTaskInfo;
                            for(let i = 0 ; i < studyTaskInfoList.length ; i++){
                                if(studyTaskInfoList[i].studyTaskId == deleteStufyInfo.studyTaskId){
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
                this.$confirm('是否删除' + deleteStudyName + '学习任务配置?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let that = this
                    ajax.post('api/CollegeStudyTaskCfg/deleteStudyTaskByRow', row, function(responseData){
                        let studyTaskInfoList = that.studyTaskInfo;
                        for(let i = 0 ; i < studyTaskInfoList.length ; i++){
                            if(studyTaskInfoList[i].studyTaskId == row.studyTaskId){
                                that.studyTaskInfo.splice(i, 1);
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
                            if(studyTaskInfoList[i].studyTaskId == editStudyTaskInfo.studyTaskId){
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
            changeJobType: function (val) {
                this.showJob = 'display:none'
                this.showEmployee = 'display:none'
                if(val == 0 || val == 1){
                    this.showJob = 'display:block'
                }else if(val == 2){
                    this.showJob = 'display:none'
                }else if (val == 3){
                    let that = this
                    if (null == that.employeeInfos || [] == that.employeeInfos || undefined == that.employeeInfos || that.employeeInfos.length == 0){
                        ajax.get('api/CollegeStudyTaskCfg/queryEmployeeTransferInfo', null, function(responseData){
                            that.employeeInfos = responseData;
                        });
                    }
                    this.showEmployee = 'display:block'
                }
            },
            filterJobRoleMethod: function (query, item) {
                return item.label.indexOf(query) > -1;
            },
            toTaskDetail: function (studyTaskId) {
                util.openPage('openUrl?url=modules/college/task/study_task_detail&studyTaskId='+studyTaskId, '任务详情');
            },
            changeStudyStartType: function(val){
                if (3 == val){
                    this.showStudyModel = 'display:block';
                    this.showAppointDay = 'display:none'
                }else if (2 == val){
                    this.showStudyModel = 'display:none';
                    this.showAppointDay = 'display:block'
                }
                else {
                    this.showStudyModel = 'display:none';
                    this.showTogetherStudyTask = 'display:none';
                    this.showAppointDay = 'display:none'
                }
            },
            changeStudyModel: function (val) {
                if (1 == val){
                    this.showTogetherStudyTask = 'display:block';
                    let that = this;
                    ajax.get('api/CollegeStudyTaskCfg/queryEffectiveTogetherStudyTaskList', null, function(responseData){
                        if (null == responseData || undefined == responseData || responseData.length == 0){
                            that.$message({
                                showClose: true,
                                duration: 3000,
                                message: '没有可以选择同时学习的任务，请选择其他开始模式，或者先添加其他任务配置',
                                center: true
                            });
                            that.changeStudyModel(0);
                            that.addStudyTaskInfo.studyModel = ''
                        }else {
                            that.togetherStudyTaskList = responseData;
                        }
                    });
                }else {
                    this.showTogetherStudyTask = 'display:none';
                }
            },
            releaseTaskExamBatch: function (examType) {
                let val = this.multipleSelection
                if(val == undefined || val == 'undefined' || val.length <= 0){
                    this.$message({
                        showClose: true,
                        duration: 3000,
                        message: '您未选择需要设置习题目标的任务配置！请选择后再点击设置。',
                        center: true
                    });
                    return;
                }
                this.$confirm('您选中批量设置练习和考试的任务中如果有实践任务，实践任务不会设置练习和考试，是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.studyTopicTypeInfo = {};
                    //批量设置
                    this.studyTopicTypeInfo.releaseType = '0';
                    this.releaseExamDialogVisible = true;
                    this.examType = examType;
                    this.showExercises = 'display:none'
                    this.showExam = 'display:none'
                    if (examType == "0"){
                        this.showExercises = 'display:block'
                        this.showExam = 'display:none'
                        this.examMaxLabel = '最多练习次数'
                    }else if (examType == "1"){
                        this.showExercises = 'display:none'
                        this.showExam = 'display:block'
                        this.examMaxLabel = '最多考试次数'
                    }
                })
            },
            submitReleaseExam: function(studyTopicTypeInfo){
                this.$refs.studyTopicTypeInfo.validate((valid) => {
                    if (valid) {
                        let releaseType = studyTopicTypeInfo.releaseType;
                        let studyTopicTypeInfoList = studyTopicTypeInfo.studyTopicTypeInfoDetails;
                        if (null == studyTopicTypeInfoList || undefined == studyTopicTypeInfoList || studyTopicTypeInfoList == [] || studyTopicTypeInfoList.length <= 0){
                            this.$message({
                                showClose: true,
                                duration: 3000,
                                message: '请设置习题类型和数量。',
                                center: true
                            });
                            return;
                        }
                        let topicTypes = ',';
                        let allScore = 0;
                        for (let i = 0 ; i < studyTopicTypeInfoList.length; i++){
                            let studyTopicTypeInfo = studyTopicTypeInfoList[i];
                            let type = "";
                            this.studyTopicTypeOptions.forEach((option) => {
                                if (option.value == studyTopicTypeInfo.exercisesType){
                                    type = option.label;
                                }
                            });
                            if (undefined == studyTopicTypeInfo.exercisesNumber || null == studyTopicTypeInfo.exercisesNumber || studyTopicTypeInfo.exercisesNumber == 0 || studyTopicTypeInfo.exercisesNumber == '0'){
                                this.$message({
                                    showClose: true,
                                    duration: 3000,
                                    message: '请设置' + type + '习题的数量，且不能为0',
                                    center: true
                                });
                                return;
                            }
                            if (topicTypes.indexOf("," + studyTopicTypeInfo.exercisesType + ",") > -1){
                                this.$message({
                                    showClose: true,
                                    duration: 3000,
                                    message: '习题类型' + type + '不能重复设置',
                                    center: true
                                });
                                return;
                            }
                            topicTypes = topicTypes + studyTopicTypeInfo.exercisesType + ",";
                        }
                        let requestInfo = studyTopicTypeInfo;
                        let val = this.multipleSelection
                        if(val == undefined || val == 'undefined' || val.length <= 0){
                            this.$message({
                                showClose: true,
                                duration: 3000,
                                message: '您未选择需要设置习题目标的任务配置！请选择后再点击设置。',
                                center: true
                            });
                            return;
                        }
                        let taskInfoList = [];
                        val.forEach((studyInfo) => {
                            let taskInfo = {};
                            taskInfo.studyTaskId = studyInfo.studyTaskId;
                            taskInfoList.push(taskInfo);
                        })
                        requestInfo.taskInfoList = taskInfoList;
                        let that = this;
                        requestInfo.examType = that.examType
                        ajax.post('api/CollegeExamCfg/releaseTaskExam', requestInfo, function(responseData){
                            that.$message({
                                showClose: true,
                                message: '课程任务考试设置成功',
                                type: 'success'
                            });
                            that.releaseExamDialogVisible = false;
                            that.studyTopicTypeInfo = {}
                            that.studyTopicTypeInfoDetails = []
                            that.examType = ''
                        });
                    }
                });
            },

            addTopicType: function () {
                this.topicInfoItem = {
                    exercisesNumber: 0,
                    exercisesType: "1"
                };
                let that = this;
                that.$refs.studyTopicTypeInfoDetails.insertAt(that.topicInfoItem, 0);
                that.studyTopicTypeInfoDetails.push(that.topicInfoItem)
                that.studyTopicTypeInfo.studyTopicTypeInfoDetails = that.studyTopicTypeInfoDetails
            },
            deleteSelectedTopicType: function () {
                let data = this.$refs.studyTopicTypeInfoDetails.getCheckboxRecords();
                if (data == null || data.length <= 0) {
                    this.$message.error('没有选中任何记录，无法删除');
                    return;
                }
                data.forEach((studyTopicTypeInfo) => {
                    let studyTopicTypeInfos = this.studyTopicTypeInfoDetails;
                    for(let i = 0 ; i < studyTopicTypeInfos.length ; i++){
                        if(studyTopicTypeInfos[i]._XID == studyTopicTypeInfo._XID){
                            this.studyTopicTypeInfoDetails.splice(i, 1);
                            break;
                        }
                    }
                })
            },
            deleteTopicType: function (row) {
                let studyTopicTypeInfos = this.studyTopicTypeInfoDetails;
                for(let i = 0 ; i < studyTopicTypeInfos.length ; i++){
                    if(studyTopicTypeInfos[i]._XID == row._XID){
                        this.studyTopicTypeInfoDetails.splice(i, 1);
                        break;
                    }
                }
            },
            releaseTask: function () {
                let val = this.multipleSelection
                if(val == undefined || val == 'undefined' || val.length <= 0){
                    this.$message({
                        showClose: true,
                        duration: 3000,
                        message: '您未选择发布的任务！请选择后再点击设置。',
                        center: true
                    });
                    return;
                }
                let requestInfo = [];
                val.forEach((studyInfo) => {
                    requestInfo.push(studyInfo.studyTaskId);
                })
                let that = this;
                ajax.post('api/CollegeEmployeeTask/taskReleaseByTaskList', requestInfo, function(responseData){
                    that.$message({
                        showClose: true,
                        message: '任务发布成功',
                        type: 'success'
                    });
                    that.showExercises = 'display:none';
                    that.showExam = 'display:none';
                    that.showStudyModel = 'display:none';
                    that.showTogetherStudyTask = 'display:none';
                    that.showAppointDay = 'display:none';
                    this.showTaskValidityTerm = 'display:none';
                    that.query();
                });
            },
            changeTaskType: function (val) {
                if (2 == val){
                    this.showTaskValidityTerm = 'display:block';
                }else {
                    this.showTaskValidityTerm = 'display:none';
                    this.showAppointDay = 'display:none';
                    this.showStudyModel = 'display:none';
                    this.showTogetherStudyTask = 'display:none';
                }
            },
            initAddStudyTaskDialogVisible: function () {
                this.changeTaskCoursewareType();
                this.taskCoursewareType = '';
                this.showTaskValidityTerm = 'display:none';
                this.showAppointDay = 'display:none';
                this.showStudyModel = 'display:none';
                this.showTogetherStudyTask = 'display:none';
                this.showStudyLength = 'display:none'
                this.showTaskDesc = 'display:none'
                this.showTree = 'display:none'
                this.showUpload = 'display:none'
                this.showExercisesNumber = 'display:none'
                this.showPassScore = 'display:none'
                this.showJob = 'display:none'
                this.showEmployee = 'display:none'
            },
            filterLabelMethod: function (query, item) {
                return item.label.indexOf(query) > -1;
            }
        }

        /*mounted () {
            this.init();
        }*/
    });

    return vm;
});