require(['vue', 'ELEMENT', 'axios', 'ajax', 'vxe-table', 'vueselect', 'org-selectemployee', 'org-orgtree', 'util'], function (Vue, element, axios, ajax, table, vueselect, orgSelectEmployee, orgTree, util) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function () {
            return {
                queryCond: {
                    count: 0,
                    limit: 20,
                    page: 1
                },
                payItemOptions: null,
                pickerOptions: {
                    shortcuts: [{
                        text: '最近一周',
                        onClick(picker) {
                            const end = util.getNowDate();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                            let begin = util.formatDate(start, "YYYY-MM-DD");
                            picker.$emit('pick', [begin, end]);
                        }
                    }, {
                        text: '最近一个月',
                        onClick(picker) {
                            const end = util.getNowDate();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                            let begin = util.formatDate(start, "YYYY-MM-DD");
                            picker.$emit('pick', [begin, end]);
                        }
                    }, {
                        text: '最近三个月',
                        onClick(picker) {
                            const end = util.getNowDate();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                            let begin = util.formatDate(start, "YYYY-MM-DD");
                            picker.$emit('pick', [begin, end]);
                        }
                    }]
                },
                normalPayInfos: []
            }
        },
        methods: {

            init: function() {
                let url = 'api/bss.order/finance/initCollectionItems';
                let that = this;
                ajax.get(url, null, function (data) {
                    that.payItemOptions = data;
                });
            },
            queryPayInfoByCond: function() {
                let that = this;
                ajax.post('api/bss.order/finance/queryPayInfoByCond', this.queryCond, function (responseData) {
                    that.normalPayInfos = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;
                });
            },

            toPayDetail: function(payNo, auditStatus) {
                if (auditStatus == '0') {
                    util.openPage('openUrl?url=modules/bss/order/finance/cashier/audit_non_business&payNo='+payNo, '非主营收款复核');
                } else if (auditStatus == '1') {
                    util.openPage('openUrl?url=modules/bss/order/finance/cashier/audit_non_business&payNo='+payNo, '非主营收款交单');
                } else if (auditStatus == '2' || auditStatus == '5') {
                    util.openPage('openUrl?url=modules/bss/order/finance/cashier/non_business_collection&payNo='+payNo, '非主营收款修改');
                }
            },

            toDetail: function(payNo) {
                util.openPage('openUrl?url=modules/bss/order/finance/cashier/audit_non_business&mode=readOnly&payNo='+payNo, '非主营收款详情');
            }
        },

        mounted () {
            this.init();
        }
    });
    return vm;
})