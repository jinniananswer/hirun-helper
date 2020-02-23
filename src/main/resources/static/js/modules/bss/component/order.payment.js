define(['vue','ELEMENT','ajax', 'vxe-table'], function(Vue,element,ajax, table){
    Vue.use(table);
    Vue.component('order-payment', {
        data : function(){
            return {
                payments: []
            }
        },

        template: `
            <vxe-table
                border
                resizable
                :data="payments"
                :edit-config="{trigger: 'click', mode: 'cell'}">
                <vxe-table-column field="paymentName" title="付款方式"></vxe-table-column>
                <vxe-table-column field="money" title="付款金额（单位：元）" :edit-render="{name: 'input', attrs: {type: 'text'}}"></vxe-table-column>
            </vxe-table>
            `,

        methods: {
            init() {
                let that = this;
                ajax.get('api/bss.order/order-domain/queryPayment', null, function(data) {
                    that.payments = data;
                })
            }
        },

        watch: {

        },

        mounted () {
            this.init();
        }
    });


})