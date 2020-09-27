require(['vue','ELEMENT','ajax', 'vxe-table', 'vueselect', 'org-orgtree','house-select', 'util', 'order-selectemployee'], function(Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util, orderSelectEmployee) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                queryCond: {
                    count: 0,
                    limit: 20,
                    page: 1
                },
                factoryOrders: []
            }
        },

        methods: {
            init: function() {

            },

            query: function() {
                let that = this;
                ajax.get('api/bss/order/order-factory-order/queryFactoryOrders', this.queryCond, function(responseData){
                    if (responseData) {
                        that.factoryOrders = responseData.records;
                        that.queryCond.page = responseData.current;
                        that.queryCond.count = responseData.total;
                    }

                });
            },

            redirect: function(row) {
                util.openPage('openUrl?url=modules/bss/order/factory/edit_factory_order&orderId='+row.orderId, '工厂产品订单');
            }
        },

        mounted () {
            this.init();
        }
    });

    return vm;
});