require(['vue','ELEMENT','ajax', 'vxe-table', 'vueselect', 'org-orgtree','house-select', 'util', 'cust-info', 'order-info', 'order-worker', 'order-search-employee'], function(Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util, custInfo, orderInfo, orderWorker, orderSearchEmployee) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                employeeId: util.getRequest('employeeId'),
                queryCond: {
                    name: '',
                    sex: '',
                    limit: 20,
                    page: 1,
                    count: null
                },
                employeeInfo:[]
            }
        },

        methods: {
            query: function() {
                let that = this;
                ajax.get('api/organization/employee/queryNewEmployeeByPage', this.queryCond, function(responseData){
                    that.employeeInfo = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;
                });
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            fixedTaskRelease: function(){
                
            }
        },

        /*mounted () {
            this.init();
        }*/
    });

    return vm;
});