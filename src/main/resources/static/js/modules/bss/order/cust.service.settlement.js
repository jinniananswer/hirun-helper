require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-selectemployee','cust-visit', 'order-search-employee','order-discount-item'], function(Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee,custVisit, orderSearchEmployee,orderDiscountItem) {
    let vm = new Vue({
        el: '#app',
        data() {
            return {
                customerSettlement: {
                    orderId:'30',
                    deferReason:'',
                    expectCheckDate:'',
                    expectLastfeeDate:'',
                    detailReason:'',
                    actualCheckDate:'',
                    checkEmployeeId:'',
                    sourceContractFee:'0',
                    changedItemFee:'0',
                    changedContractFee:'0',
                    payedMoney:'0',
                    needPayMoney:'',
                    adminCheckDate:'',
                    remark:'',
                    id:'',
                },

                workerSalary: {
                    hydropowerSalary: '',
                    hydropowerRemark: '',
                    woodworkerSalary: '',
                    woodworkerRemark:'',
                    tilerSalary: '',
                    tilerRemark: '',
                    painterSalary:'',
                    painterRemark:'',
                    wallworkerSalary:'',
                    wallworkerRemark:'',
                    orderId:'29',
                    id:'',
                    custId:'18163',
                },

                orderId : '30',
                custId : '18164',
                workSalaryShow:false,
                customerSettlementRules : {
                    expectCheckDate: [
                        {required: true, message: '请选择预计验收时间', trigger: 'blur'},
                    ],
                    expectLastDate: [
                        {required: true, message: '请选择预计尾款时间', trigger: 'blur'},
                    ],
                    expectLastDate: [
                        {required: true, message: '请选择预计尾款时间', trigger: 'blur'},
                    ],
                    actualCheckDate: [
                        {required: true, message: '请选择实际验收时间', trigger: 'blur'},
                    ],
                    checkEmployeeId: [
                        {required: true, message: '请选择验收负责人', trigger: 'change'},
                    ],
                },
            }
        },

        mounted: function() {
            this.init();
        },
        methods: {

            init:function(){
                let that = this;
                ajax.get('api/bss.order/order-settlement/queryOrderSettlement', {orderId:this.customerSettlement.orderId}, function(data) {
                    Object.assign(that.customerSettlement, data);
                })
            },

            save : function() {

                this.$refs['customerSettlement'].validate((valid) => {
                    if (valid) {
                        let url = 'api/bss.order/order-settlement/saveOrderSettlement';
                        ajax.post(url,this.customerSettlement,null,null,true);
                    } else {
                        return false;
                    }
                });
            },

            collectLastFee:function () {
                this.$refs['customerSettlement'].validate((valid) => {
                    this.$confirm('执行操作【已结算收取尾款】, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        let url = 'api/bss.order/order-settlement/submitCollectLastFee';
                        ajax.post(url,this.customerSettlement,null,null,true);
                    })
                });
            },

            orderNoSettlement:function () {
                this.$refs['customerSettlement'].validate((valid) => {
                    this.$confirm('执行操作【客户不结算】, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        //客户不结算，单子发往管理员
                        return false;
                    })
                });
            },

            showWorkerSalary:function () {
                this.workSalaryShow=true;

            }

        }
    });

    return vm;

})