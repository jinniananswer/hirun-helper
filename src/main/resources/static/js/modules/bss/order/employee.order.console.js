require(['vue', 'ELEMENT', 'axios', 'ajax', 'util', 'vxe-table', 'vueselect','house-select'], function(Vue, element, axios, ajax, util, table, vueSelect,  houseSelect) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                tasks: [],
                queryCond: {
                    orderStatus: util.getRequest('status')
                },
                options: []
            }
        },

        methods: {
            init : function() {
                let that = this;
                ajax.get("api/bss.config/order-status-cfg/queryAll", null, function(data) {
                    if (data == null) {
                        return;
                    }
                    that.options = data;
                });
            },

            query : function() {
                let that = this;
                ajax.get('api/bss.order/order-domain/queryOrderTasks', this.queryCond, function(data) {
                    if (data == null) {
                        return null;
                    }
                    that.tasks = data;
                })
            },

            openProcess : function(url, title, orderId, custId, status) {
                util.openPage(url+'&orderId='+orderId+'&custId='+custId+'&status='+status, title);
            }
        },

        mounted () {
            this.init();
            this.query();
        }
    });

    return vm;
})