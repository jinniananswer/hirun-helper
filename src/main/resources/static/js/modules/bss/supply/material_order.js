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
                selectedMaterial: '',
                materialOptions: [],
                employeeTableData: [],
                serviceGuaranteeInfo: {
                    id:'',
                    customerEmployeeName: '',
                    designEmployeeName: '',
                    projectEmployeeName:'',
                    checkTime: '',
                    hydropowerName: '',
                    woodworkerName: '',
                    inlayName:'',
                    paintName:'',
                    createTime:util.getNowTime(),
                    guaranteeStartDate:'',
                    guaranteeEndDate:'',
                    custId: util.getRequest('custId'),
                    orderId: util.getRequest('orderId'),
                },
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

            init: function () {
                this.show = 'display:none';
                this.queryShow = 'display:block';
                this.materialOptions=[
                    {'id': '1', 'materialName': '鸿扬','materialType': '木材', 'materialUnit': '1'},
                    {'id': '2', 'materialName': '鸿扬','materialType': '石头', 'materialUnit': '1'},
                    {'id': '3', 'materialName': '万象','materialType': '石头', 'materialUnit': '1'},
                    {'id': '4', 'materialName': '蒋庄','materialType': '油漆', 'materialUnit': '1'},
                ];
                // axios.get('api/organization/employee/loadEmployee').then(function (res) {
                //     vm.materialOptions = res.data.rows;
                // }).catch(function (error) {
                //     console.log(error);
                // });
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
                this.serviceGuaranteeInfo.custId = custId;
                this.serviceGuaranteeInfo.orderId = orderId;
                this.dialogVisible = false;
                this.show = 'display:block';
                let that = this;
                ajax.get('api/bss.service/ServiceGuarantee/queryCustomerGuaranteeInfo', {orderId: this.serviceGuaranteeInfo.orderId}, function(responseData) {
                    that.serviceGuaranteeInfo=responseData;
                });
            },

            save : function() {
                if(this.serviceGuaranteeInfo.guaranteeStartDate==''){
                    this.$message.error('保修开始时间不能为空');
                    return false;
                }
                if(this.serviceGuaranteeInfo.guaranteeEndDate==''){
                    this.$message.error('保修结束时间不能为空');
                    return false;
                }
               // ajax.post('api/bss.service/ServiceGuarantee/saveGuaranteeInfo', this.serviceGuaranteeInfo,null,null,true);
            },
            selectedMaterialChange: function (item) {
                let spans = item.split('-');
                let isExist = false;
                vm.employeeTableData.forEach(function (element, index, array) {
                    if (spans[0] == element.userId) {
                        isExist = true;
                    }
                });
                if (!isExist) {
                    vm.employeeTableData.push({
                        id: spans[0],
                        materialName: spans[1],
                        materialType: spans[2],
                        materialUnit: spans[3],
                    });
                }
            },


        },

        mounted () {
            this.init();
        }
    });

    return vm;
});