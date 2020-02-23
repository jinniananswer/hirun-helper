require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util', 'cust-info', 'order-info', 'order-worker', 'order-selectemployee', 'cust-visit'], function (Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee, custVisit) {
    let vm = new Vue({
        el: '#app',
        data: function () {
            return {
                customerConsult: {
                    custServiceEmployeeId: '',
                    designCupboardEmployeeId: '',
                    mainMaterialKeeperEmployeeId: '',
                    designEmployeeId:'',
                    cupboardKeeperEmployeeId: '',
                    consultRemark: '',
                    orderId:'2',
                    id:''
                },
                progress: [-10, 70],
                activeTab: 'orderInfo',

                rules: {
                    custServiceEmployeeId: [
                        {required: true, message: '请选择客户代表', trigger: 'change'}
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

                marks: {
                    0: '酝酿',
                    10: '初选',
                    30: '初步决策',
                    50: '决策',
                    60: '施工',
                    95: '维护'
                },

                requirement: {
                    title: '客户需求信息',
                    style: '白色简约',
                    func: '功能列表'
                },

                avatarUrl: 'static/img/male.jpg'
            }
        },

        methods: {
            init:function(){
                let that = this;
                ajax.get('api/order/order-consult/queryOrderConsult', {orderId:this.customerConsult.orderId}, function(data) {
                    Object.assign(that.customerConsult, data);
                })
            },

            save(customerConsult) {
                this.$refs.customerConsult.validate((valid) => {
                    if (valid) {
                        ajax.post('api/order/order-consult/saveCustomerConsultInfo', this.customerConsult);
                    }
                })
            },

            submitSneak(customerConsult) {
                this.$refs.customerConsult.validate((valid) => {
                    if (valid) {
                        this.$confirm('执行操作【咨询跑单】, 是否继续?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            ajax.post('api/order/order-consult/submitSneak', this.customerConsult);
                        })
                    }
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

        mounted () {
            this.init();
        }
    });

    return vm;
})