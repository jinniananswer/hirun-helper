require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-selectemployee','cust-visit', 'vxe-table', 'order-search-employee'], function(Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee,custVisit, vxetable, orderSearchEmployee) {
    Vue.use(vxetable);
    let vm = new Vue({
        el: '#app',
        data() {
            return {
                woodContract: {
                    id:'',
                    orderId : '',
                    signDate : '',
                    startDate : '',
                    endDate : '',
                    environmentalTestingAgency: '',
                    contractFee : '0',
                    doorFee : '0',
                    furnitureFee : '0',
                    returnDesignFee : '0',
                    taxFee : '0',
                    remark : '',
                    financeEmployeeId : '',
                    projectEmployeeId:'',
                    cupboardFee:'0',
                    firstCupboardFee:'0',
                    firstContractFee:'0',
                    chargedWoodFee:'0',
                    projectEmployeeName:'',
                    financeEmployeeName:'',
                },
                orderId : util.getRequest('orderId'),
                custId:util.getRequest('custId'),

                woodContractRules : {
                    signDate: [
                        { required: true, message: '请选择签订合同时间', trigger: 'change' }
                    ],
                    startDate: [
                        { required: true, message: '请选择合同开始时间', trigger: 'change' }
                    ],
                    endDate: [
                        { required: true, message: '请填写合同结束时间', trigger: 'change' }
                    ],
                    environmentalTestingAgency: [
                        {required: true, message: '请选择环保检测机构', trigger: 'change'},
                    ],
                    financeEmployeeId : [
                        {required: true, message: '请选择财务人员', trigger: 'change'},
                    ],
                    projectEmployeeId : [
                        {required: true, message: '请选择财务人员', trigger: 'change'},
                    ]
                },
            }
        },
        computed: {
            getContractFee: {
                get() {
                    let contractFee = parseFloat(this.woodContract.doorFee)
                        + parseFloat(this.woodContract.furnitureFee)
                        - parseFloat(this.woodContract.returnDesignFee)
                        + parseFloat(this.woodContract.taxFee)
                        + parseFloat(this.woodContract.cupboardFee);
                    this.woodContract.contractFee = Math.round(contractFee*100) / 100;
                    return this.woodContract.contractFee;
                }
            },

            getFirstContractFee: {
                get() {
                    let firstContractFee = parseFloat(this.woodContract.doorFee) * 0.95
                        + parseFloat(this.woodContract.furnitureFee) * 0.95
                        - parseFloat(this.woodContract.returnDesignFee)
                        + parseFloat(this.woodContract.taxFee) * 0.95
                        + parseFloat(this.woodContract.cupboardFee) * 0.95;
                    this.woodContract.firstContractFee = Math.round(firstContractFee*100) / 100;
                    return this.woodContract.firstContractFee;
                }
            },

            getFirstCupBoardFee: {
                get() {
                    let firstCupBoardFee = this.woodContract.cupboardFee;
                    this.woodContract.firstCupboardFee=firstCupBoardFee;
                    return this.woodContract.firstCupboardFee;
                }
            }
        },

        mounted: function() {
            //this.init();
            this.woodContract.orderId = this.orderId;
        },

        methods: {
            submit : function() {
                this.$refs['woodContract'].validate((valid) => {
                    if (valid) {
                        let url = 'api/bss.order/order-contract/submitWoodContract';
                        let data = this.woodContract;
                        ajax.post(url, data);
                    } else {
                        return false;
                    }
                });
            },

            save:function(){

            },

            uploadDecorateContract : function () {
                alert('上传合同附件')
            },

            init(){
                let that = this;
                ajax.get('api/bss.order/order-contract/queryWoodContract', {orderId:this.orderId}, function(data) {
                    that.woodContract=data;
                })
            },

        }
    });

    return vm;

})