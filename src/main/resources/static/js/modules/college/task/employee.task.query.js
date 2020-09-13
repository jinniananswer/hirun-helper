require(['vue','ELEMENT','ajax', 'vxe-table', 'vueselect', 'org-orgtree','house-select', 'util', 'cust-info', 'order-info', 'order-worker', 'order-search-employee', 'order-file-upload', 'upload-file'], function(Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util, custInfo, orderInfo, orderWorker, orderSearchEmployee, orderFileUpload) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                queryCond: {
                    employeeName: '',
                    employeeId: '',
                    limit: 20,
                    page: 1,
                    count: null
                },
                employeeTaskInfo: [],
                exercisesType: '',
                collegeStudyExamList: [],
                fixedExamDialogVisible: false,
                collegeStudyExamInfo: {}
            }
        },
        methods: {
            query: function() {
                let that = this;
                ajax.get('api/CollegeEmployeeTask/queryEmployeeTask', this.queryCond, function(responseData){
                    that.employeeTaskInfo = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;
                });
            },
            toEmployeeTaskDetail: function (employeeId) {
                util.openPage('openUrl?url=modules/college/task/employee_task_detail&employeeId='+employeeId, '员工任务详情');
            }
        }
    });

    return vm;
});