require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util', 'order-selectemployee', 'vue-router','house-select'], function (Vue, element, axios, ajax, vueselect, util, orderSelectEmployee, vueRouter,houseSelect) {
    let vm = new Vue({
        el: '#app',
        data: function () {
            return {
                custQueryCond: {
                    custName: '',
                    designEmployeeId: '',
                    counselorEmployeeId: '',
                    informationSource: '',
                    customerType: '',
                    reportEmployeeId: '',
                    timeType: '',
                    startTime: util.getNowDate(),
                    endTime: util.getNowDate(),
                    houseMode: '',
                    orderStatus:'',
                    houseId:''
                },
                pageConf: {
                    //设置一些初始值(会被覆盖)
                    pageCode: 1, //当前页
                    pageSize: 4, //每页显示的记录数
                    totalPage: 12, //总记录数
                    pageOption: [4, 10, 20], //分页选项
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
                this.mobileNo=row.mobileNo;
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
            },

            customerRuling(){
                if (this.custId == '') {
                    this.$message({
                        showClose: true,
                        message: '请选择一条客户数据再操作',
                        type: 'error'
                    });
                    return;
                }
                util.openPage('openUrl?url=modules/bss/cust/cust_ruling&custId=' + this.custId+'&mobileNo='+this.mobileNo, '报备裁定');
            },

        }
    });

    return vm;
})