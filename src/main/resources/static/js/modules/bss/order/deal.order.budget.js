require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-selectemployee','cust-visit'], function(Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee,custVisit) {    let vm = new Vue({
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
                checkedFail : false
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
            }
            this.activities = [
                {value : "1", name : "活动3"},
                {value : "2", name : "活动4"}
            ];
            this.submitButtonName = '重新提交二级精算';
        },
        methods: {
            submit : function() {
                let data = this.budget;
                data.fee = data.fee * 100;
                let url = 'api/bss.order/order-budget/submitBudget';
                ajax.post(url, data);
            }
        }
    });

    return vm;
})