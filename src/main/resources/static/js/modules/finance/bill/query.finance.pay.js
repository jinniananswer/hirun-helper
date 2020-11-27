require(['vue', 'ELEMENT', 'axios', 'ajax', 'util', 'vxe-table', 'vueselect','house-select', 'order-search-employee'], function(Vue, element, axios, ajax, util, table, vueSelect,  houseSelect, searchEmployee) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                queryCond: {
                    auditStatus: util.getRequest('auditStatus'),
                    count: 0,
                    limit: 20,
                    page: 1
                },

                vouchers: null,
                voucherItems: null,
                detailVisible : false,

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
            init : function() {

            },

            query : function() {
                let that = this;
                ajax.post('api/finance/finance-voucher/queryVouchers', this.queryCond, function(data) {
                    if (data == null) {
                        return;
                    }
                    that.vouchers = data.records;
                    that.queryCond.page = data.current;
                    that.queryCond.count = data.total;
                });
            },

            checkMethod : function({row}) {
                if (row.auditStatus == '0') {
                    return true;
                } else {
                    return false;
                }
            },

            showDetail : function(voucherNo) {
                let data = {
                    voucherNo : voucherNo
                };
                let that = this;
                ajax.get('api/finance/finance-voucher-item/queryVoucherItems', data, function(response) {
                    if (response == null) {
                        return;
                    }
                    that.voucherItems = response;
                });
                this.detailVisible = true;
            },

            toPay : function(id) {
                util.openPage('openUrl?url=modules/finance/bill/finance_pay&id='+id, '财务付款');
            },

            receive : function(id) {
                ajax.post('api/finance/finance-voucher/receiveVoucher', '&id='+id, null, null, true);
            },

            refuse : function(id) {
                ajax.post('api/finance/finance-voucher/refuseVoucher', '&id='+id, null, null, true);
            }
        },

        mounted () {
            this.init();
        }
    });

    return vm;
})