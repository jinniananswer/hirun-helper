require(['vue','ELEMENT','ajax', 'vxe-table', 'vueselect', 'org-orgtree','house-select', 'util', 'order-selectemployee'], function(Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util, selectEmployee) {
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

                dialogQueryCond: {
                    roleId: 1,
                    limit: 20,
                    page: 1,
                    count: 0
                },

                roles : [{
                        roleId: 30,
                        roleName: "设计师"
                    },
                    {
                        roleId: 15,
                        roleName : "客户代表"
                    }],
                balances: [],
                custOrder: [],
                dialogVisible : false

            }
        },

        methods: {
            init: function() {

            },

            query: function() {
                let that = this;
                ajax.get('api/bss/order/order-fee/queryNoBalanceFees', this.queryCond, function(responseData){
                    if (responseData) {
                        that.balances = responseData.records;
                        that.queryCond.page = responseData.current;
                        that.queryCond.count = responseData.total;
                    }

                });
            },

            showCustQuery: function() {
                this.dialogVisible = true;
            },

            dialogQuery: function() {
                let that = this;
                ajax.get('api/bss.order/finance/queryCustOrderInfo', this.dialogQueryCond, function(responseData){
                    that.custOrder = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;
                });
            },

            selectCustOrder: function(orderId, custId, custName) {
                this.queryCond.custId = custId;
                this.queryCond.orderId = orderId;
                this.queryCond.custName = custName;
                this.dialogVisible = false;
            }
        },

        mounted () {
            this.init();
        }
    });

    return vm;
});