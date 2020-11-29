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
                auditForm: {},
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
                });
            },

            submitAuditNo : function() {
                if (!this.auditForm.auditComment) {
                    this.$message.error('审核意见不能为空~~~~~~~！');
                    return;
                }
                ajax.post('api/finance/finance-voucher/auditSingleVoucherNo', '&id=' + this.id + '&auditComment=' + this.auditForm.auditComment);
            }
        },

        mounted () {
            this.init();
        }
    });

    return vm;
})