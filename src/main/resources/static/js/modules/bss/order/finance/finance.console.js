require(['vue', 'ELEMENT', 'axios', 'ajax', 'util'], function(Vue, element, axios, ajax, util) {
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                tasks: null
            }
        },

        methods: {
            init() {
                let that = this;
                ajax.get('api/bss.order/finance/queryFinancePendingTask', null, function(data) {
                    that.tasks = data;
                })
            },

            openProcess(orderId, custId, payNo) {
                util.openPage('openUrl?url=modules/bss/order/finance/audit_fee&orderId='+orderId+'&custId='+custId+'&payNo='+payNo, '出纳复核');
            }
        },

        mounted () {
            this.init();
        }
    });

    return vm;
})