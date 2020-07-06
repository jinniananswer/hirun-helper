require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util', 'cust-info', 'order-info', 'order-worker', 'order-selectemployee', 'cust-visit'], function (Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee, custVisit) {
    let vm = new Vue({
        el: '#app',
        data: function () {
            return {
                customerConsult: {
                    custServiceEmployeeId: '',
                    designCupboardEmployeeId: '',
                    mainMaterialKeeperEmployeeId: '',
                    designEmployeeId: '',
                    cupboardKeeperEmployeeId: '',
                    consultRemark: '',
                    consultTime: '',
                    orderId: '',
                    id: '',
                    custId: '',
                    custName: '',
                },
                progress: [-10, 70],
                activeTab: 'orderInfo',
                custInfo: '',
                dialogTableVisible: false,
                rules: {
                    designCupboardEmployeeId: [
                        {required: true, message: '请选择橱柜设计师', trigger: 'change'}
                    ],
                    consultTime: [
                        {required: true, message: '请选择咨询时间', trigger: 'blur'}
                    ],
                    designEmployeeId: [
                        {required: true, message: '请选择设计师', trigger: 'change'}
                    ],
                    designCupboardEmployeeId: [
                        {required: true, message: '请选择橱柜设计师', trigger: 'change'}
                    ],
                    mainMaterialKeeperEmployeeId: [
                        {required: true, message: '请选择主材管家', trigger: 'change'}
                    ],
                    cupboardKeeperEmployeeId: [
                        {required: true, message: '请选择橱柜管家', trigger: 'change'}
                    ],
                },
                avatarUrl: 'static/img/male.jpg'
            }
        },

        methods: {

            init: function () {
                ajax.get('api/customer/cust-base/queryCustomer4TransOrder', null, function (responseDate) {
                    vm.custInfo = responseDate;
                });
            },

            selectCustomer: function () {
                this.dialogTableVisible = true;
            },

            selectedCustomer: function (row, column, cell, event) {
                console.log(row);
                let that = this;
                ajax.get('api/order/order-consult/queryOrderConsultForTrans', {orderId: row.orderId}, function (data) {
                    Object.assign(that.customerConsult, data);
                })
                this.dialogTableVisible = false;
            },

            save(customerConsult) {
                if (this.customerConsult.orderId == '') {
                    this.$message.error('请选择一位客户');
                    return;
                }
                this.$refs.customerConsult.validate((valid) => {
                    if (valid) {
                        ajax.post('api/order/order-consult/saveCustomerConsultInfo', this.customerConsult, null, null, true);
                    }
                })
            },

            submitSneak(customerConsult) {
                this.$confirm('执行操作【跑单】, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    ajax.post('api/order/order-consult/submitSneak', this.customerConsult);
                })
            },

            submitTransOrder(customerConsult) {
                this.$refs.customerConsult.validate((valid) => {
                    if (valid) {
                        this.$confirm('执行操作【转家装订单】, 是否继续?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            ajax.post('api/order/order-consult/transOrder', this.customerConsult);
                        })
                    }
                })
            },

            handle(row) {
                this.customerConsult.orderId = row.orderId;
                this.customerConsult.custId = row.custId;
                this.dialogTableVisible = false;
                ajax.get('api/order/order-consult/queryOrderConsult', {orderId: this.customerConsult.orderId}, function (data) {
                    Object.assign(that.customerConsult, data);
                })

            },

            submitMeasure(customerConsult) {
                this.$refs.customerConsult.validate((valid) => {
                    if (valid) {
                        this.$confirm('执行操作【提交量房】, 是否继续?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            ajax.post('api/order/order-consult/submitMeasure', this.customerConsult);
                        })
                    }
                })
            },
        },

        mounted() {
            this.init();
        }
    });

    return vm;
})