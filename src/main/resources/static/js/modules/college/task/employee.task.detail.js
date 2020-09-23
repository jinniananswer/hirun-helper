require(['vue','ELEMENT','ajax', 'vxe-table', 'vueselect', 'org-orgtree','house-select', 'util', 'cust-info', 'order-info', 'order-worker', 'order-search-employee', 'order-file-upload', 'upload-file'], function(Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util, custInfo, orderInfo, orderWorker, orderSearchEmployee, orderFileUpload) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                queryCond: {
                    employeeId: '',
                    limit: 20,
                    page: 1,
                    count: null
                },
                employeeId: util.getRequest("employeeId"),
                employee: {},
                subActiveTab: 'baseInfo',
                taskDetailInfo: {},
                employeeTaskInfo: [],
                evaluateTaskDialogVisible: false,
                colors: ['#99A9BF', '#F7BA2A', '#FF9900'],
                taskScore: null,
                exercisesScore: null,
                examScore: null,
                studyScore: null,
                taskInfo: {},
                argTaskScore: null
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
            initEmployeeTaskDetailInfo: function(){
                let that = this;
                let employeeId=that.employeeId;
                if(employeeId=='undefined'){
                    employeeId=null;
                }
                that.queryCond.employeeId = employeeId;
                ajax.get('api/CollegeEmployeeTask/queryEmployeeTaskDetailByPage', this.queryCond, function(responseData){
                    that.employeeTaskInfo = responseData.records;
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
                ajax.get('api/CollegeEmployeeTaskScore/isEvaluateTaskByTaskId', {taskId:taskId}, function(responseData){
                    let isEvaluateTask = responseData.evaluateTask
                    if (!isEvaluateTask){
                        that.$message({
                            showClose: true,
                            duration: 3000,
                            message: responseData.scoreNotReasons,
                            center: true
                        });
                        return;
                    }else {
                        that.evaluateTaskDialogVisible = true;
                        that.taskInfo = row;
                    }
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
                if (null == this.studyScore || undefined == this.studyScore || 0 == this.studyScore){
                    this.$message({
                        showClose: true,
                        duration: 3000,
                        message: '请对该任务学习情况进行评分！最低一颗星，最高五颗星',
                        center: true
                    });
                    return;
                }
                if (null == this.examScore || undefined == this.examScore || 0 == this.examScore){
                    this.$message({
                        showClose: true,
                        duration: 3000,
                        message: '请对该任务考试情况进行评分！最低一颗星，最高五颗星',
                        center: true
                    });
                    return;
                }
                if (null == this.exercisesScore || undefined == this.exercisesScore || 0 == this.exercisesScore){
                    this.$message({
                        showClose: true,
                        duration: 3000,
                        message: '请对该任务练习情况进行评分！最低一颗星，最高五颗星',
                        center: true
                    });
                    return;
                }
                let scoreInfo = {}
                scoreInfo.taskId = this.taskInfo.taskId;
                scoreInfo.taskScore = this.taskScore;
                scoreInfo.studyScore = this.studyScore;
                scoreInfo.exercisesScore = this.exercisesScore;
                scoreInfo.examScore = this.examScore;
                let that = this
                ajax.post('api/CollegeEmployeeTaskScore/evaluateTask', scoreInfo, function(responseData){
                    that.$message({
                        message: '员工任务评分成功',
                        type: 'success'
                    });
                    that.taskScore = null;
                    that.studyScore = null;
                    that.exercisesScore = null;
                    that.examScore = null;
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
            this.initEmployeeTaskDetailInfo();
        },
    });

    return vm;
});