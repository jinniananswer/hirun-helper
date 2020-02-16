require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-selectemployee','cust-visit'], function(Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee,custVisit) {
    let vm = new Vue({
        el: '#app',
        data() {
            return {
                decorateContract: {
                    orderId : '',
                    signDate : util.getNowDate(),
                    contractStartDate : util.getNowDate(),
                    contractEndDate : '',
                    chargeSecondFeeDate : '',
                    busiLevel : '',
                    environmentalTestingAgency: '',
                    contractFee : '',
                    baseDecorationFee : '0',
                    doorFee : '0',
                    furnitureFee : '0',
                    activityId : '',
                    chargedDesignFee : '0',
                    returnDesignFee : '0',
                    taxFee : '0',
                    cashDiscount : '0',
                    discountItem : ''
                },
                id : util.getRequest('id'),
                orderState : util.getRequest('orderState'),
                activities : []
            }
        },
        computed: {
            getContractFee: {
                get() {
                    let contractFee = parseFloat(this.decorateContract.baseDecorationFee) + parseFloat(this.decorateContract.doorFee)
                        + parseFloat(this.decorateContract.furnitureFee) - parseFloat(this.decorateContract.returnDesignFee)
                        + parseFloat(this.decorateContract.taxFee);
                    this.decorateContract.contractFee = contractFee;
                    return contractFee;
                }
            }
        },
        mounted: function() {
            // if(this.orderState == 'fail') {
            //     let data = {
            //         id : this.id
            //     }
            //     ajax.post('api/bss.order/order-budget/getBudgetById', data, (responseData)=>{
            //         Object.assign(this.budget, responseData);
            //     });
            // }
            // this.isCheckFailed = this.orderState == 'fail' ? true : false;
            // this.budget.activities = [
            //     {value : "1", name : "活动3"},
            //     {value : "2", name : "活动4"}
            // ];
            // this.budget.checkUserId = 296;
        },
        methods: {
            submit : function() {
                let url = 'api/bss.order/order-budget/submitBudget';
                if(this.orderState == 'fail') {
                    url = 'api/bss.order/order-budget/submitBudgetCheckedResult';
                }
                ajax.post(url, this.budget);
            },
            openDiscntItemWindow : function () {
                alert('优惠项目录入')
            },
            uploadDecorateContract : function () {
                alert('上传合同附件')
            }
        }
    });

    return vm;

})