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
                id : util.getRequest("id")
            }
            ajax.post('api/bss.order/order-budget/getBudgetById', data, (responseData)=>{
                Object.assign(this.budget, responseData);
            });
            this.budget.checkDate = util.getNowDate();
        },
        methods: {
            submit : function() {
                ajax.post('api/bss.order/order-budget/submitBudgetCheckedResult', this.budget /*(responseData)=>{*/
                //     提示成功
                    // this.$alert('提交成功', '提交成功', {
                    //     confirmButtonText: '确定'
                    // });
                /*}*/);
            }
        }
    });

    return vm;
})