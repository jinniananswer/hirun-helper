require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-selectemployee','cust-visit', 'order-search-employee','order-file-upload'], function(Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee,custVisit, orderSearchEmployee,orderFileUpload) {
    let vm = new Vue({
        el: '#app',
        data() {
            return {
                customerSettlement: {
                    orderId:'',
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

                workerSalary_1: {
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
                },

                workerSalary_2: {
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
                },
                workerSalary_3: {
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
                },

                orderId : util.getRequest("orderId"),
                custId : util.getRequest("custId"),
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
            this.customerSettlement.orderId=this.orderId;
            this.initOrderSettlement();
            this.initOrderWorkerSalary();
        },
        methods: {

            initOrderSettlement:function(){
                let that = this;
                ajax.get('api/bss.order/order-settlement/queryOrderSettlement', {orderId:this.customerSettlement.orderId}, function(data) {
                    Object.assign(that.customerSettlement, data);
                })
            },

            initOrderWorkerSalary:function(){
                let that=this;
                ajax.get('api/bss.order/order-worker-salary/queryAllWorkerSalary', {orderId:this.orderId}, function(data) {
                        Object.assign(that.workerSalary_1, data.workerSalary_1);
                        Object.assign(that.workerSalary_2, data.workerSalary_2);
                        Object.assign(that.workerSalary_3, data.workerSalary_3);
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
                        ajax.post(url,this.customerSettlement);
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
                        //todo 客户不结算，单子发往管理员
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