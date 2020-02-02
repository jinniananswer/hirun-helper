require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util'], function (Vue, element, axios, ajax, vueselect, util) {
    let vm = new Vue({
        el: '#customer_perparation',
        data: function () {
            return {
                employeeOptions: [],

                customerPreparation: {
                    prepareOrgId: '',
                    prepareEmployeeId: '',
                    prepareTime:  new Date(),
                    enterEmployeeId: '',
                    custProperty: '',
                    refereeFixPlace: '',
                    refereeName: '',
                    refereeMobileNo: '',
                    custNo: 'KH2020020216',
                    custName: '',
                    mobileNo: '',
                    remark: '',
                    houseMode: '',
                    houseArea: 0,
                    housePlace: '',
                    houseBuilding: '',
                    houseRoomNo: '',
                },
                custOrder: [],
                display: 'display:block',
                id: util.getRequest('id'),

            }
        },

        methods: {
            loadEmployee: function () {
                axios.get('api/organization/employee/loadEmployee').then(function (responseData) {
                    vm.employeeOptions = responseData.data.rows;
                }).catch(function (error) {
                    console.log(error);
                });
            },

            loadPreparationHistory: function () {
                axios.get('api/customer/cust-preparation/loadPreparationHistory?mobileNo='+this.customerPreparation.mobileNo).then(function (responseData) {
                    vm.custOrder = responseData.data.rows;
                }).catch(function (error) {
                    console.log(error);
                });
            },

            submit: function () {
                axios({
                    method: 'post',
                    url: 'api/customer/cust-preparation/addCustomerPreparation',
                    data: this.customerPreparation
                }).then(function (responseData) {
                    if (0 == responseData.data.code) {

                        Vue.prototype.$message({
                            message: '报备成功！',
                            type: 'success'
                        });
                    }
                });
            },

        }
    });
    vm.loadEmployee();
    return vm;
})