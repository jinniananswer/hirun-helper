require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util'], function(Vue, element, axios, ajax, vueselect, util) {
    let orderState = util.getRequest('orderState');
    let id = util.getRequest('id');
    let vm = new Vue({
        el: '#app',
        data() {
            return {
                budget: {
                    id : '',
                    orderId : '',
                    fee : 0,
                    reportedBudgetDate : '',
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
                    checkUserId : '',
                }
            }
        },
        mounted: function() {
            if(orderState == 'fail') {
                let data = {
                    id : id
                }
                ajax.post('api/bss.order/order-budget/getBudgetById', data, (responseData)=>{
                    this.budget = responseData;
                    this.$set(this.budget,'activities',[
                        {value : "1", name : "活动3"},
                        {value : "2", name : "活动4"}
                    ])
                });
            } else {
                this.$set(this.budget,'activities',[
                    {value : "1", name : "活动3"},
                    {value : "2", name : "活动4"}
                ])
            }

        },
        methods: {
            submit : function() {
                ajax.post('api/bss.order/order-budget/submitBudget', this.budget /*(responseData)=>{*/
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