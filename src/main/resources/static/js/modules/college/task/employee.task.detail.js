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
                employeeTaskInfo: []
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
            customColorMethod(percentage) {
                if (percentage < 30) {
                    return '#909399';
                } else if (percentage < 70) {
                    return '#e6a23c';
                } else {
                    return '#67c23a';
                }
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