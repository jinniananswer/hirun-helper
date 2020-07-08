require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util', 'house-select'], function(Vue, element, axios, ajax, vueselect, util, houseSelect) {
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                queryCond: {
                    id: '',
                    name: '',
                    abbreviation: '',
                    supplier_type: '',
                    operator: '',
                    mobile_no: '',
                    Landline: '',
                    mailbox: '',
                    address: '',
                    status: '',
                    contacts: '',
                    standby_telephone: '',
                    verify_person: '',
                    reviewer: '',
                    account_number1: '',
                    limit: 20,
                    page: 1,
                    count: null
                },

                supplierInfo: [],
            }
        },

        methods: {
            querySupplier: function() {
                let that = this;
                ajax.get('api/SupplySupplier/queryByNameAndId', this.queryCond, function(responseData){
                    vm.supplierInfo = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;
                });
            }
        }
    });

    return vm;
})