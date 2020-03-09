require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util', 'cust-info', 'order-info', 'order-worker', 'order-selectemployee', 'cust-visit'], function (Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee, custVisit) {
    let vm = new Vue({
        el: '#app',
        data: function () {
            return {
                uploadGarrisonDesign: {
                    startDate: util.getNowDate(),
                    endDate: util.getNowDate(),
                    designEmployeeId:'',
                    orderId:'',
                    id:'',
                    custId:'',
                },
                progress: [-10, 70],
                activeTab: 'orderInfo',
                orderId : util.getRequest('orderId'),
                custId : util.getRequest('custId'),

                rules: {
                    designEmployeeId: [
                        {required: true, message: '请选择设计师', trigger: 'change'}
                    ],
                    startDate: [
                        {required: true, message: '请填入开始时间', trigger: 'blur'}
                    ],
                    endDate: [
                        {required: true, message: '请填入结束时间', trigger: 'blur'}
                    ],
                },

            }
        },

        methods: {
            init:function(){
                let that = this;
                that.uploadGarrisonDesign.orderId=that.orderId;
                ajax.get('api/bss.order/order-garrison-design/queryGarrisonDesignInfo', {orderId:this.uploadGarrisonDesign.orderId}, function(data) {
                    that.uploadGarrisonDesign=data;
                })
            },

            save(uploadGarrisonDesign) {
                this.$refs.uploadGarrisonDesign.validate((valid) => {
                    if (valid) {
                        ajax.post('api/bss.order/order-garrison-design/saveGarrisonDesignInfo', this.uploadGarrisonDesign,null,null,true);
                    }
                })
            },

            submitBudget(uploadGarrisonDesign) {
                this.$refs.uploadGarrisonDesign.validate((valid) => {
                    if (valid) {
                        this.$confirm('执行操作【提交预算】, 是否继续?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            ajax.post('api/bss.order/order-garrison-design/submitBudget', this.uploadGarrisonDesign);
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