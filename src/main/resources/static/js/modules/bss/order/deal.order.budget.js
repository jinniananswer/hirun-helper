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
            }
        },
        mounted: function() {
            this.budget.orderId = this.orderId;
            alert(this.orderStatus);
            if(this.orderState == '15') {
                let data = {
                    orderId : this.orderId
                }
                ajax.post('api/bss.order/order-budget/getBudgetById', data, (responseData)=>{
                    Object.assign(this.budget, responseData);
                });
            }
            this.activities = [
                {value : "1", name : "活动3"},
                {value : "2", name : "活动4"}
            ];
        },
        methods: {
            submit : function() {
                this.budget.fee = this.budget.fee * 100;
                let url = 'api/bss.order/order-budget/submitBudget';
                if(this.orderStatus == '15') {
                    url = 'api/bss.order/order-budget/submitBudgetCheckedResult';
                }
                ajax.post(url, this.budget);
            }
        }
    });

    return vm;
})