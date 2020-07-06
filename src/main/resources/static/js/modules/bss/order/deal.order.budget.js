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
                orderStatus : util.getRequest('status'),
                activities : [],
                submitButtonName : '提交对审',
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
            if(this.orderStatus == '15') {
                this.checkedFail = true;
                let data = {
                    orderId : this.orderId
                }
                ajax.post('api/bss.order/order-budget/getBudgetByOrderId', data, (responseData)=>{
                    Object.assign(this.budget, responseData);
                    this.budget.fee = this.budget.fee/100;
                });
                this.submitButtonName = '重新提交二级精算';
            }
            this.activities = [
                {value : "1", name : "活动3"},
                {value : "2", name : "活动4"}
            ];
        },
        methods: {
            submit : function() {
                this.$refs['budget'].validate((valid) => {
                    if (valid) {
                        let data = JSON.parse(JSON.stringify(this.budget));
                        data.fee = data.fee * 100;
                        let url = 'api/bss.order/order-budget/submitBudget';
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