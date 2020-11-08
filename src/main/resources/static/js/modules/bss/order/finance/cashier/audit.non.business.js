require(['vue', 'ELEMENT', 'axios', 'ajax', 'vxe-table', 'vueselect', 'org-selectemployee', 'org-orgtree', 'util'], function (Vue, element, axios, ajax, table, vueselect, orgSelectEmployee, orgTree, util) {
    Vue.use(table);
    let vm = new Vue({
            el: '#app',
            data: function () {
                return {
                    mode: util.getRequest('mode'),
                    payItems: [],
                    payments: [],
                    payDate: '',
                    needPay: '',
                    remark: '',
                    auditStatus: '',
                    auditComment: '',
                    receiveComment: null,
                    financeEmployeeId: null,
                    payNo: util.getRequest("payNo")
                }
            },
            methods: {
                init() {
                    let that = this;
                    let url = 'api/bss.order/finance/initCollectionComponent';
                    if (this.payNo != null) {
                        url += '?payNo=' + this.payNo;
                    } else {
                        url += '?payNo=0';
                    }
                    ajax.get(url, null, function (data) {
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
                        if (data.payItems) {
                            that.payItems = data.payItems;
                        }

                        that.needPay = data.needPay;

                        if (data.payDate) {
                            that.payDate = data.payDate;
                        }
                        if (data.needPay) {
                            that.needPay = data.needPay;
                        }
                        if (data.auditComment) {
                            that.auditComment = data.auditComment;
                        }
                        if (data.remark) {
                            that.remark = data.remark;
                        }
                        if (data.auditStatus) {
                            that.auditStatus = data.auditStatus;
                        }
                    });
                },

                footerMethod({columns, data}) {
                    return [
                        columns.map((column, columnIndex) => {
                            if (['money'].includes(column.property)) {
                                let total = 0;
                                data.forEach(function (v, k) {
                                    total += parseFloat(v.money);
                                })
                                return "合计: " + total.toFixed(2) + "元"
                            }
                            return '-'
                        })
                    ]
                },

                submitAudit: function() {
                    let data = {
                        payNo: this.payNo,
                        auditComment: this.auditComment,
                        auditStatus: '1'
                    }
                    ajax.post('api/bss.order/finance/nonCollectFeeForAudit', data);
                },

                auditFailed: function() {
                    let data = {
                        payNo: this.payNo,
                        auditComment: this.auditComment,
                        auditStatus: '2'
                    }
                    ajax.post('api/bss.order/finance/nonCollectFeeForAudit', data);
                },

                submitReceipt : function() {
                    if (this.financeEmployeeId == null || this.financeEmployeeId == '') {
                        this.$message.error('收单会计不能为空');
                        return;
                    }
                    let data = {
                        payNo: this.payNo,
                        financeEmployeeId: this.financeEmployeeId
                    }
                    ajax.get('api/bss.order/finance/submitNonBusinessReceipt', data);
                },

                submitReceive:  function(){
                    let auditData = {
                        payNo: this.payNo,
                        receiveComment : this.receiveComment,
                        auditStatus: '4'
                    }
                    ajax.post('api/finance/finance-field/submitNonBusinessReceiveReceipt', auditData);

                },

                submitNoReceive:  function(){
                    let auditData = {
                        payNo: this.payNo,
                        receiveComment : this.receiveComment,
                        auditStatus: '5'
                    }
                    ajax.post('api/finance/finance-field/submitNonBusinessReceiveReceipt', auditData);
                }
            }
        })
    ;
    vm.init();
    return vm;
})