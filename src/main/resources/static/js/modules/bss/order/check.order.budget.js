require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-selectemployee','cust-visit'], function(Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee,custVisit) {    let vm = new Vue({
        el: '#app',
        data() {
            return {
                budget: {
                    id : '',
                    orderId : '',
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
                }
            }
        },
        mounted: function() {
            let data = {
                orderId : util.getRequest("orderId")
            }
            ajax.post('api/bss.order/order-budget/getBudgetByOrderId', data, (responseData)=>{
                Object.assign(this.budget, responseData);
            });
            this.budget.checkDate = util.getNowDate();
        },
        methods: {
            submit : function(command) {
                let data = this.budget;
                data.checkResult = command;
                ajax.post('api/bss.order/order-budget/submitBudgetCheckedResult', data);
            }
        }
    });

    return vm;
})