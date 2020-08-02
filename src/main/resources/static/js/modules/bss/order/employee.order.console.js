require(['vue', 'ELEMENT', 'axios', 'ajax', 'util', 'vxe-table', 'vueselect','house-select'], function(Vue, element, axios, ajax, util, table, vueSelect,  houseSelect) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                tasks: [],
                queryCond: {
                    orderStatus: util.getRequest('status'),
                    count: 0,
                    limit: 20,
                    page: 1,
                },
                options: []
            }
        },

        methods: {
            query : function() {
                let that = this;
                ajax.get("api/bss.config/order-status-cfg/queryAll", null, function(data) {
                    if (data == null) {
                        return;
                    }
                    that.options = data;
                    that.queryCond.page = data.current;
                    that.queryCond.count = data.total;
                });
                ajax.get('api/bss.order/order-domain/queryOrderTasks', this.queryCond, function(data) {
                    if (data == null) {
                        return null;
                    }
                    that.tasks = data.records;
                })
            },

            openProcess : function(url, title, orderId, custId, status) {
                util.openPage(url+'&orderId='+orderId+'&custId='+custId+'&status='+status, title);
            }
        },

        mounted () {
            this.query();
        }
    });

    return vm;
})