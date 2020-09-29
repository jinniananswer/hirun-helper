require(['vue','ELEMENT','ajax', 'vxe-table', 'vueselect', 'org-orgtree','house-select', 'util', 'cust-info', 'order-info', 'order-worker', 'order-search-employee', 'order-file-upload', 'upload-file'], function(Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util, custInfo, orderInfo, orderWorker, orderSearchEmployee, orderFileUpload) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                queryCourseTaskCond: {
                    employeeId: '',
                    studyType: '',
                    limit: 20,
                    page: 1,
                    count: null
                },
                queryCoursewareTaskCond: {
                    employeeId: '',
                    studyType: '',
                    limit: 20,
                    page: 1,
                    count: null
                },
                queryPracticeTaskCond: {
                    employeeId: '',
                    studyType: '',
                    limit: 20,
                    page: 1,
                    count: null
                },
                employeeId: util.getRequest("employeeId"),
                employee: {},
                subActiveTab: 'courseTaskInfo',
                subEmployeeActiveTab: 'baseInfo',
                subTaskEvaluateActiveTab: 'taskInfo',
                subTaskExperienceActiveTab: 'writtenInfo',
                taskDetailInfo: {},
                employeeCourseTaskInfo: [],
                employeeCoursewareTaskInfo: [],
                employeePracticeTaskInfo: [],
                evaluateTaskDialogVisible: false,
                colors: ['#99A9BF', '#F7BA2A', '#FF9900'],
                taskScore: null,
                exercisesScore: null,
                examScore: null,
                studyScore: null,
                experienceScore: null,
                imgScore: null,
                taskInfo: {},
                argTaskScore: null,
                writtenExperience: '',
                imgExperienceList: [],
                url: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
                urls: [
                    'https://fuss10.elemecdn.com/a/3f/3302e58f9a181d2509f3dc0fa68b0jpeg.jpeg',
                    'https://fuss10.elemecdn.com/1/34/19aa98b1fcb2781c4fba33d850549jpeg.jpeg',
                    'https://fuss10.elemecdn.com/0/6f/e35ff375812e6b0020b6b4e8f9583jpeg.jpeg',
                    'https://fuss10.elemecdn.com/9/bb/e27858e973f5d7d3904835f46abbdjpeg.jpeg',
                    'https://fuss10.elemecdn.com/d/e6/c4d93a3805b3ce3f323f7974e6f78jpeg.jpeg',
                    'https://fuss10.elemecdn.com/3/28/bbf893f792f03a54408b3b7a7ebf0jpeg.jpeg',
                    'https://fuss10.elemecdn.com/2/11/6535bcfb26e4c79b48ddde44f4b6fjpeg.jpeg'
                ]
            }
        },
        methods: {
            initEmployeeInfo: function() {
                let that = this;
                let employeeId=that.employeeId;
                if(employeeId=='undefined'){
                    employeeId=null;
                }
                ajax.get('api/CollegeEmployeeTask/getEmployeeByEmployeeId', {employeeId:employeeId}, function(responseData){
                    that.employee = responseData;
                });
            },
            initTaskInfo: function(){
                let that = this;
                let employeeId=that.employeeId;
                if(employeeId=='undefined'){
                    employeeId=null;
                }
                ajax.get('api/CollegeEmployeeTask/getEmployeeTaskDetailByEmployeeId', {employeeId:employeeId}, function(responseData){
                    that.taskDetailInfo = responseData;
                    that.argTaskScore = responseData.argTaskScore;
                });
            },
            queryEmployeeCourseTaskInfo: function(){
                let that = this;
                let employeeId=that.employeeId;
                if(employeeId=='undefined'){
                    employeeId=null;
                }
                that.queryCourseTaskCond.employeeId = employeeId;
                that.queryCourseTaskCond.studyType = '0';
                ajax.get('api/CollegeEmployeeTask/queryEmployeeTaskDetailByPage', that.queryCourseTaskCond, function(responseData){
                    that.employeeCourseTaskInfo = responseData.records;
                });
            },
            queryEmployeeCoursewareTaskInfo: function(){
                let that = this;
                let employeeId=that.employeeId;
                if(employeeId=='undefined'){
                    employeeId=null;
                }
                that.queryCoursewareTaskCond.employeeId = employeeId;
                that.queryCoursewareTaskCond.studyType = '1';
                ajax.get('api/CollegeEmployeeTask/queryEmployeeTaskDetailByPage', that.queryCoursewareTaskCond, function(responseData){
                    that.employeeCoursewareTaskInfo = responseData.records;
                });
            },
            queryEmployeePracticeTaskInfo: function(){
                let that = this;
                let employeeId=that.employeeId;
                if(employeeId=='undefined'){
                    employeeId=null;
                }
                that.queryPracticeTaskCond.employeeId = employeeId;
                that.queryPracticeTaskCond.studyType = '2';
                ajax.get('api/CollegeEmployeeTask/queryEmployeeTaskDetailByPage', that.queryPracticeTaskCond, function(responseData){
                    that.employeePracticeTaskInfo = responseData.records;
                });
            },
            customColorMethod: function(percentage) {
                if (percentage < 30) {
                    return '#909399';
                } else if (percentage < 70) {
                    return '#e6a23c';
                } else {
                    return '#67c23a';
                }
            },
            evaluateTask: function(row){
                let taskId = row.taskId;
                let that = this;
                ajax.get('api/CollegeTaskExperience/queryByTaskId', {taskId:taskId}, function(responseData){
                    that.writtenExperience = responseData.writtenExperience
                    that.imgExperienceList = [];
                    responseData.imgExperienceList.forEach((imgExperience) => {
                        that.imgExperienceList.push(imgExperience.fileUrl);
                    })
                    that.evaluateTaskDialogVisible = true;
                    that.taskInfo = row;
                    that.taskScore = responseData.taskScore;
                    that.experienceScore = responseData.experienceScore;
                    that.imgScore = responseData.imgScore;
                    that.queryEmployeePracticeTaskInfo();
                });
            },
            submitScore: function(){
                if (null == this.taskScore || undefined == this.taskScore || 0 == this.taskScore){
                    this.$message({
                        showClose: true,
                        duration: 3000,
                        message: '请对该任务进行评分！最低一颗星，最高五颗星',
                        center: true
                    });
                    return;
                }
                if (null == this.experienceScore || undefined == this.experienceScore || 0 == this.experienceScore){
                    this.$message({
                        showClose: true,
                        duration: 3000,
                        message: '请对该任务心得进行评分！最低一颗星，最高五颗星',
                        center: true
                    });
                    return;
                }
                if (null == this.imgScore || undefined == this.imgScore || 0 == this.imgScore){
                    this.$message({
                        showClose: true,
                        duration: 3000,
                        message: '请对该任务完成照片进行评分！最低一颗星，最高五颗星',
                        center: true
                    });
                    return;
                }
                let scoreInfo = {}
                scoreInfo.taskId = this.taskInfo.taskId;
                scoreInfo.taskScore = this.taskScore;
                scoreInfo.experienceScore = this.experienceScore;
                scoreInfo.imgScore = this.imgScore;
                let that = this
                ajax.post('api/CollegeEmployeeTaskScore/evaluateTask', scoreInfo, function(responseData){
                    that.$message({
                        message: '员工任务评分成功',
                        type: 'success'
                    });
                    that.taskScore = null;
                    that.experienceScore = null;
                    that.imgScore = null;
                    that.taskInfo = {};
                    that.evaluateTaskDialogVisible = false;
                },null, true);
            },
            cancel: function () {
                let that = this;
                that.evaluateTaskDialogVisible = false;
            },
        },
        mounted () {
            this.initEmployeeInfo();
            this.initTaskInfo();
            this.queryEmployeeCourseTaskInfo();
            this.queryEmployeeCoursewareTaskInfo();
            this.queryEmployeePracticeTaskInfo();
        },
    });

    return vm;
});