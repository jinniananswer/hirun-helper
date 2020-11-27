require(['vue', 'ELEMENT', 'axios', 'ajax', 'util', 'vxe-table', 'vueselect','house-select', 'order-search-employee'], function(Vue, element, axios, ajax, util, table, vueSelect,  houseSelect, searchEmployee) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                id : util.getRequest('id'),
                voucher: {},
                voucherItems: [],
                payments: [],
                payForm: {
                    payDate : util.getNowDate()
                },
                remark: ''
            }
        },

        methods: {
            init : function() {
                if (!this.id) {
                    return;
                }

                let data = {
                    id : this.id
                }
                let that = this;
                ajax.get('api/finance/finance-voucher/getVoucher', data, function(response) {
                    if (response == null) {
                        return;
                    }
                    that.voucher = response;
                    that.voucherItems = response.voucherItems;
                    that.voucher.chineseTotalMoney = util.moneyToChinese(that.voucher.totalMoney);
                    that.payForm.totalMoney = that.voucher.totalMoney;
                    that.payForm.chineseTotalMoney = that.voucher.chineseTotalMoney
                });

                ajax.get('api/finance/finance-voucher/queryPayments', null, function(response) {
                    if (response == null) {
                        return;
                    }
                    that.payments = response;
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

            valid : function() {
                if (this.payForm.payDate == null || this.payForm.payDate == '') {
                    this.$message.error('付款日期不能为空！');
                    return false;
                }
                let totalMoney = 0;
                for (let i=0;i<this.payments.length;i++) {
                    totalMoney = parseInt(totalMoney) + parseInt(this.payments[i].money);
                    if (this.payments[i].money > 0) {
                        this.$message.error('付款金额必须为负数！');
                        return false;
                    }
                }

                if (totalMoney >= 0) {
                    this.$message.error('付款金额必须为负数！');
                    return false;
                }
                if (Math.abs(totalMoney) != this.payForm.totalMoney) {
                    this.$message.error('付款金额与总金额不一致！');
                    return false;
                }

                return true;
            },

            submit : function() {
                if (!this.valid()) {
                    return;
                }
                this.payForm['payments'] = this.payments;
                this.payForm['voucherNo'] = this.voucher.voucherNo;
                ajax.post('api/finance/finance-pay-no/createFinancePay', this.payForm);
            }
        },

        mounted () {
            this.init();
        }
    });

    return vm;
})