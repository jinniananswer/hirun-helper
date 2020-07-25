require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util', 'order-selectemployee'], function(Vue, element, axios, ajax, vueselect, util, orderSelectemployee) {
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                queryCond: {
                    employeeId: '',
                    roleId: '',
                    orderCreateStartDate: '',
                    orderCreateEndDate: '',
                },
                roles: [],
                employeeResults: [],
                queryCondRules : {
                    roleId: [
                        {required: true, message: '请选择角色', trigger: 'change'}
                    ],
                    startDate: [
                        {required: true, message: '请选择开始时间', trigger: 'change'}
                    ],
                    endDate: [
                        {required: true, message: '请选择结束时间', trigger: 'change'}
                    ],
                }
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
                this.$refs['queryCond'].validate((valid) => {
                    if (valid) {
                        ajax.get('api/bss.order/order-domain/queryEmployeeResults', this.queryCond, function(responseData){
                            that.employeeResults = responseData;
                        });
                    } else {
                        return false;
                    }
                });
            },
            changeRole: function() {
                // alert(this.queryCond.roleId);
            }
        }
    });

    return vm;
})