require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-selectemployee','cust-visit','order-search-employee', 'order-payment'], function(Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee,custVisit,orderSearchEmployee,payment) {
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                custId: util.getRequest("custId"),
                orderId: util.getRequest("orderId"),
                order:{

                },
                stages:{
                    0: '酝酿',
                    10: '初选',
                    30: '初步决策',
                    50: '决策',
                    60: '施工',
                    95: '维护'
                },
                feeActiveTab: 'feeInfo'
            }
        },

        methods: {
            init : function() {
                let that = this;
                if (this.orderId != null) {
                    ajax.get('api/bss.order/order-domain/getOrderDetail', {orderId:this.orderId}, function(data) {
                        let stage = data.stage;
                        data.stage = [];
                        data.stage.push(-10);
                        data.stage.push(stage);
                        that.order = data;
                    });
                }
            }
        },

        mounted () {
            this.init();
        }
    });

    return vm;
})