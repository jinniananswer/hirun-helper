require(['vue','ELEMENT','ajax', 'vxe-table', 'vueselect', 'org-orgtree','house-select', 'util', 'cust-info', 'order-info', 'order-worker'], function(Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util, custInfo, orderInfo, orderWorker) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                custId: util.getRequest('custId'),
                orderId: util.getRequest('orderId'),
                queryCond: {
                    custName: '',
                    sex: '',
                    mobileNo: '',
                    housesId: null,
                    orderStatus: '',
                    limit: 20,
                    page: 1,
                    count: null
                },
                dialogVisible: false,
                show: 'display:none',
                queryShow: 'display:none',
                custOrder: [],
                designRoyaltyDetails: [],
                projectRoyaltyDetails: [],
                activeTab:'designRoyaltyTab'
            }
        },

        methods: {
            init: function() {
                this.show = 'display:none';
                this.queryShow = 'display:block';
            },

            query: function() {
                let that = this;
                ajax.get('api/bss.order/finance/queryCustOrderInfo', this.queryCond, function(responseData){
                    that.custOrder = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;
                });
            },

            showCustQuery: function() {
                this.dialogVisible = true;
            },

            selectCustOrder: function(orderId, custId) {
                this.custId = custId;
                this.orderId = orderId;
                this.dialogVisible = false;
                this.show = 'display:block';
                let that = this;
                ajax.get('api/bss.salary/salary-royalty-detail/queryRoyaltyByOrderId', {orderId: this.orderId}, function(responseData) {
                    if (responseData.designRoyaltyDetails) {
                        that.designRoyaltyDetails = responseData.designRoyaltyDetails;
                    }
                    if (responseData.projectRoyaltyDetails) {
                        that.projectRoyaltyDetails = responseData.projectRoyaltyDetails;
                    }
                });
            },

            submit : function() {
                ajax.post('api/bss.salary/salary-fix/submitFixSalaries', this.employees);
            },

            audit : function() {
                ajax.post('api/bss.salary/salary-fix/auditFixSalaries', this.employees);
            },

            isModify: function(obj) {
                if (obj.row.isModified == '1') {
                    return "modify_row";
                }
            },

            activeRowMethod ({ row, rowIndex }) {
                if (row.auditStatus ==  '1' || row.auditStatus == '2' || row.auditStatus == '4') {
                    return false;
                }

                return true;
            }
        },

        mounted () {
            this.init();
        }
    });

    return vm;
});