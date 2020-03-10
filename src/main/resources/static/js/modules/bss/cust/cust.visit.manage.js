require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info','cust-visit', 'order-worker', 'order-selectemployee'], function (Vue, element, axios, ajax, vueselect, util,custInfo,custVisit,orderWorker, orderSelectEmployee) {
    let vm = new Vue({
        el: '#customer_visit',
        data: function () {
            return {
                customerVisit: {
                    visitObject: '',
                    //visitEmployeeId: '',
                    visitType: '',
                    visitTime:util.getNowTime(),
                    visitWay:'',
                    visitContent:'',
                    custId: util.getRequest('custId'),
                },
                display: 'display:block',
                rules: {
                    visitObject: [
                        {required: true, message: '请填写回访对象', trigger: 'blur'},
                    ],
                    visitEmployeeId: [
                        {required: true, message: '请选择回访人', trigger: 'change'}
                    ],
                    visitType: [
                        {required: true, message: '请回访类型', trigger: 'change'}
                    ],
                    visitTime: [
                        {required: true, message: '请选择回访时间', trigger: 'blur'}
                    ],
                    visitContent: [
                        {required: true, message: '请填写回访内容', trigger: 'blur'}
                    ],
                }
            }
        },

        methods: {
            submit(customerVisit) {
                this.$refs.customerVisit.validate((valid) => {
                    if (valid) {
                        ajax.post('api/customer/party-visit/addCustomerVisit',this.customerVisit,null,null,true);
                    }
                })
            },
        }
    });
    return vm;
})