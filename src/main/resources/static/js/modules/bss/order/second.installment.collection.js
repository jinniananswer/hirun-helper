require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-selectemployee','cust-visit'], function(Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee,custVisit) {
    let vm = new Vue({
        el: '#app',
        data() {
            return {
                secondInstallment: {
                    chargedDecorateFee : '1000',
                    orderId : '',
                    secondInstallmentFee : '0',
                    secondContractFee : '0',
                    chargedSecondInstallmentFee : '0',
                    woodProductFee : '0',
                    baseDecorationFee : '0',
                    doorFee : '0',
                    taxFee: '0',
                    furnitureFee : '0',
                    otherFee : '0',
                    HydropowerWages : '0',
                    HydropowerRemark : '',
                    WoodworkingWages : '0',
                    WoodworkingRemark : '',
                    MasonWages : '0',
                    MasonWRemark : '',
                    oilWorkerWages : '0',
                    oilWorkerRemark : '',
                    wallKnockingWorkerWages : '0',
                    wallKnockingWorkerRemark : ''
                },
                orderId : util.getRequest('orderId'),
                orderStatus : util.getRequest('status'),
                // activities : []
            }
        },
        computed: {
            getWoodProductFee: {
                get() {
                    let woodProductFee = parseFloat(this.secondInstallment.doorFee) + parseFloat(this.secondInstallment.furnitureFee)
                    this.secondInstallment.woodProductFee = woodProductFee;
                    return woodProductFee;
                }
            },
            getSecondContractFee : {
                get() {
                    let secondContractFee = parseFloat(this.secondInstallment.woodProductFee) + parseFloat(this.secondInstallment.baseDecorationFee)
                        + parseFloat(this.secondInstallment.taxFee)
                        + parseFloat(this.secondInstallment.otherFee)
                    this.secondInstallment.secondContractFee = secondContractFee;
                    return secondContractFee;
                }
            },
            getSecondInstallmentFee : {
                get() {
                    let secondInstallmentFee = parseFloat(this.secondInstallment.secondContractFee) *0.95 - parseFloat(this.secondInstallment.chargedDecorateFee)
                    this.secondInstallment.secondInstallmentFee = secondInstallmentFee;
                    return secondInstallmentFee
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
            },
            handleClick(tab, event) {
                console.log(tab, event);
            }
        }
    });

    return vm;

})