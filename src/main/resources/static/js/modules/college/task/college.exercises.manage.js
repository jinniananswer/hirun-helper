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
                collegeStudyExercisesRules: {
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
                collegeStudyExercisesList: [],
                fixedExercisesDialogVisible: false,
                collegeStudyExercisesInfo: {}
            }
        },
        methods: {
            query: function() {
                let that = this;
                ajax.get('api/CollegeStudyTaskCfg/queryCollegeStudyExercisesByPage', this.queryCond, function(responseData){
                    that.studyTaskInfo = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;
                });
            },
            handleExercisesSelectionChange(val) {
                this.multipleSelection = val;
            },
            fixedExercisesBatch: function () {
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
                this.collegeStudyExercisesInfo = {};
                //批量设置
                this.collegeStudyExercisesInfo.fixedType = '0';
                this.fixedExercisesDialogVisible = true;
            },
            fixedExercises: function (row) {
                this.collegeStudyExercisesInfo = {};
                //单条设置
                this.collegeStudyExercisesInfo.fixedType = '1';
                let studyChaptersList = [];
                let studyChapters = {};
                studyChapters.studyId = row.studyId;
                studyChapters.chaptersId = row.chaptersId;
                studyChaptersList.push(studyChapters);
                this.collegeStudyExercisesInfo.studyChaptersList = studyChaptersList
                this.fixedExercisesDialogVisible = true;
            },
            cancel: function () {
                this.fixedExercisesDialogVisible = false;
            },
            submitFixedExercises: function(collegeStudyExercisesInfo){
                this.$refs.collegeStudyExercisesInfo.validate((valid) => {
                    if (valid) {
                        let fixedType = collegeStudyExercisesInfo.fixedType;
                        let requestInfo = collegeStudyExercisesInfo;
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
                        ajax.post('api/CollegeStudyExercisesCfg/fixedStudyExercises', requestInfo, function(responseData){
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
                                        studyTask.collegeStudyExercisesList.push(collegeStudyExam);
                                    }
                                })
                            })
                            that.$message({
                                showClose: true,
                                message: '课程任务新增成功',
                                type: 'success'
                            });
                            that.fixedExercisesDialogVisible = false;
                        });
                    }
                });
            }
        }
    });

    return vm;
});