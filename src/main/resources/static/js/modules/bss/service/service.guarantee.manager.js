require(['vue','ELEMENT','ajax', 'vxe-table', 'vueselect', 'org-orgtree','house-select', 'util', 'cust-info', 'order-info', 'order-worker', 'order-search-employee'], function(Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util, custInfo, orderInfo, orderWorker, orderSearchEmployee) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                queryCond: {
                    custName: '',
                    sex: '',
                    mobileNo: '',
                    housesId: null,
                    orderStatus: '',
                    limit: 20,
                    page: 1,
                    count: null
                },
                serviceGuaranteeInfo: {
                },
                custId: util.getRequest('custId'),
                orderId: util.getRequest('orderId'),
                dialogVisible:false,
                activeTab:'designRoyaltyTab',
                custOrder:[],
                show: 'display:none',
                rules: {
                    guaranteeStartDate: [
                        { required: true, message: '保修开始时间不能为空', trigger: 'blur' }
                    ],
                    guaranteeEndDate: [
                        { required: true, message: '保修结束时间不能为空', trigger: 'blur' }
                    ],
                },
            }
        },

        methods: {
            init: function() {
                this.show = 'display:none';
                this.queryShow = 'display:block';
            },

            query: function() {
                let that = this;
                ajax.get('api/bss.order/finance/queryCustOrderInfo', this.queryCond, function(responseData){
                    that.custOrder = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;
                });
            },

            showCustQuery: function() {
                this.dialogVisible = true;
            },

            selectCustOrder: function(orderId, custId) {
                this.custId = custId;
                this.orderId = orderId;
                this.dialogVisible = false;
                this.show = 'display:block';
                let that = this;
                ajax.get('api/bss.service/service-guarantee/queryCustomerGuaranteeInfo', {orderId: that.orderId}, function(responseData) {
                    that.serviceGuaranteeInfo=responseData;
                });
            },

            save : function() {
/*                if(this.serviceGuaranteeInfo.guaranteeStartDate==''){
                    this.$message.error('保修开始时间不能为空');
                    return false;
                }
                if(this.serviceGuaranteeInfo.guaranteeEndDate==''){
                    this.$message.error('保修结束时间不能为空');
                    return false;
                }
                this.serviceGuaranteeInfo.orderId=this.orderId;
                this.serviceGuaranteeInfo.custId=this.custId;*/

                this.$refs.serviceGuaranteeInfo.validate((valid) => {
                    if (valid) {
                        this.serviceGuaranteeInfo.orderId=this.orderId;
                        this.serviceGuaranteeInfo.custId=this.custId;
                        ajax.post('api/bss.service/service-guarantee/saveGuaranteeInfo', this.serviceGuaranteeInfo,null,null,true);
                    }
                })

            },


        },

        mounted () {
            this.init();
        }
    });

    return vm;
});