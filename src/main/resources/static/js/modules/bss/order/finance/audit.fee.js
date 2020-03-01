require(['vue', 'ELEMENT','ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-payment'], function(Vue, element, ajax, vueselect, util, custInfo, orderInfo, orderWorker, payment) {
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                auditData: {
                    orderId:util.getRequest('orderId'),
                    payNo:util.getRequest('payNo'),
                    // payNo: 1,
                    // orderId: 1,
                    auditStatus:0,
                },

            }
        },
        mounted: function() {
            this.init();
        },
        methods: {
            init() {
                // let that = this;
                // ajax.get('api/bss.order/order-domain/initPayComponent', null, function(data) {
                //     that.payments = data.payments;
                //     that.payItems = data.payItems;
                // })
            },
            submitAudit: async function(){
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