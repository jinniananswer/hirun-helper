require(['vue', 'ELEMENT', 'axios', 'ajax', 'util', 'vxe-table', 'vueselect','house-select'], function(Vue, element, axios, ajax, util, table, vueSelect,  houseSelect) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                tasks: [],
                queryCond: {
                    auditStatus: '0',
                    count: 0,
                    limit: 20,
                    page: 1,
                },
            }
        },

        methods: {
            query : function() {
                let that = this;
                ajax.get('api/bss.order/finance/queryFinanceOrderTasks', this.queryCond, function(data) {
                    if (data == null) {
                        return;
                    }
                    that.tasks = data.records;
                    that.queryCond.page = data.current;
                    that.queryCond.count = data.total;
                })
            },

            openProcess(orderId, custId, payNo, auditStatus) {
                let url = null;
                let title = null;
                if (auditStatus == '0') {
                    url = 'openUrl?url=modules/bss/order/finance/audit_fee';
                    title = '出纳复核';
                } else {
                    url = 'openUrl?url=modules/bss/order/finance/collect_fee'
                    title = '收款修改';
                }
                util.openPage(url+'&orderId='+orderId+'&custId='+custId+'&payNo='+payNo, title);
            }
        },

        mounted () {
            this.query();
        }
    });

    return vm;
})