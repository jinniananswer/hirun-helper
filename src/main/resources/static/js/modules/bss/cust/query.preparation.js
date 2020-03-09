require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util', 'order-selectemployee', 'vue-router','house-select'], function (Vue, element, axios, ajax, vueselect, util, orderSelectEmployee, vueRouter,houseSelect) {
    let vm = new Vue({
        el: '#app',
        data: function () {
            return {
                custQueryCond: {
                    custName: '',
                    customerType: '',
                    reportEmployeeId: '',
                    startTime: util.getNowDate(),
                    endTime: util.getNowDate(),
                    houseMode: '',
                    prepareStatus:'',
                    houseId:'',
                    page:1,
                    size:10,
                    total:0
                },

                custId: '',
                customerInfo: [],
                checked: null,
                display: 'display:block',
                mobileNo:'',
            }
        },

        methods: {
            queryCustomer: function () {
                let that = this;
                ajax.get('api/customer/cust-preparation/queryPreparationInfo', this.custQueryCond, function (responseData) {
                    vm.customerInfo = responseData.records;
                    that.custQueryCond.page = responseData.current;
                    that.custQueryCond.total = responseData.total;
                });
            },


            getTemplateRow(index, row) {
                this.templateSelection = row;
                this.custId = row.custId;
                this.mobileNo=row.mobileNo;
            },

            customerVisit(custId) {
                util.openPage('openUrl?url=modules/bss/cust/cust_visit_manage&custId=' + custId, '客户回访');
            },

            customerRuling(custId,mobileId){
                util.openPage('openUrl?url=modules/bss/cust/cust_ruling&custId=' + custId+'&mobileNo='+mobileId, '报备裁定');
            },

            handleSizeChange: function (size) {
                this.custQueryCond.size = size;
                this.custQueryCond.page = 1;
                this.queryCustomer();
            },

            handleCurrentChange: function(currentPage){
                this.custQueryCond.page = currentPage;
                this.queryCustomer();
            },

            toOrderDetail(orderId, custId) {
                util.openPage('openUrl?url=modules/bss/order/cust_order_detail&orderId='+orderId+'&custId='+custId, '订单详情');
            }
        }
    });

    return vm;
})