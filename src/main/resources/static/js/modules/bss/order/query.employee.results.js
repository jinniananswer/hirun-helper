require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util', 'order-selectemployee'], function(Vue, element, axios, ajax, vueselect, util, orderSelectemployee) {
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                queryCond: {
                    employeeId: '',
                    roleId: '',
                    orderCreateStartDate: '2020-01-01',
                    orderCreateEndDate: '2020-08-01 23:59:59',
                },
                roles: [],
                employeeResults: []
            }
        },
        mounted() {
            this.init();
        },
        methods: {
            init : function() {
                let that = this;
                ajax.get("api/user/role/role-list", null, function(data) {
                    that.roles = data;
                });
            },
            onSubmit: function() {
                let that = this;
                ajax.get('api/bss.order/order-domain/queryEmployeeResults', this.queryCond, function(responseData){
                    that.employeeResults = responseData;
                });
            },
            changeRole: function() {
                // alert(this.queryCond.roleId);
            }
        }
    });

    return vm;
})