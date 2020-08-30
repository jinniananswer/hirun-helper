require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util', 'cust-info', 'order-info', 'order-worker', 'order-selectemployee', 'cust-visit', 'vxe-table', 'order-search-employee', 'order-file-upload', 'xe-utils'], function (Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee, custVisit, vxetable, orderSearchEmployee, xeUtils) {
    Vue.use(vxetable);
    let vm = new Vue({
        el: '#app',
        data() {
            return {
                orderInspect: {
                    orderId:null
                },
                orderId: util.getRequest('orderId'),
                orderStatus: util.getRequest('status'),
                custId: util.getRequest('custId'),
                submitButtonName: '',
                orderInspectRule: {
                    applyDate: [
                        {required: true, message: '请选择申报日期', trigger: 'change'}
                    ],
                    institution: [
                        {required: true, message: '请选择环保机构', trigger: 'change'}
                    ],
                    offerDate: [
                        {required: true, message: '请填写送达日期', trigger: 'change'}
                    ],

                },

            }
        },
        computed: {},
        mounted: function () {
            this.init();
            if (this.orderStatus == '41') {
                this.submitButtonName = '检测中';
            }

        },
        methods: {
            init: function () {
                let that = this;
                ajax.get('api/bss.order/order-inspect/queryOrderInspect', {orderId:this.orderInspect.orderId}, function (responseData) {
                    that.orderInspect = responseData;
                });
            },

            submit: function () {
                let that = this;
                this.$refs['orderInspect'].validate((valid) => {
                    if (valid) {
                        that.orderInspect.orderId=that.orderId;
                        ajax.post('api/bss.order/order-inspect/save',that.orderInspect,null,null,true);
                    } else {
                        return false;
                    }
                });
            },

            submit: function () {
                let that = this;
                this.$refs['orderInspect'].validate((valid) => {
                    if (valid) {
                        that.orderInspect.orderId=that.orderId;
                        ajax.post('api/bss.order/order-inspect/nextStep',that.orderInspect,null,null,true);
                    } else {
                        return false;
                    }
                });
            },
        }
    });

    return vm;

})