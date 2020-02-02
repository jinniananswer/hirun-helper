require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util'], function (Vue, element, axios, ajax, vueselect, util) {
    let vm = new Vue({
        el: '#customer_perparation',
        data: function () {
            return {
                employeeOptions: [],
                customerPreparation: {
                    prepareOrgId: '',
                    prepareEmployeeId: '',
                    prepareTime: new Date(),
                    enterEmployeeId: '',
                    custProperty: '',
                    refereeFixPlace: '',
                    refereeName: '',
                    refereeMobileNo: '',
                    custNo: '',
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
                isRefereeFixPlaceDisable: true,
                isRefereeNameDisable: true,
                isRefereeMobileNoDisable: true,
                display: 'display:block',
                id: util.getRequest('id'),
                rules: {
                    custName: [
                        {required: true, message: '请填写客户姓名', trigger: 'blur'},
                        {min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur'}
                    ],
                    mobileNo: [
                        {required: true, message: '请填写电话号码', trigger: 'blur'},
                        {min: 11, max: 11, message: '输入电话号码长度不对', trigger: 'blur'}
                    ],
                    houseMode: [
                        {required: true, message: '请选择房屋类型', trigger: 'change'}
                    ],
                    housePlace: [
                        {required: true, message: '请填写装修地址', trigger: 'blur'}
                    ],
                    houseBuilding: [
                        {required: true, message: '请填写栋号', trigger: 'blur'}
                    ],
                    houseRoomNo: [
                        {required: true, message: '请填写房间号', trigger: 'blur'}
                    ],
                    prepareOrgId: [
                        {required: true, message: '请填写部门', trigger: 'blur'}
                    ],
                    prepareEmployeeId: [
                        {required: true, message: '请选择申报人', trigger: 'change'}
                    ],
                    enterEmployeeId: [
                        {required: true, message: '请填写录入人', trigger: 'blur'}
                    ],
                    custProperty: [
                        {required: true, message: '请选择客户属性', trigger: 'change'}
                    ],
                }
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
                axios.get('api/customer/cust-preparation/loadPreparationHistory?mobileNo=' + this.customerPreparation.mobileNo).then(function (responseData) {
                    vm.custOrder = responseData.data.rows;
                }).catch(function (error) {
                    console.log(error);
                });
            },

            changeCustomerProperty: function (newVal) {

                if (newVal == 2) {
                    this.isRefereeFixPlaceDisable = false;
                    this.isRefereeNameDisable = false;
                    this.isRefereeMobileNoDisable = false;
                }else{
                    this.isRefereeFixPlaceDisable = true;
                    this.isRefereeNameDisable = true;
                    this.isRefereeMobileNoDisable = true;
                }
            },

            submit(customerPreparation) {
                this.$refs.customerPreparation.validate((valid) => {
                    if (valid) {
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
                    }
                })
            },

        }
    });
    vm.loadEmployee();
    return vm;
})