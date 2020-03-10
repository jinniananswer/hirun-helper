require(['vue', 'ELEMENT','ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-payment'], function(Vue, element, ajax, vueselect, util, custInfo, orderInfo, orderWorker, payment) {
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                auditData: {
                    orderId:util.getRequest('orderId'),
                    payNo:util.getRequest('payNo'),
                    custId:util.getRequest('custId'),
                    auditStatus:0,
                    auditReason:"",
                },
                payItems: [],
                payments: [],
            }
        },
        mounted: function() {
            this.init();
        },
        methods: {
            init() {
                let that = this;
                let url = 'api/bss.order/finance/initPayComponent';
                if (this.auditData.orderId != null && this.auditData.payNo != null) {
                    url += '?orderId=' + this.auditData.orderId + '&payNo=' + this.auditData.payNo;
                }
                ajax.get(url, null, function(data) {
                    that.payments = data.payments;
                    if (data.payItems) {
                        that.payItems = data.payItems;
                    }
                });
            },
            submitAudit:  function(){
                this.auditData['auditStatus'] = "1";
                    ajax.post('api/bss/order/order-fee/costReview', this.auditData);

            },
            auditFailed(data) {
                this.auditData['auditStatus'] = "2";
                    ajax.post('api/bss/order/order-fee/costReview', this.auditData);

            }


        }
    });

    return vm;
})