require(['vue', 'ELEMENT','ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-payment', 'org-selectemployee'], function(Vue, element, ajax, vueselect, util, custInfo, orderInfo, orderWorker, payment, selecteemployee) {
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                auditData: {
                    orderId:util.getRequest('orderId'),
                    payNo:util.getRequest('payNo'),
                    custId:util.getRequest('custId'),
                    auditStatus:0,
                    auditComment: null,
                },
                remark: null,
                payItems: [],
                payments: [],
            }
        },
        mounted: function() {
            this.init();
        },
        methods: {
            init() {
                let that = this;
                let url = 'api/bss.order/finance/initPayComponent';
                if (this.auditData.orderId != null && this.auditData.payNo != null) {
                    url += '?orderId=' + this.auditData.orderId + '&payNo=' + this.auditData.payNo;
                }
                ajax.get(url, null, function(data) {
                    if (data.payments && data.payments.length > 0) {
                        let tempPayments = [];
                        for (let i=0;i<data.payments.length;i++) {
                            let payment = data.payments[i];
                            if (payment.money != 0) {
                                tempPayments.push(payment);
                            }
                        }
                        that.payments = tempPayments;
                    }
                    that.remark = data.remark;
                    that.auditData.auditComment = data.auditComment;
                    that.auditData.auditStatus = data.auditStatus;
                    if (data.payItems) {
                        that.payItems = data.payItems;
                    }
                });
            },
            submitAudit:  function(){
                this.auditData['auditStatus'] = "1";
                ajax.post('api/bss/order/order-fee/costReview', this.auditData);

            },
            auditFailed:  function() {
                this.auditData['auditStatus'] = "2";
                ajax.post('api/bss/order/order-fee/costReview', this.auditData);

            },

            submitReceipt : function() {
                if (this.auditData.financeEmployeeId == null || this.auditData.financeEmployeeId == '') {
                    this.$message.error('收单会计不能为空');
                    return;
                }
                let data = {
                    orderId: this.auditData.orderId,
                    payNo: this.auditData.payNo,
                    financeEmployeeId: this.auditData.financeEmployeeId
                }
                ajax.get('api/bss.order/finance/submitBusinessReceipt', data);
            }
        }
    });

    return vm;
})