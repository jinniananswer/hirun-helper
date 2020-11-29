require(['vue', 'ELEMENT', 'axios', 'ajax', 'util', 'vxe-table', 'vueselect','house-select', 'order-search-employee'], function(Vue, element, axios, ajax, util, table, vueSelect,  houseSelect, searchEmployee) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                queryCond: {
                    auditStatus: '1',
                    count: 0,
                    limit: 20,
                    page: 1
                },

                vouchers: null,
                voucherItems: null,

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

            auditPass : function() {
                let data = this.$refs.vouchers.getCheckboxRecords();
                if (data == null || data.length <= 0) {
                    this.$message.error('没有选中任何记录，无法提交');
                    return;
                }
                ajax.post('api/finance/finance-voucher/auditPass', data, null, null, true);
            },

            auditNo : function() {
                let data = this.$refs.vouchers.getCheckboxRecords();
                if (data == null || data.length <= 0) {
                    this.$message.error('没有选中任何记录，无法提交');
                    return;
                }
                ajax.post('api/finance/finance-voucher/auditNo', data, null, null, true);
            },

            checkMethod : function({row}) {
                if (row.auditStatus == '1') {
                    return true;
                } else {
                    return false;
                }
            },

            showDetail : function(id, voucherNo) {
                util.openPage('openUrl?url=modules/finance/bill/voucher_detail&id='+id, '领款详情');
            },

            toAuditNo : function(id) {
                util.openPage('openUrl?url=modules/finance/bill/audit_no_voucher&id='+id, '审核不通过');
            }
        },

        mounted () {
            this.init();
        }
    });

    return vm;
})