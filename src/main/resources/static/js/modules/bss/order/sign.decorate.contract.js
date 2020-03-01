require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-selectemployee','cust-visit', 'vxe-table', 'order-search-employee'], function(Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee,custVisit, vxetable, orderSearchEmployee) {
    Vue.use(vxetable);
    let vm = new Vue({
        el: '#app',
        data() {
            return {
                decorateContract: {
                    orderId : '',
                    signDate : util.getNowDate(),
                    startDate : util.getNowDate(),
                    endDate : '',
                    chargeSecondFeeDate : '',
                    busiLevel : '',
                    environmentalTestingAgency: '',
                    contractFee : '',
                    baseDecorationFee : '0',
                    doorFee : 0,
                    furnitureFee : '0',
                    activityId : '',
                    chargedDesignFee : '0',
                    returnDesignFee : '0',
                    taxFee : '0',
                    cashDiscount : '0',
                    remark : '',
                    firstContractFee : '0',
                    financeEmployeeId : '',
                },
                discountItemDetailList : [],
                orderId : util.getRequest('orderId'),
                orderStatus : util.getRequest('status'),
                activities : [],
                discountItemDetail: false,
                decorateContractRules : {
                    signDate: [
                        { required: true, message: '请选择签订合同时间', trigger: 'change' }
                    ],
                    startDate: [
                        { required: true, message: '请选择合同开始时间', trigger: 'change' }
                    ],
                    endDate: [
                        { required: true, message: '请填写合同结束时间', trigger: 'change' }
                    ],
                    chargeSecondFeeDate: [
                        { required: true, message: '请选择合同二期款时间', trigger: 'change' }
                    ],
                    busiLevel: [
                        {required: true, message: '请选择业务级别', trigger: 'change'},
                    ],
                    environmentalTestingAgency: [
                        {required: true, message: '请选择环保检测机构', trigger: 'change'},
                    ],
                    baseDecorationFee: [
                        {required: true, message: '请填写基础装修总金额', trigger: 'blur'},
                        // {type: 'number', message: '必须为数字', trigger: 'blur'}
                    ],
                    doorFee: [
                        {required: true, message: '请填写门总金额', trigger: 'blur'},
                        // {type: 'number', message: '必须为数字', trigger: 'blur'}
                    ],
                    furnitureFee: [
                        {required: true, message: '请填写家具总金额', trigger: 'blur'},
                        // {type: 'number', message: '必须为数字', trigger: 'blur'}
                    ],
                    returnDesignFee: [
                        {required: true, message: '请填写返设计费', trigger: 'blur'},
                        // {type: 'number', message: '必须为数字', trigger: 'blur'}
                    ],
                    taxFee: [
                        {required: true, message: '请填写税金', trigger: 'blur'},
                        // {type: 'number', message: '必须为数字', trigger: 'blur'}
                    ],
                    cashDiscount: [
                        {required: true, message: '请填写现金优惠', trigger: 'blur'},
                        // {type: 'number', message: '必须为数字', trigger: 'blur'}
                    ],
                    financeEmployeeId : [
                        {required: true, message: '请选择财务人员', trigger: 'change'},
                    ]
                },
                statusList : [],
                approveEmployeeList : [
                    // {'label': '女', 'value': '0'},
                    // {'label': '1', 'value': '2'},
                    ]
            }
        },
        computed: {
            getContractFee: {
                get() {
                    let contractFee = parseFloat(this.decorateContract.baseDecorationFee)
                        + parseFloat(this.decorateContract.doorFee)
                        + parseFloat(this.decorateContract.furnitureFee)
                        - parseFloat(this.decorateContract.returnDesignFee)
                        + parseFloat(this.decorateContract.taxFee);
                    this.decorateContract.contractFee = Math.round(contractFee*100) / 100;
                    return this.decorateContract.contractFee;
                }
            },
            getFirstContractFee: {
                get() {
                    let firstContractFee = parseFloat(this.decorateContract.baseDecorationFee) * 0.65
                        + parseFloat(this.decorateContract.doorFee) * 0.95
                        + parseFloat(this.decorateContract.furnitureFee) * 0.95
                        - parseFloat(this.decorateContract.returnDesignFee)
                        + parseFloat(this.decorateContract.taxFee) * 0.95;
                    this.decorateContract.firstContractFee = Math.round(firstContractFee*100) / 100;
                    return this.decorateContract.firstContractFee;
                }
            }
        },
        mounted: function() {
            this.decorateContract.orderId = this.orderId;

            // let arr = this.approveEmployeeList;
            ajax.get('api/bss.order/order-base/selectRoleEmployee', {roleId:19,isSelf:false}, (responseData)=>{
                let arr = [];
                for(let i = 0; i < responseData.length; i++) {
                    arr.push({
                        label: responseData[i].name,
                        value: responseData[i].employeeId
                    });
                }
                this.approveEmployeeList = arr;
            })
            // let data = {
            //     orderId : this.orderId
            // }
            // ajax.get('api/bss.order/order-contract/getDecorateContractInfo', data, (responseData)=>{
            //     Object.assign(this.decorateContract, responseData);
            //     this.fenTransToYuan(this.decorateContract);
            // });
        },
        methods: {
            submit : function() {
                this.$refs['decorateContract'].validate((valid) => {
                    if (valid) {
                        let url = 'api/bss.order/order-contract/submitDecorateContract';
                        let data = this.decorateContract;
                        this.yuanTransToFen(this.data)
                        ajax.post(url, data);
                    } else {
                        return false;
                    }
                });
            },
            uploadDecorateContract : function () {
                alert('上传合同附件')
            },
            openDiscountItemDetailDialog : function() {
                let data = {
                    orderId : this.orderId
                }
                ajax.get('api/bss.order/order-discount-item/list', data, (responseData)=>{
                    for(let i = 0; i < responseData.length; i++) {
                        responseData[i].contractDiscountFee = responseData[i].contractDiscountFee / 100;
                        responseData[i].settleDiscountFee = responseData[i].settleDiscountFee / 100;
                    }
                    this.discountItemDetailList = responseData;
                });
            },
            fenTransToYuan : function(data) {
                data.contractFee = data.contractFee / 100;
                data.baseDecorationFee = data.baseDecorationFee / 100;
                data.doorFee = data.doorFee / 100;
                data.furnitureFee = data.furnitureFee / 100;
                data.chargedDesignFee = data.chargedDesignFee / 100;
                data.returnDesignFee = data.returnDesignFee / 100;
                data.taxFee = data.taxFee / 100;
                data.cashDiscount = data.cashDiscount / 100;
                data.firstContractFee = data.firstContractFee / 100;
            },
            yuanTransToFen : function(data) {
                data.contractFee = Math.round(data.contractFee * 100);
                data.baseDecorationFee = Math.round(data.baseDecorationFee * 100);
                data.doorFee = Math.round(data.doorFee * 100);
                data.furnitureFee = Math.round(data.furnitureFee * 100);
                data.chargedDesignFee = Math.round(data.chargedDesignFee * 100);
                data.returnDesignFee = Math.round(data.returnDesignFee * 100);
                data.taxFee = Math.round(data.taxFee * 100);
                data.cashDiscount = Math.round(data.cashDiscount * 100);
                data.firstContractFee = Math.round(data.firstContractFee * 100);
            },
            saveDiscountItemList : function () {
                let updateRecords = vm.$refs.discountItemTable.getUpdateRecords()
                console.log("updateRecords: " + updateRecords);

                axios({
                    method: 'post',
                    url: 'api/bss.order/order-discount-item/save',
                    data: updateRecords
                }).then(function (responseData) {
                    if (0 == responseData.data.code) {

                        Vue.prototype.$message({
                            message: '保存成功！',
                            type: 'success'
                        });
                    }
                }).catch(function (error) {
                    console.log(error);
                    Vue.prototype.$message({
                        message: '保存失败！',
                        type: 'warning'
                    });
                });
            },
            selectRowChangeEvent ({ row }, evnt) {
                debugger;
                alert(evnt.target.value);
            }
        }
    });

    return vm;

})