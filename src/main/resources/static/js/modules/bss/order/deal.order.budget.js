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
                    activities : [],
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
                    checkUserId : ''
                },
                id : util.getRequest('id'),
                orderState : util.getRequest('orderState'),
                isCheckFailed : ''
            }
        },
        mounted: function() {
            if(this.orderState == 'fail') {
                let data = {
                    id : this.id
                }
                ajax.post('api/bss.order/order-budget/getBudgetById', data, (responseData)=>{
                    Object.assign(this.budget, responseData);
                });
            }
            this.isCheckFailed = this.orderState == 'fail' ? true : false;
            this.budget.activities = [
                {value : "1", name : "活动3"},
                {value : "2", name : "活动4"}
            ];
            // this.budget.checkUserId = 296;
        },
        methods: {
            submit : function() {
                let url = 'api/bss.order/order-budget/submitBudget';
                if(this.orderState == 'fail') {
                    url = 'api/bss.order/order-budget/submitBudgetCheckedResult';
                }
                ajax.post(url, this.budget);
            }
        }
    });

    return vm;
})