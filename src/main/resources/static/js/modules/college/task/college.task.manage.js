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
                courseTaskInfo: [],
                courseTaskTypes: [],
                addCourseTaskDialogVisible: false,
                courseChaptersInfos: [],
                addCourseTaskInfo: {}
            }
        },
        mounted: function() {
            this.courseTaskTypes = [
                {value : "1", name : "固定任务"},
                {value : "2", name : "活动任务"}
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
            handleSelectionChange(val) {
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
                this.addCourseTaskDialogVisible = true;
            },
            getRowClass(row, index) {
                let res = []
                if (!row.children)//即改行没有子元素时，添加row-expand-cover类
                    return "visibility:hidden";
            }
        },

        /*mounted () {
            this.init();
        }*/
    });

    return vm;
});