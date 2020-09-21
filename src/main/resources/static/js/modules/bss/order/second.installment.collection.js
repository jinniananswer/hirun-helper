require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-selectemployee','cust-visit', 'order-search-employee', 'vxe-table'], function(Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee,custVisit, orderSearchEmployee, vxetable) {
    Vue.use(vxetable);
    let vm = new Vue({
        el: '#app',
        data() {
            return {
                secondInstallment: {
                    chargedDecorateFee : '0',
                    orderId : '',
                    secondInstallmentFee : '0',
                    secondContractFee : '0',
                    chargedSecondInstallmentFee : '0',
                    woodProductFee : '0',
                    baseDecorationFee : '0',
                    doorFee : '0',
                    taxFee: '0',
                    furnitureFee : '0',
                    otherFee : '0',
                    hydropowerSalary : '0',
                    hydropowerRemark : '',
                    woodworkerSalary : '0',
                    woodworkerRemark : '',
                    tilerSalary : '0',
                    tilerRemark : '',
                    painterSalary : '0',
                    painterRemark : '',
                    wallworkerSalary : '0',
                    wallworkerRemark : '',
                    financeEmployeeId : '',
                    financeEmployeeName : ''
                },
                discountItemDetailList : [],
                approveEmployeeList : [],
                discountItemDetail: false,
                orderId : util.getRequest('orderId'),
                custId : util.getRequest('custId'),
                orderStatus : util.getRequest('status'),
                secondInstallmentRules : {
                    financeEmployeeId: [
                        { required: true, message: '请选择财务人员', trigger: 'change' }
                    ],
                    baseDecorationFee: [
                        {required: true, message: '请填写基础装修总金额', trigger: 'blur'},
                        // {type: 'number', message: '必须为数字', trigger: 'blur'}
                    ],
                    doorFee: [
                        {required: true, message: '请填写门总金额', trigger: 'blur'},
                        // {type: 'number', message: '必须为数字', trigger: 'blur'}
                    ],
                    furnitureFee: [
                        {required: true, message: '请填写家具总金额', trigger: 'blur'},
                        // {type: 'number', message: '必须为数字', trigger: 'blur'}
                    ],
                    otherFee: [
                        {required: true, message: '请填写其他费用', trigger: 'blur'},
                        // {type: 'number', message: '必须为数字', trigger: 'blur'}
                    ],
                    taxFee: [
                        {required: true, message: '请填写税金', trigger: 'blur'},
                        // {type: 'number', message: '必须为数字', trigger: 'blur'}
                    ],
                    financeEmployeeId : [
                        {required: true, message: '请选择财务人员', trigger: 'change'},
                    ]
                },
                // activities : []
            }
        },
        computed: {
            getWoodProductFee: {
                get() {
                    let woodProductFee = parseFloat(this.secondInstallment.doorFee) + parseFloat(this.secondInstallment.furnitureFee)
                    this.secondInstallment.woodProductFee = woodProductFee;
                    return woodProductFee;
                }
            },
            getSecondContractFee : {
                get() {
                    let secondContractFee = parseFloat(this.secondInstallment.woodProductFee) + parseFloat(this.secondInstallment.baseDecorationFee)
                        + parseFloat(this.secondInstallment.taxFee)
                        + parseFloat(this.secondInstallment.otherFee)
                    this.secondInstallment.secondContractFee = secondContractFee;
                    return secondContractFee;
                }
            },
            getSecondInstallmentFee : {
                get() {
                    let secondInstallmentFee = parseFloat(this.secondInstallment.secondContractFee) *0.95 - parseFloat(this.secondInstallment.chargedDecorateFee)
                    this.secondInstallment.secondInstallmentFee = secondInstallmentFee;
                    return secondInstallmentFee
                }
            }
        },
        mounted: function() {
            this.secondInstallment.orderId = this.orderId;
            let data = {
                orderId : this.orderId
            }
            ajax.get('api/bss.order/order-second-installment/getSecondInstallment', data, (responseData)=>{
                Object.assign(this.secondInstallment, responseData);
                this.secondInstallment.chargedDecorateFee = responseData.chargedDecorateFee ? responseData.chargedDecorateFee / 100 : 0;
                this.secondInstallment.baseDecorationFee = responseData.baseDecorationFee ? responseData.baseDecorationFee / 100 : 0;
                this.secondInstallment.furnitureFee = responseData.furnitureFee ? responseData.furnitureFee / 100 : 0;
                this.secondInstallment.doorFee = responseData.doorFee ? responseData.doorFee / 100 : 0;
                this.secondInstallment.taxFee = responseData.taxFee ? responseData.taxFee / 100 : 0;
                this.secondInstallment.otherFee = responseData.otherFee ? responseData.otherFee / 100 : 0;
            });

            ajax.get('api/bss.order/order-base/selectRoleEmployee', {roleId:19,isSelf:false}, (responseData)=>{
                let arr = [];
                for(let i = 0; i < responseData.length; i++) {
                    arr.push({
                        label: responseData[i].name,
                        value: responseData[i].employeeId
                    });
                }
                this.approveEmployeeList = arr;
            })
        },
        methods: {
            submit : function() {
                this.$refs['secondInstallment'].validate((valid) => {
                    if (valid) {
                        let url = 'api/bss.order/order-second-installment/secondInstallmentCollect';
                        let data = JSON.parse(JSON.stringify(this.secondInstallment));
                        this.yuanTransToFen(data);
                        ajax.post(url, data);
                    } else {
                        return false;
                    }
                });
            },
            openDiscountItemDetailDialog : function () {
                let data = {
                    orderId : this.orderId
                }
                ajax.get('api/bss.order/order-discount-item/list', data, (responseData)=>{
                    for(let i = 0; i < responseData.length; i++) {
                        responseData[i].contractDiscountFee = responseData[i].contractDiscountFee / 100;
                        responseData[i].settleDiscountFee = responseData[i].settleDiscountFee / 100;
                    }
                    this.discountItemDetailList = responseData;
                });
            },
            uploadDecorateContract : function () {
                alert('上传合同附件')
            },
            handleClick(tab, event) {
                console.log(tab, event);
            },
            yuanTransToFen : function(data) {
                data.secondInstallmentFee = Math.round(data.secondInstallmentFee * 100);
                data.secondContractFee = Math.round(data.secondContractFee * 100);
                data.chargedSecondInstallmentFee = Math.round(data.chargedSecondInstallmentFee * 100);
                data.woodProductFee = Math.round(data.woodProductFee * 100);
                data.baseDecorationFee = Math.round(data.baseDecorationFee * 100);
                data.doorFee = Math.round(data.doorFee * 100);
                data.furnitureFee = Math.round(data.furnitureFee * 100);
                data.otherFee = Math.round(data.otherFee * 100);
                data.taxFee = Math.round(data.taxFee * 100);
            },
            saveDiscountItemList : function () {
                let updateRecords = vm.$refs.discountItemTable.getUpdateRecords();
                if(updateRecords.length == 0) {
                    this.$alert('没有任何修改', '提示', {
                        confirmButtonText: '确定',
                    });
                    return;
                }
                let list = JSON.parse(JSON.stringify(updateRecords));//值传递
                for(let i = 0; i < list.length; i++) {
                    list[i].contractDiscountFee = list[i].contractDiscountFee * 100;
                    list[i].settleDiscountFee = list[i].settleDiscountFee * 100;
                }
                ajax.post('api/bss.order/order-discount-item/save', list, (responseData)=>{
                    this.$alert('保存成功', '提示', {
                        confirmButtonText: '确定',
                        callback: action => {
                            this.discountItemDetail = false;
                        }
                    });
                });
            }
        }
    });

    return vm;

})