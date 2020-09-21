require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util', 'cust-info', 'order-info', 'order-worker', 'cust-visit'], function (Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, custVisit) {
    let vm = new Vue({
        el: '#app',
        data: function () {
            return {
                escape: {
                    escapeNode: '',
                    escapeNodeName: '',
                    escapeType: '',
                    escapeTrend: '',
                    escapeCause: '',
                    escapeTime: util.getNowDate(),
                    escapeRemark: '',
                    orderId: util.getRequest('orderId'),
                    custId: util.getRequest('custId'),
                    id: '',
                    status: ''
                },


                rules: {
                    escapeType: [
                        {required: true, message: '请选择跑单类型', trigger: 'change'}
                    ],
                    escapeTrend: [
                        {required: true, message: '请选择跑单去向', trigger: 'change'}
                    ],
                    escapeCause: [
                        {required: true, message: '请选择跑单原因', trigger: 'change'}
                    ],
                    escapeTime: [
                        {required: true, message: '跑单时间不能为空', trigger: 'blur'}
                    ],
                },

            }
        },

        methods: {
            init: function () {
                let that = this;
                ajax.get('api/order/order-escape/getEscapeInfo', {orderId: this.escape.orderId}, function (data) {
                    Object.assign(that.escape, data);
                })
            },

            save(escape) {
                this.$refs.escape.validate((valid) => {
                    if (valid) {
                        ajax.post('api/order/order-escape/save', this.escape,null,null,true);
                    }
                })
            },

            submitDirectorAudit(escape) {
                this.$refs.escape.validate((valid) => {
                    if (valid) {
                        this.$confirm('执行操作【提交主管审核】, 是否继续?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            ajax.post('api/order/order-escape/submitDirectorAudit', this.escape);
                        })
                    }
                })
            },

            submitBack(escape) {
                this.$confirm('执行操作【非跑单,返回上一个节点】, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    ajax.post('api/order/order-escape/submitBack', this.escape);
                })
            },
        },

        mounted() {
            this.init();
        }
    });

    return vm;
})