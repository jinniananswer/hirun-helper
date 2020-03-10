require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-selectemployee','cust-visit', 'order-file-upload'], function(Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee,custVisit, orderFileUpload) {
    let vm = new Vue({
        el: '#app',
        data() {
            return {
                budget: {
                    id : '',
                    orderId : '',
                    fee : 0,
                    reportedBudgetDate : util.getNowDate(),
                    activityId : '',
                    custIdea : '',
                    totalFeeCheckResult : '',
                    locationRemarkCheckResult : '',
                    contentExpressionCheckResult : '',
                    unitPriceCheckResult : '',
                    logoCheckResult : '',
                    unitPriceConsistenceCheckResult : '',
                    materialRemarkCheckResult : '',
                    serialNumberCheckResult : '',
                    materialRemarkConsistenceCheckResult : '',
                    fontSizeCheckResult : '',
                    selfPurchaseRemarkCheckResult : '',
                    numberConsistenceCheckResult : '',
                    checkDate : '',
                    checkEmployeeId : ''
                },
                orderId : util.getRequest('orderId'),
                custId : util.getRequest('custId'),
                activities : [],
                submitButtonName : '签订木制品合同',
                checkedFail : false,
                budgetRules : {
                    fee: [
                        {required: true, message: '请填写预算总金额', trigger: 'blur'},
                        {type: 'number', message: '必须为数字', trigger: 'blur'}
                    ],
                    checkEmployeeId : [
                        {required: true, message: '请选择对审人', trigger: 'change'}
                    ],
                    reportedBudgetDate : [
                        {required: true, message: '实际看预算时间', trigger: 'change' }
                    ],
                }
            }
        },
        mounted: function() {
            this.budget.orderId = this.orderId;
        },
        methods: {
            submit : function() {
                this.$refs['budget'].validate((valid) => {
                    if (valid) {
                        let data = this.budget;
                        data.fee = data.fee * 100;
                        let url = 'api/bss.order/order-budget/submitWoodContract';
                        ajax.post(url, data);
                    } else {
                        return false;
                    }
                });
            },
            uploadFile : function () {
                alert('上传文件');
            }
        }
    });

    return vm;
})