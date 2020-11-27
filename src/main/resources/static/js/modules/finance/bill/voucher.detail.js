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
                payForm: {},
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

                    let param = {voucherNo: that.voucher.voucherNo};
                    ajax.get('/api/finance/finance-pay-no/getFinancePay', param, function(value) {
                        if (value == null) {
                            return;
                        }
                        that.payForm = value;
                        that.payments = value.payments;
                        that.payForm.chineseTotalMoney = util.moneyToChinese(that.payForm.totalMoney);
                    });
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
            }
        },

        mounted () {
            this.init();
        }
    });

    return vm;
})