require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util', 'house-select'], function(Vue, element, axios, ajax, vueselect, util, houseSelect) {
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
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
            onSubmit: function() {
                let that = this;
                ajax.get('api/bss.order/order-domain/queryMyOrder', this.queryCond, function(responseData){
                    that.custOrder = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;
                });
            },

            toOrderDetail(orderId, custId) {
                util.openPage('openUrl?url=modules/bss/order/cust_order_detail&orderId='+orderId+'&custId='+custId, '订单详情');
            }
        }
    });

    return vm;
})