require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util', 'house-select', 'order-selectemployee', 'order-search-employee'], function (Vue, element, axios, ajax, vueselect, util, houseSelect, orderSelectEmployee, orderSearchEmployee) {
    let vm = new Vue({
        el: '#customer_perparation',
        data: function () {
            return {
                customerPreparation: {
                    prepareOrgId: '',
                    prepareEmployeeId: '',
                    prepareEmployeeName: '',
                    prepareTime: util.getNowTime(),
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
                    houseId: '',
                    houseBuilding: '',
                    houseRoomNo: '',
                    prepareId:'',
                    custId:'',
                },
                custOrder: [],
                custInfo: [],
                isRefereeFixPlaceDisable: true,
                isRefereeNameDisable: true,
                isRefereeMobileNoDisable: true,
                enterDisabled: true,
                dialogTableVisible: false,
                mobileReadonly:false,
                custNameReadonly:false,
                houseModeReadonly:false,
                custReadonly:false,
                isContinueAuth:'',
                display: 'display:block',

                id: util.getRequest('id'),
                rules: {
                    custName: [
                        {required: true, message: '请填写客户姓名', trigger: 'blur'},
                    ],
                    mobileNo: [
                        {required: true, message: '请填写电话号码', trigger: 'blur'},
                        {min: 11, max: 11, message: '输入电话号码长度不对', trigger: 'blur'}
                    ],
                    houseMode: [
                        {required: true, message: '请选择房屋类型', trigger: 'change'}
                    ],
                    houseId: [
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

                },

            }
        },

        methods: {

            init(){
                let that=this;
                ajax.get('api/customer/cust-preparation/getCustomerNoAndSec', '', function (responseDate) {
                    that.isContinueAuth=responseDate.isContinueAuth;
                    that.customerPreparation.custNo=responseDate.custNo;
                });
            },

            loadPreparationHistory: function () {
                let that = this;
                if (that.customerPreparation.mobileNo == '') {
                    return;
                }
                ajax.get('api/customer/cust-base/queryCustomerInfoByMobile', {mobileNo: this.customerPreparation.mobileNo}, function (responseDate) {
                    if (responseDate != null && responseDate.length > 0) {
                        that.dialogTableVisible = true;
                        vm.custInfo = responseDate;
                    }
                });
            },

            changeCustomerProperty: function (newVal) {

                if (newVal == 2 || newVal == 3) {
                    this.isRefereeFixPlaceDisable = false;
                    this.isRefereeNameDisable = false;
                    this.isRefereeMobileNoDisable = false;
                } else {
                    this.isRefereeFixPlaceDisable = true;
                    this.isRefereeNameDisable = true;
                    this.isRefereeMobileNoDisable = true;
                }
            },

            submit(customerPreparation) {
                this.$refs.customerPreparation.validate((valid) => {
                    if (valid) {
                        if (this.checkReferee()) {
                            ajax.post('api/customer/cust-preparation/addCustomerPreparation', this.customerPreparation);
                        } else {
                            this.$message.error('客户属性为老客户介绍或者工地营销需填工地地址、客户姓名、客户电话');
                        }
                    }
                })
            },

            checkReferee() {
                if (this.customerPreparation.custProperty == 2 || this.customerPreparation.custProperty == 3) {
                    if (this.customerPreparation.refereeFixPlace == '' || this.customerPreparation.refereeMobileNo == '' || this.customerPreparation.refereeName == '') {
                        return false;
                    }
                }
                return true;
            },

            handle(row) {
                this.customerPreparation.custName = row.custName;
                this.customerPreparation.houseId = row.houseId;
                this.customerPreparation.houseMode = row.houseMode;
                this.customerPreparation.houseArea = row.houseArea;
                this.customerPreparation.houseBuilding = row.houseBuilding;
                this.customerPreparation.houseRoomNo = row.houseRoomNo;
                this.dialogTableVisible = false;
                this.custReadonly=true;
                this.mobileReadonly=true;
                this.customerPreparation.custId=row.custId;
            },
        },

        mounted () {
            this.init();
        }
    });
    return vm;
})