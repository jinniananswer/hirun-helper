require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util'], function(Vue, element, axios, ajax, vueselect, util) {
    let vm = new Vue({
        el: '#customer_perparation',
        data: function() {
            return {
                employeeOptions: [],

                custBase: {
                    custNo: '',
                    custName: '',
                    custBase: '',
                    mobileNo: ''
                },

                project: {
                    houseMode: '',
                    houseArea: '',
                    housePlace: '',
                    houseBuilding: '',
                    houseRoomNo:'',
                },

                custPreparation: {
                    prepareOrgId: '',
                    prepareEmployeeId: '',
                    prepareTime: util.getNowDate(),
                    enterEmployeeId: '',
                    custProperty:'',
                    refereeFixPlace: '',
                    refereeName: '',
                    refereeMobileNo:'',
                    remark:'',
                },
                custOrder: [],
                display:'display:block',
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

            submit: function() {
                ajax.get('api/organization/employee/searchEmployee', {searchText:'金'}, function(responseData){
                    vm.custOrder = responseData;
                });
            },

        }
    });
    vm.loadEmployee();
    return vm;
})