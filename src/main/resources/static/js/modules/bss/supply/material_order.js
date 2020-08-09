require(['vue', 'ELEMENT', 'ajax', 'vxe-table', 'vueselect', 'org-orgtree', 'house-select', 'util', 'cust-info', 'order-info', 'order-worker', 'order-search-employee'], function (Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util, custInfo, orderInfo, orderWorker, orderSearchEmployee) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function () {
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
                supplyOrderType: '',
                materialOptions: [],
                materialTableData: [],
                validRules: {
                    materialNum: [
                        {type: 'number', message: '数量必须为数字'},
                        {pattern: /^[0-9]+(\.\d+)?$/, message: '数量必须为正数'}
                    ]
                },
                MaterialInfo: {
                    custId: util.getRequest('custId'),
                    orderId: util.getRequest('orderId'),
                },
                dialogVisible: false,
                activeTab: 'designRoyaltyTab',
                custOrder: [],
                show: 'display:none',
                rules: {
                    guaranteeStartDate: [
                        {required: true, message: '保修开始时间不能为空', trigger: 'blur'}
                    ],
                    guaranteeEndDate: [
                        {required: true, message: '保修结束时间不能为空', trigger: 'blur'}
                    ],
                },
            }
        },

        methods: {

            init: function () {
                this.show = 'display:none';
                this.queryShow = 'display:block';
                // this.materialOptions=[
                //     {'id': '1', 'materialName': '鸿扬','materialType': '木材', 'materialUnit': '1'},
                //     {'id': '2', 'materialName': '鸿扬','materialType': '石头', 'materialUnit': '1'},
                //     {'id': '3', 'materialName': '万象','materialType': '石头', 'materialUnit': '1'},
                //     {'id': '4', 'materialName': '蒋庄','materialType': '油漆', 'materialUnit': '1'},
                // ];

            },
            selectedMaterialChange: function (item) {
                let spans = item.split('-');
                let isExist = false;
                vm.materialTableData.forEach(function (element, index, array) {
                    if (spans[0] == element.id) {
                        isExist = true;
                    }
                });
                if (!isExist) {
                    vm.materialTableData.push({
                        id: spans[0],
                        materialName: spans[1],
                        supplierName: spans[2],
                        standard: spans[3],
                        materialUnit: spans[4],
                        costPrice: spans[5],
                        supplierId: spans[7]
                    });
                }
            },

            query: function () {
                let that = this;
                ajax.get('api/bss.order/finance/queryCustOrderInfo', this.queryCond, function (responseData) {
                    that.custOrder = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;
                });
            },

            showCustQuery: function () {
                this.dialogVisible = true;
            },

            loadMaterial: function (orderId, custId) {
                this.MaterialInfo.custId = custId;
                this.MaterialInfo.orderId = orderId;
                this.dialogVisible = false;
                this.show = 'display:block';
                let that = this;
                ajax.get('api/bss.supply/SupplyMaterial/loadMaterial', '', function (responseData) {
                    that.materialOptions = responseData;
                });
            },
            handleMaterialTableDelete: function (index) {
                vm.materialTableData.splice(index, 1);
            },
            save: function () {
                if (this.materialTableData == '') {
                    this.$message.error('没有新增材料下单，不能提交');
                    return false;
                }
                let data = {
                    orderId: this.MaterialInfo.orderId,
                    supplyOrderType: "1",
                    supplyMaterial: []
                };
                for (let i = 0; i < this.materialTableData.length; i++) {
                    let material = {};
                    material.id = this.materialTableData[i].id;
                    if (this.materialTableData[i].materialNum == null) {
                        this.$message.error('没有输入材料数量，不能提交');
                        return false;
                    }
                    material.materialNum = this.materialTableData[i].materialNum;
                    material.costPrice = this.materialTableData[i].costPrice;
                    material.supplierId = this.materialTableData[i].supplierId;
                    data.supplyMaterial.push(material);
                }
                ajax.post('api/bss.supply/supply-order/materialOrderDeal', data, null, null, true);
            },

            saveForOther: function () {
                if (this.materialTableData == '') {
                    this.$message.error('没有新增材料下单，不能提交');
                    return false;
                }
                let data = {
                    orderId: this.MaterialInfo.orderId,
                    supplyOrderType: "2",
                    supplyMaterial: []
                };
                for (let i = 0; i < this.materialTableData.length; i++) {
                    let material = {};
                    material.id = this.materialTableData[i].id;
                    material.num = this.materialTableData[i].num;
                    material.costPrice = this.materialTableData[i].costPrice;
                    data.supplyMaterial.push(material);
                }
                ajax.post('api/bss.supply/supply-order/materialOrderDeal', data, null, null, true);
            },


        },

        mounted() {
            this.init();
        }
    });

    return vm;
});