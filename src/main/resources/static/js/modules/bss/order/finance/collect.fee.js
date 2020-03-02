require(['vue', 'ELEMENT','ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-payment', 'house-select'], function(Vue, element, ajax, vueselect, util, custInfo, orderInfo, orderWorker, payment, houseSelect) {
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                custId: util.getRequest('custId'),
                orderId: util.getRequest('orderId'),
                payNo: util.getRequest('payNo'),

                dialogVisible: false,

                custQueryVisible: true,

                show: 'display:none',
                queryShow: 'display:none',

                queryCond: {
                    custName: '',
                    sex: '',
                    mobileNo: '',
                    housesId: null,
                    orderStatus: '',
                    limit: 20,
                    page: 1,
                    count: null
                },

                custOrder: []
            }
        },

        methods: {
            init: function() {
                if (this.custId) {
                    this.show = 'display:block';
                    this.queryShow = 'display:none';
                } else {
                    this.show = 'display:none';
                    this.queryShow = 'display:block';
                }
            },

            showCustQuery: function() {
                this.dialogVisible = true;
            },

            selectCustOrder: function(orderId, custId) {
                this.custId = custId;
                this.orderId = orderId;
                this.dialogVisible = false;
                this.show = 'display:block';
            },

            query: function() {
                let that = this;
                ajax.get('api/bss.order/finance/queryCustOrderInfo', this.queryCond, function(responseData){
                    that.custOrder = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;
                });
            },

            submit : async function() {
                let isValid = await this.$refs.pay.valid().then(isValid=>isValid);
                if (isValid) {
                    let data = this.$refs.pay.getSubmitData();
                    data['orderId'] = this.orderId;
                    ajax.post('api/bss.order/finance/collectFee', data);
                }
            }
        },

        mounted () {
            this.init();
        }
    });

    return vm;
})