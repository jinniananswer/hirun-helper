require(['vue', 'ELEMENT', 'ajax','vxe-table', 'vueselect', 'util', 'order-selectemployee','house-select'], function (Vue, element, ajax,table,vueselect, util, orderSelectEmployee) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function () {
            return {
                queryCond: {
                    custName: '',
                    designEmployeeId: '',
                    checkStatus: '',
                    receiveStatus: '',
                    settleStatus: '',
                    timeType: '',
                    startTime: util.getNowDate(),
                    endTime: util.getNowDate(),
                    institution: '',
                    houseId:'',
                    agentEmployeeId:'',
                    page:1,
                    size:10,
                    total:0
                },
                orderInspectInfo: [],
                checked: null,
                display: 'display:block',
                mobileNo:'',
            }
        },

        methods: {
            queryOrderInspect: function () {
                let that = this;
                ajax.get('api/bss.order/order-inspect/queryOrderInspects', this.queryCond, function (responseData) {
                    vm.orderInspectInfo = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.total = responseData.total;
                });
            },


            handleSizeChange: function (size) {
                this.queryCond.size = size;
                this.queryCond.page = 1;
                this.queryOrderInspect();
            },

            handleCurrentChange: function(currentPage){
                this.queryCond.page = currentPage;
                this.queryOrderInspect();
            },

        }
    });

    return vm;
})