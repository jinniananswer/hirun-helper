require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-selectemployee','cust-visit', 'order-search-employee','order-discount-item'], function(Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee,custVisit, orderSearchEmployee,orderDiscountItem) {
    let vm = new Vue({
        el: '#app',
        data() {
            return {
                lastInstallment: {
                    orderId:'30',
                    chargedAllFee:'0',
                    chargedLastFee:'0',
                    chargedMaterialFee:'0',
                    chargedCupboardFee:'0',
                    woodProductFee:'',
                    baseDecorationFee:'',
                    doorFee:'0',
                    taxFee:'0',
                    furnitureFee:'0',
                    otherFee:'0',
                },
                workerSalary: {
                    orderId:'30',
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
                orderId : util.getRequest('orderId'),
                custId : util.getRequest('custId'),

                //orderStatus : util.getRequest('status'),
                lastInstallmentRules : {
                    doorFee: [
                        {required: true, message: '请填写门总金额', trigger: 'blur'},
                    ],
                    furnitureFee: [
                        {required: true, message: '请填写家具总金额', trigger: 'blur'},
                    ],
                },
            }
        },
        computed: {
            getWoodProductFee: {
                get() {
                    let woodProductFee = parseFloat(this.lastInstallment.doorFee) + parseFloat(this.lastInstallment.furnitureFee)
                    this.lastInstallment.woodProductFee = woodProductFee;
                    return woodProductFee;
                }
            },
            getBaseDecorationFee : {
                get() {
                    let baseDecorationFee = parseFloat(this.lastInstallment.chargedAllFee)-parseFloat(this.lastInstallment.woodProductFee)
                        -parseFloat(this.lastInstallment.taxFee)-parseFloat(this.lastInstallment.otherFee);
                    this.lastInstallment.baseDecorationFee = baseDecorationFee;
                    return baseDecorationFee;
                }
            },
        },

        mounted: function() {
            this.init();
        },
        methods: {

            init:function(){
                let that = this;
                ajax.get('api/bss/order/order-fee/queryLastInstallmentCollect', {orderId:this.lastInstallment.orderId}, function(data) {
                    Object.assign(that.workerSalary, data.workerSalaryDTO);
                    Object.assign(that.lastInstallment, data.lastInstallmentInfoDTO);
                })
            },

            save : function() {
                axios({
                    method: 'post',
                    url: 'api/bss/order/order-fee/saveLastInstallmentCollect',
                    data: {
                        "lastInstallmentInfoDTO": this.lastInstallment,
                        "workerSalaryDTO": this.workerSalary,
                    }
                }).then(function (responseData) {
                    if (0 == responseData.data.code) {
                        vm.$confirm('点击确定按钮刷新本页面，点击关闭按钮关闭本界面', '操作成功', {
                            confirmButtonText: '确定',
                            type: 'success',
                            center: true
                        }).then(() => {
                            document.location.reload();
                        }).catch(() => {
                            top.layui.admin.closeThisTabs();
                        });
                    }
                }).catch(function (error) {
                    console.log(error);
                    Vue.prototype.$message({
                        message: '保存失败！',
                        type: 'warning'
                    });
                });

/*                this.$refs['lastInstallment'].validate((valid) => {
                    if (valid) {
                        let url = 'api/bss/order/order-fee/saveLastInstallmentCollect';
                        let data = {
                            "lastInstallmentInfoDTO": this.lastInstallment,
                            "workerSalaryDTO": this.workerSalary,
                        };
                        ajax.post(url, data);
                    } else {
                        return false;
                    }
                });*/
            },

            applyFinanceAuditLast:function () {
                axios({
                    method: 'post',
                    url: 'api/bss/order/order-fee/applyFinanceAuditLast',
                    data: {
                        "lastInstallmentInfoDTO": this.lastInstallment,
                        "workerSalaryDTO": this.workerSalary,
                    }
                }).then(function (responseData) {
                    if (0 == responseData.data.code) {
                        vm.$alert('点击确定关闭本页面', '操作成功', {
                            confirmButtonText: '确定',
                            callback: action => {
                                top.layui.admin.closeThisTabs();
                            }
                        });
                    }
                }).catch(function (error) {
                    console.log(error);
                    Vue.prototype.$message({
                        message: '保存失败！',
                        type: 'warning'
                    });
                });
            }

        }
    });

    return vm;

})