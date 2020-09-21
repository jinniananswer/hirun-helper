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
                ajax.get('api/bss.order/order-domain/getPendingTask', null, function(data) {
                    that.tasks = data;
                })
            },

            openProcess(url, title, orderId, custId, status) {
                util.openPage(url+'&orderId='+orderId+'&custId='+custId+'&status='+status, title);
            }
        },

        mounted () {
            this.init();
        }
    });

    return vm;
})