require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util'], function(Vue, element, axios, ajax, vueselect, util) {
    let vm = new Vue({
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
                this.budget = responseData;
            });
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