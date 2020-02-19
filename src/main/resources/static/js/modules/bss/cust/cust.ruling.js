require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info','order-search-employee','order-selectemployee'], function (Vue, element, axios, ajax, vueselect, util,custInfo,orderSearchEmployee,orderSelectEmployee) {
    let vm = new Vue({
        el: '#customer_ruling',
        data: function () {
            return {
                customerRuling: {
                    prepareOrgId: '',
                    prepareEmployeeId: '',
                    prepareEmployeeName:'',
                    enterEmployeeId: '',
                    mobileNo: util.getRequest('mobileNo'),
                    rulingRemark: '',
                    rulingEmployeeId: '',
                    custId: util.getRequest('custId'),
                    id:''
                },
                preparationFailRecord: [],
                display: 'display:block',
                enterDisabled:true,
                checked: null,
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
                    rulingRemark: [
                        {required: true, message: '请填写主管备注', trigger: 'blur'}
                    ],
                }
            }
        },

        methods: {

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

            getTemplateRow(index, row) {
                let that = this;

                that.customerRuling.id = row.id;
                that.customerRuling.prepareEmployeeId=row.prepareEmployeeId;
                that.customerRuling.prepareEmployeeName=row.prepareEmployeeName;
            },

        },

        mounted () {
            this.queryFailPreparation();
        }
    });

    return vm;
})