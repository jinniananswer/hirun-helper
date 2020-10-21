require(['vue', 'ELEMENT','ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-payment', 'house-select', 'org-selectemployee'], function(Vue, element, ajax, vueselect, util, custInfo, orderInfo, orderWorker, payment, houseSelect, orgSelectEmployee) {
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                custId: util.getRequest('custId'),
                orderId: util.getRequest('orderId'),
                payNo: util.getRequest('payNo'),
                remark: null,
                auditComment:null,
                cust: {
                    custName: '',
                    housesId: null,
                    address: ''
                },

                dialogVisible: false,

                custQueryVisible: true,

                show: 'display:none',
                queryShow: 'display:none',

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

                custOrder: []
            }
        },

        methods: {
            init: function() {
                if (this.custId) {
                    this.show = 'display:block';
                    this.queryShow = 'display:none';
                } else {
                    this.show = 'display:none';
                    this.queryShow = 'display:block';
                }

                if (this.orderId && this.payNo) {
                    this.getData();
                }
            },

            getData : function() {
                let data = {
                    orderId : this.orderId,
                    payNo : this.payNo
                }
                let that = this;
                ajax.get('api/bss.order/finance/getCustPayData', data, function(responseData){
                    that.cust.custName = responseData.custName;
                    that.cust.housesId = responseData.housesId;
                    that.cust.address = responseData.address;
                    that.remark = responseData.payNoRemark;
                    that.auditComment = responseData.auditComment;
                });
            },

            showCustQuery: function() {
                this.dialogVisible = true;
            },

            selectCustOrder: function(orderId, custId, custName, housesId, address) {
                this.custId = custId;
                this.orderId = orderId;
                this.cust.custName = custName;
                this.cust.housesId = housesId;
                this.cust.address = address;
                this.dialogVisible = false;
                this.show = 'display:block';
            },

            selectCustOrderRow: function(row) {
                this.selectCustOrder(row.orderId, row.custId, row.custName, row.housesId, row.decorateAddress);
            },

            query: function() {
                let that = this;
                ajax.get('api/bss.order/finance/queryCustOrderInfo', this.queryCond, function(responseData){
                    that.custOrder = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;

                });
            },

            submit : async function() {
                let isValid = await this.$refs.pay.valid().then(isValid=>isValid);
                if (isValid) {
                    let data = this.$refs.pay.getSubmitData();
                    data['orderId'] = this.orderId;
                    data['payNo'] = this.payNo;
                    data['remark'] = this.remark;
                    data['custName'] = this.cust.custName;
                    data['housesId'] = this.cust.housesId;
                    data['address'] = this.cust.address;
                    ajax.post('api/bss.order/finance/collectFee', data);
                }
            }
        },

        mounted () {
            this.init();
        }
    });

    return vm;
})