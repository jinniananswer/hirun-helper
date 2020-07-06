require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util', 'cust-info', 'order-info', 'order-worker', 'order-selectemployee', 'cust-visit'], function (Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee, custVisit) {
    let vm = new Vue({
        el: '#app',
        data: function () {
            return {
                collectFee: {
                    orderId:util.getRequest('orderId'),
                    engineeringClerk:'',
                    auditRemark:'',
                    auditStatus:0,
                    orderStatus :util.getRequest('status'),
                    custId:util.getRequest('custId')
                },
                payItems: [],
                progress: [-10, 70],
                activeTab: 'orderInfo',
                // rules: {
                //     engineeringClerk: [
                //         {required: true, message: '请选择工程文员', trigger: 'change'}
                //     ],
                // },

                marks: {
                    0: '酝酿',
                    10: '初选',
                    30: '初步决策',
                    50: '决策',
                    60: '施工',
                    95: '维护'
                },

                requirement: {
                    title: '客户需求信息',
                    style: '白色简约',
                    func: '功能列表'
                },

                avatarUrl: 'static/img/male.jpg'
            }
        },

        methods: {
            init() {
                let that = this;
                let url = 'api/bss/order/order-fee/initCostAudit';
                if (this.collectFee.orderId != null&& this.collectFee.orderStatus != null) {
                    url += '?orderId=' + this.collectFee.orderId+ '&orderStatus=' + this.collectFee.orderStatus;
                }
                ajax.get(url, null, function(data) {
                    if (data.payItems) {
                        that.payItems = data.payItems;
                    }
                });
            },

            save(collectFee) {
                // this.$refs.collectFee.validate((valid) => {
                //     if (valid) {
                //         ajax.post('api/order/order-consult/saveCustomerConsultInfo', this.collectFee);
                //     }
                // })
            },


            submitAudit(collectFee) {
                this.collectFee['auditStatus'] = "1";
                if(this.collectFee.orderStatus=="18"){
                    if(this.collectFee.engineeringClerk==""){
                        this.$message.error('工程文员没有选择，请亲重新选择哦~~~~~~~！');
                        return ;
                    }
                }
                    this.$refs.collectFee.validate((valid) => {
                        if (valid) {
                            this.$confirm('执行操作【提交审核】, 是否继续?', '提示', {
                                confirmButtonText: '确定',
                                cancelButtonText: '取消',
                                type: 'warning'
                            }).then(() => {

                                ajax.post('api/bss/order/order-fee/submitAudit', this.collectFee);
                            })
                        }
                    })

            },

            auditFailed(collectFee) {
                this.collectFee['auditStatus'] = "2";
                this.$refs.collectFee.validate((valid) => {
                    if (valid) {
                        this.$confirm('执行操作【提交审核不通过】, 是否继续?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            ajax.post('api/bss/order/order-fee/submitAudit', this.collectFee);
                        })
                    }
                })
            },
        },

        mounted () {
            this.init();
        }
    });

    return vm;
})