require(['vue', 'ELEMENT', 'axios', 'ajax', 'util', 'vxe-table', 'vueselect','house-select'], function(Vue, element, axios, ajax, util, table, vueSelect,  houseSelect) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                activeName: 'business',
                tasks: [],
                queryCond: {
                    auditStatus: '3',
                    count: 0,
                    limit: 20,
                    page: 1,
                },
                nonQueryCond: {
                    auditStatus: '3',
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
            query : function() {
                let that = this;
                ajax.get('api/finance/finance-field/queryBusinessReceipt', this.queryCond, function(data) {
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
                if (auditStatus == '3') {
                    url = 'openUrl?url=modules/bss/order/finance/audit_fee';
                    title = '主营收款收单';
                }
                util.openPage(url+'&orderId='+orderId+'&custId='+custId+'&payNo='+payNo, title);
            },

            init: function() {
                let url = 'api/bss.order/finance/initCollectionItems';
                let that = this;
                ajax.get(url, null, function (data) {
                    that.payItemOptions = data;
                });
            },
            queryPayInfoByCond: function() {
                let that = this;
                ajax.post('api/finance/finance-field/queryNonBusinessReceipt', this.nonQueryCond, function (responseData) {
                    that.normalPayInfos = responseData.records;
                    that.nonQueryCond.page = responseData.current;
                    that.nonQueryCond.count = responseData.total;
                });
            },

            toPayDetail: function(payNo, auditStatus) {
                if (auditStatus == '3') {
                    util.openPage('openUrl?url=modules/bss/order/finance/cashier/audit_non_business&payNo='+payNo, '非主营收款收单');
                }
            },

            toDetail: function(payNo) {
                util.openPage('openUrl?url=modules/bss/order/finance/cashier/audit_non_business&mode=readOnly&payNo='+payNo, '非主营收款详情');
            }
        },

        mounted () {
            this.query();
            this.init();
        }
    });

    return vm;
})