require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info'], function (Vue, element, axios, ajax, vueselect, util,custInfo) {
    let vm = new Vue({
        el: '#customer_ruling',
        data: function () {
            return {
                employeeOptions: [],
                customerRuling: {
                    prepareOrgId: '',
                    prepareEmployeeId: '',
                    enterEmployeeId: '',
                    mobileNo: util.getRequest('mobileNo'),
                    rulingRemark: '',
                    rulingEmployeeId: '',
                    custId: util.getRequest('custId'),
                },
                preparationFailRecord: [],
                display: 'display:block',
                rules: {
                    rulingEmployeeId: [
                        {required: true, message: '请填写裁定主管', trigger: 'blur'}
                    ],
                    prepareOrgId: [
                        {required: true, message: '请填写部门', trigger: 'blur'}
                    ],
                    prepareEmployeeId: [
                        {required: true, message: '请选择申报人', trigger: 'change'}
                    ],
                }
            }
        },

        methods: {
            loadEmployee: function () {
                axios.get('api/organization/employee/loadEmployee').then(function (responseData) {
                    vm.employeeOptions = responseData.data.rows;
                }).catch(function (error) {
                    console.log(error);
                });
            },

            queryFailPreparation: function () {
                axios.get('api/customer/cust-preparation/queryFailPreparation?mobileNo=' + this.customerRuling.mobileNo+'&custId='+this.customerRuling.custId)
                    .then(function (responseData) {
                    vm.preparationFailRecord = responseData.data.rows;
                }).catch(function (error) {
                    console.log(error);
                });
            },

            submit(customerRuling) {
                this.$refs.customerRuling.validate((valid) => {
                    if (valid) {
                        ajax.post('api/customer/cust-preparation/customerRuling',this.customerRuling);
                    }
                })
            },

        }
    });
    vm.loadEmployee();
    vm.queryFailPreparation();
    return vm;
})