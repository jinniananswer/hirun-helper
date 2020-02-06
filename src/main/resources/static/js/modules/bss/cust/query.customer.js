require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util', 'order-selectemployee', 'vue-router'], function (Vue, element, axios, ajax, vueselect, util, orderSelectEmployee, vueRouter) {
    let vm = new Vue({
        el: '#app',
        data: function () {
            return {
                custQueryCond: {
                    custName: '',
                    designEmployeeId: '',
                    counselorEmployeeId: '',
                    informationSource: '',
                    customerStatus: '',
                    customerType: '',
                    reportEmployeeId: '',
                    timeType: '',
                    startTime: '',
                    endTime: '',
                    houseMode: ''
                },
                custId: '',
                customerInfo: [],
                checked: null,
                display: 'display:block',

                id: util.getRequest('id'),

            }
        },

        methods: {
            queryCustomer: function () {
                ajax.get('api/customer/cust-base/queryCustomerInfo', this.custQueryCond, function (responseData) {
                    vm.customerInfo = responseData;
                });
            },

            handleClick(row) {
                console.log(row);
            },

            getTemplateRow(index, row) {
                this.templateSelection = row;
                this.custId = row.custId;
            },

            customerVisit() {
                if (this.custId == '') {
                    this.$message({
                        showClose: true,
                        message: '请选择一条客户数据再操作',
                        type: 'error'
                    });
                    return;
                }
                util.openPage('openUrl?url=modules/bss/cust/cust_visit_manage&custId=' + this.custId, '客户回访');
            }

        }
    });

    return vm;
})