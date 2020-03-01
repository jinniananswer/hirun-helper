require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util', 'cust-info', 'order-info', 'order-worker', 'order-selectemployee', 'cust-visit'], function (Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee, custVisit) {
    let vm = new Vue({
        el: '#app',
        data: function () {
            return {
                task: {
                    orderId:util.getRequest('orderId'),
                    engineeringSupervisor:'',
                    projectManager:'',
                    engineeringAssistant:'',
                    auditRemark:'',
                    auditStatus:0,
                    orderStatus : util.getRequest('status'),
                    custId:util.getRequest('custId')
                },
                progress: [-10, 70],
                activeTab: 'orderInfo',
                rules: {
                    engineeringSupervisor: [
                        {required: true, message: '请选择项目主管', trigger: 'change'}
                    ],
                    projectManager: [
                        {required: true, message: '请选择项目经理', trigger: 'change'}
                    ],
                    // engineeringAssistant: [
                    //     {required: true, message: '请选择工程助理', trigger: 'change'}
                    // ],
                },
                projectrules: {

                    engineeringAssistant: [
                        {required: true, message: '请选择工程助理', trigger: 'change'}
                    ],
                },

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
            init:function(){
                // let that = this;
                // ajax.get('api/order/order-fee/queryOrderCollectFee', {orderId:this.collectFee.orderId}, function(data) {
                //     Object.assign(that.customerConsult, data);
                // })
            },

            save(task) {
                // this.$refs.task.validate((valid) => {
                //     if (valid) {
                //         ajax.post('api/order/order-consult/saveCustomerConsultInfo', this.task);
                //     }
                // })
            },


            submitTask(task) {
                    this.$refs.task.validate((valid) => {
                        if (valid) {
                            this.$confirm('执行操作【提交】, 是否继续?', '提示', {
                                confirmButtonText: '确定',
                                cancelButtonText: '取消',
                                type: 'warning'
                            }).then(() => {
                                 ajax.post('api/bss/order/order-fee/submitTask', this.task);
                            })
                        }
                    })

            },

            submitAudit(task) {
                this.task['auditStatus'] = "1";
                this.$refs.task.validate((valid) => {
                    if (valid) {
                        this.$confirm('执行操作【提交】, 是否继续?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            ajax.post('api/bss/order/order-fee/submitAuditProject', this.task);
                        })
                    }
                })

            },
            auditFailed(task) {
                this.task['auditStatus'] = "2";
                this.$refs.task.validate((valid) => {
                    if (valid) {
                        this.$confirm('执行操作【提交审核不通过】, 是否继续?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            ajax.post('api/bss/order/order-fee/submitAuditProject', this.task);
                        })
                    }
                })
            },
            submit(task) {
                this.$refs.task.validate((valid) => {
                    if (valid) {
                        this.$confirm('执行操作【提交】, 是否继续?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            ajax.post('api/bss/order/order-fee/submitAssignment', this.task);
                        })
                    }
                })

            },
        },

        // mounted () {
        //     this.init();
        // }
    });

    return vm;
})