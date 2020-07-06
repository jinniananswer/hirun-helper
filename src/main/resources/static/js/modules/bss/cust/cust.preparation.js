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
                    isNetPrepare:'0',
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
                houseIdDisabled:false,
                isContinueAuth:'',
                moreCustomer:false,
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
/*                    houseId: [
                        {required: true, message: '请填写装修地址', trigger: 'blur'}
                    ],*/
/*                    houseBuilding: [
                        {required: true, message: '请填写栋号', trigger: 'blur'}
                    ],
                    houseRoomNo: [
                        {required: true, message: '请填写房间号', trigger: 'blur'}
                    ],*/
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

            loadCustomerInfo: function () {
                let that = this;
                if (that.customerPreparation.mobileNo == '') {
                    return;
                }
                ajax.get('api/customer/cust-base/queryCustomerInfoByMobile', {mobileNo: this.customerPreparation.mobileNo}, function (responseDate) {
                    if (responseDate != null && responseDate.length > 0) {
                        that.dialogTableVisible = true;
                        that.moreCustomer=true;
                        vm.custInfo = responseDate;
                    }else{
                        that.moreCustomer=false;
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
                if(this.isContinueAuth=='false'&&this.customerPreparation.custId==''&&this.moreCustomer){
                    this.$message.error('该号码存在多条客户信息，请选择客户做新增报备。如为特殊情况，请联系文员进行新增操作');
                    return;
                }

                this.$refs.customerPreparation.validate((valid) => {
                    if (valid) {
                        if (this.checkReferee()) {
                            if(this.checkHouseIdReq()){
                                ajax.post('api/customer/cust-preparation/addCustomerPreparation', this.customerPreparation,null,null,true);
                            }else {
                                this.$message.error('请选择楼盘地址');
                            }
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

            checkHouseIdReq() {
                if (this.customerPreparation.houseId != '') {
                    return true;
                }else{
                    ajax.get('api/customer/cust-preparation/validIsNetOrg', {prepareEmployeeId: this.customerPreparation.prepareEmployeeId}, function (responseDate) {
                        if (responseDate==true) {
                            this.customerPreparation.isNetPrepare='1';
                            return true;
                        }else{
                            return false;
                        }
                    });
                }
                return false;
            },

            handle(row) {
                this.customerPreparation.custName = row.custName;
                this.customerPreparation.houseId = row.houseId;
                this.customerPreparation.houseMode = row.houseMode;
                this.customerPreparation.houseArea = row.houseArea;
                this.customerPreparation.houseBuilding = row.houseBuilding;
                this.customerPreparation.houseRoomNo = row.houseRoomNo;
                this.customerPreparation.custNo=row.custNo;
                this.customerPreparation.prepareId=row.prepareId;

                this.dialogTableVisible = false;
                this.mobileReadonly=true;
                this.custNameReadonly=true;
                this.houseModeReadonly=true;
                this.houseIdDisabled=true;
                this.customerPreparation.custId=row.custId;
                this.loadPrepareHistory();

            },

            loadPrepareHistory(){
                let that = this;
                ajax.get('api/customer/cust-preparation/loadPreparationHistory', {mobileNo: this.customerPreparation.mobileNo}, function (responseDate) {
                    that.custOrder=responseDate;
                });
            }
        },

        mounted () {
            this.init();
        }
    });
    return vm;
})