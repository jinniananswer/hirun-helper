require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-selectemployee','cust-visit', 'order-search-employee','order-discount-item','order-file-upload'], function(Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee,custVisit, orderSearchEmployee,orderDiscountItem,orderFileUpload) {
    let vm = new Vue({
        el: '#app',
        data() {
            return {
                lastInstallment: {
                    orderId:'',
                    chargedAllFee:'0',
                    chargedLastFee:'0',
                    chargedMaterialFee:'0',
                    chargedCupboardFee:'0',
                    woodProductFee:'',
                    baseDecorationFee:'',
                    doorFee:'0',
                    taxFee:'0',
                    furnitureFee:'0',
                    otherFee:'0',
                    financeEmployeeId:'',
                    financeEmployeeName:'',
                },
                workerSalary: {
                    orderId:'',
                    hydropowerSalary: '',
                    hydropowerRemark: '',
                    woodworkerSalary: '',
                    woodworkerRemark:'',
                    tilerSalary: '',
                    tilerRemark: '',
                    painterSalary:'',
                    painterRemark:'',
                    wallworkerSalary:'',
                    wallworkerRemark:'',
                },
                orderId : util.getRequest('orderId'),
                custId : util.getRequest('custId'),

                lastInstallmentRules : {
                    doorFee: [
                        {required: true, message: '请填写门总金额', trigger: 'blur'},
                    ],
                    furnitureFee: [
                        {required: true, message: '请填写家具总金额', trigger: 'blur'},
                    ],
                    financeEmployeeId : [
                        {required: true, message: '请选择财务人员', trigger: 'change'},
                    ]
                },
            }
        },
        computed: {
            getWoodProductFee: {
                get() {
                    let woodProductFee = (parseFloat(this.lastInstallment.doorFee) + parseFloat(this.lastInstallment.furnitureFee)).toFixed(2)
                    this.lastInstallment.woodProductFee = woodProductFee;
                    return woodProductFee;
                }
            },
            getBaseDecorationFee : {
                get() {
                    let baseDecorationFee = (parseFloat(this.lastInstallment.chargedAllFee)-parseFloat(this.lastInstallment.woodProductFee)
                        -parseFloat(this.lastInstallment.taxFee)-parseFloat(this.lastInstallment.otherFee)).toFixed(2);
                    this.lastInstallment.baseDecorationFee = baseDecorationFee;
                    return baseDecorationFee;
                }
            },
        },

        mounted: function() {
            this.lastInstallment.orderId=this.orderId;
            this.workerSalary.orderId=this.orderId;
            this.init();
        },
        methods: {

            init:function(){
                let that = this;
                ajax.get('api/bss/order/order-fee/queryLastInstallmentCollect', {orderId:this.lastInstallment.orderId}, function(data) {
                    Object.assign(that.workerSalary, data.workerSalaryDTO);
                    Object.assign(that.lastInstallment, data.lastInstallmentInfoDTO);
                })
            },

            save : function() {

                this.$refs['lastInstallment'].validate((valid) => {
                    if (valid) {
                        let url = 'api/bss/order/order-fee/saveLastInstallmentCollect';
                        let data = {
                            "lastInstallmentInfoDTO": this.lastInstallment,
                            "workerSalaryDTO": this.workerSalary,
                        };
                        ajax.post(url, data,null,null,true);
                    } else {
                        return false;
                    }
                });

            },

            applyFinanceAuditLast:function () {
                this.$refs['lastInstallment'].validate((valid) => {
                    if (valid) {
                        let url = 'api/bss/order/order-fee/applyFinanceAuditLast';
                        let data = {
                            "lastInstallmentInfoDTO": this.lastInstallment,
                            "workerSalaryDTO": this.workerSalary,
                        };
                        ajax.post(url, data);
                    } else {
                        return false;
                    }
                });

            }

        }
    });

    return vm;

})