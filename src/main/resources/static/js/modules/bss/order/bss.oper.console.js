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
                ajax.get('api/bss.order/order-domain/getPendingTask', {custId:this.custId, orderId:this.orderId}, function(data) {
                    that.tasks = data;
                })
            },

            openProcess(url,title) {
                util.openPage(url, title);
            }
        },

        mounted () {
            this.init();
        }
    });

    return vm;
})