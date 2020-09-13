require(['vue','ELEMENT','ajax', 'vxe-table', 'vueselect', 'org-orgtree','house-select', 'util', 'cust-info', 'order-info', 'order-worker', 'order-search-employee', 'order-file-upload', 'upload-file'], function(Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util, custInfo, orderInfo, orderWorker, orderSearchEmployee, orderFileUpload) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
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
                collegeStudyExamRules: {
                    examId: [
                        { required: true, message: '请先选择习题范围', trigger: 'change' }
                    ],
                    exercisesType: [
                        { required: true, message: '请先选择习题类型', trigger: 'blur' }
                    ],
                    exercisesNumber: [
                        { required: true, message: '习题数量不能为空', trigger: 'blur' },
                        { type: 'number', message: '习题数量必须为数字值', trigger: 'blur'}
                    ]
                },
                studyTaskInfo: [],
                exercisesType: '',
                collegeStudyExamList: [],
                fixedExamDialogVisible: false,
                collegeStudyExamInfo: {}
            }
        },
        methods: {
            query: function() {
                let that = this;
                ajax.get('api/CollegeStudyTaskCfg/queryCollegeStudyExamByPage', this.queryCond, function(responseData){
                    that.studyTaskInfo = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;
                });
            },
            handleExamSelectionChange(val) {
                this.multipleSelection = val;
            },
            fixedExamBatch: function () {
                let val = this.multipleSelection
                if(val == undefined || val == 'undefined' || val.length <= 0){
                    this.$message({
                        showClose: true,
                        duration: 3000,
                        message: '您未选择需要设置考试目标的任务配置！请选择后再点击设置。',
                        center: true
                    });
                    return;
                }
                this.collegeStudyExamInfo = {};
                //批量设置
                this.collegeStudyExamInfo.fixedType = '0';
                this.fixedExamDialogVisible = true;
            },
            fixedExam: function (row) {
                this.collegeStudyExamInfo = {};
                //单条设置
                this.collegeStudyExamInfo.fixedType = '1';
                let studyChaptersList = [];
                let studyChapters = {};
                studyChapters.studyId = row.studyId;
                studyChapters.chaptersId = row.chaptersId;
                studyChaptersList.push(studyChapters);
                this.collegeStudyExamInfo.studyChaptersList = studyChaptersList
                this.fixedExamDialogVisible = true;
            },
            cancel: function () {
                this.fixedExamDialogVisible = false;
            },
            submitFixedExercises: function(collegeStudyExamInfo){
                this.$refs.collegeStudyExamInfo.validate((valid) => {
                    if (valid) {
                        let fixedType = collegeStudyExamInfo.fixedType;
                        let requestInfo = collegeStudyExamInfo;
                        if (fixedType == '0'){
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
                            let studyChaptersList = [];
                            val.forEach((studyInfo) => {
                                let studyChapters = {};
                                studyChapters.studyId = studyInfo.studyId;
                                studyChapters.chaptersId = studyInfo.chaptersId;
                                studyChaptersList.push(studyChapters);
                            })
                            requestInfo.studyChaptersList = studyChaptersList;
                        }
                        let that = this;
                        ajax.post('api/CollegeStudyExamCfg/fixedStudyExam', requestInfo, function(responseData){
                            that.$message({
                                showClose: true,
                                message: '课程任务考试题目设置成功',
                                type: 'success'
                            });
                            let addStudyChaptersList = requestInfo.studyChaptersList
                            addStudyChaptersList.forEach((addStudyChapters)=>{
                                that.studyTaskInfo.forEach((studyTask)=>{
                                    if (studyTask.studyId == addStudyChapters.studyId && studyTask.chaptersId == addStudyChapters.chaptersId){
                                        let collegeStudyExam = {};
                                        collegeStudyExam.studyId = studyTask.studyId
                                        collegeStudyExam.chaptersId = studyTask.chaptersId
                                        collegeStudyExam.examId = requestInfo.examId
                                        collegeStudyExam.exercisesType = requestInfo.exercisesType
                                        collegeStudyExam.exercisesNumber = requestInfo.exercisesNumber
                                        collegeStudyExam.exercisesTypeName = responseData.exercisesTypeName
                                        collegeStudyExam.examName = responseData.examName
                                        if(studyTask.collegeStudyExamList == undefined || studyTask.collegeStudyExamList == "undefined"){
                                            studyTask.collegeStudyExamList = [];
                                        }
                                        studyTask.collegeStudyExamList.push(collegeStudyExam);
                                    }
                                })
                            })

                            that.fixedExamDialogVisible = false;
                        });
                    }
                });
            }
        }
    });

    return vm;
});