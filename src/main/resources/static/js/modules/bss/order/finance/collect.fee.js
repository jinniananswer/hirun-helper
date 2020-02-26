require(['vue', 'ELEMENT','ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-payment'], function(Vue, element, ajax, vueselect, util, custInfo, orderInfo, orderWorker, payment) {
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                custId: 263,
                orderId: 1
            }
        },

        methods: {
            submit : async function() {
                let isValid = await this.$refs.pay.valid().then(isValid=>isValid);
                if (isValid) {
                    let data = this.$refs.pay.getSubmitData();
                    data['orderId'] = this.orderId;
                    ajax.post('api/bss.order/order-domain/collectFee', data);
                }
            }
        }
    });

    return vm;
})