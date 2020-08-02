require(['vue', 'ELEMENT', 'ajax', 'vxe-table', 'vueselect', 'org-orgtree', 'house-select', 'util', 'cust-info', 'order-info', 'order-worker', 'order-search-employee', 'order-selectemployee','order-selectdecorator'], function (Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util, custInfo, orderInfo, orderWorker, orderSearchEmployee,orderSelectEmployee,orderSelectDecorator) {
    Vue.use(table);
    let vm = new Vue({
            el: '#app',
            data: function () {
                return {
                    supplyOrderDetailDetailList : [],
                    supplyOrderDetail: false,
                    voucherTab:'',
                    materialTab:'',
                    constructionTab: '',
                    otherTab: '',
                    materialTableData: [],
                    materialDetailTable:'',
                    materialDetailTableData: [],
                    custOrder: [],
                    financeItem: {},
                    financeItems: [],
                    financeItemptions: [],
                    selectedDecorator: '',
                    decoratorOptions: [],
                    constructionTableData: [],
                    show: 'display:none',
                    queryCondForMaterial: {
                        count: 0,
                        limit: 20,
                        page: 1
                    },
                    queryConstructionCond: {
                        count: 0,
                        limit: 20,
                        page: 1
                    },
                    dialogVisible: false,
                    constructionInfo: {
                        custId: util.getRequest('custId'),
                        orderId: util.getRequest('orderId'),
                    },
                    auditRemark: "",
                    auditStatus: "",
                    validRules: {
                        money: [
                            {type: 'number', message: '金额必须为数字'},
                            {pattern: /^[0-9]+(\.\d+)?$/, message: '金额必须为正数'}
                        ]
                    },
                    checkPay: (rule, value, callback) => {
                        if (!value) {
                            callback(new Error('请输入收款金额'));
                        }
                        if (!/^[0-9]+(\.\d+)?$/.test(value)) {
                            callback(new Error('金额必须为正数'));
                        }
                        callback();
                    },
                    avatarUrl: 'static/img/male.jpg'
                }
            },
            // mounted(){
            //     this.$nextTick(()=>{
            //         this.init();
            //     })
            // },
            methods: {
                pickerOptions: {
                    shortcuts: [{
                        text: '最近一周',
                        onClick(picker) {
                            const end = util.getNowDate();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                            let begin = util.formatDate(start, "YYYY-MM-DD");
                            picker.$emit('pick', [begin, end]);
                        }
                    }, {
                        text: '最近一个月',
                        onClick(picker) {
                            const end = util.getNowDate();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                            let begin = util.formatDate(start, "YYYY-MM-DD");
                            picker.$emit('pick', [begin, end]);
                        }
                    }, {
                        text: '最近三个月',
                        onClick(picker) {
                            const end = util.getNowDate();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                            let begin = util.formatDate(start, "YYYY-MM-DD");
                            picker.$emit('pick', [begin, end]);
                        }
                    }]
                },
                showCustQuery: function () {
                    this.dialogVisible = true;
                },
                init() {
                },

                querySupplyInfo: function () {
                    let that = this;
                    ajax.get('api/bss.supply/supply-order/querySupplyInfo', this.queryCondForMaterial, function (responseData) {
                        that.materialTableData = responseData.records;
                        that.queryCondForMaterial.page = responseData.current;
                        that.queryCondForMaterial.count = responseData.total;
                    });
                },
                queryConstruction: function () {
                    let that = this;
                    ajax.get('api/bss.order/finance/queryCustOrderInfo', this.queryConstructionCond, function (responseData) {
                        that.custOrder = responseData.records;
                        that.queryConstructionCond.page = responseData.current;
                        that.queryConstructionCond.count = responseData.total;
                    });
                },
                toSupplyDetail: function(id,supplierId) {
                    this.supplyOrderDetail=true;
                    let data = {
                        id : id,
                        supplierId:supplierId
                    };
                    ajax.get('api/bss.supply/supply-order/querySupplyDetailInfo', data, (responseData)=>{
                        this.supplyOrderDetailDetailList = responseData;
                    });

                },
                auditSupplyDetail: function() {
                    let data = this.$refs.materialTable.getCheckboxRecords();
                    if (data == null || data.length <= 0) {
                        this.$message.error('没有选中任何记录，无法提交');
                        return;
                    }
                    ajax.post('api/bss.supply/supply-order/auditSupplyDetail', this.$refs.materialTable.getCheckboxRecords());
                },
                voucherPreparationForSupply : function() {
                    let data = this.$refs.materialTable.getCheckboxRecords();
                    if (data == null || data.length <= 0) {
                        this.$message.error('没有选中任何记录，无法提交');
                        return;
                    }
                    ajax.post('api/finance/finance-voucher/voucherPreparationForSupply', this.$refs.materialTable.getCheckboxRecords());
                },
                voucherPreparationForConstruction : function() {
                    let data = this.$refs.materialTable.getCheckboxRecords();
                    if (data == null || data.length <= 0) {
                        this.$message.error('没有选中任何记录，无法提交');
                        return;
                    }
                    ajax.post('api/finance/finance-voucher/voucherPreparationForSupply', this.$refs.materialTable.getCheckboxRecords());
                },
                //查询当前流程的施工人员情况,后续应该改为直接从工人预计工资表去获取，根据不同阶段最多领款额度算出来
                loadConstructionInfo: function (orderId, custId) {
                    this.constructionInfo.custId = custId;
                    this.constructionInfo.orderId = orderId;
                    this.dialogVisible = false;
                    this.show = 'display:block';
                    let that = this;
                    ajax.get('api/bss.order/decorator/selectTypeDecorator', '', function (responseData) {
                        that.decoratorOptions = responseData;
                    });
                },
                selectedDecoratorChange: function (item) {
                    let spans = item.split('-');
                    let isExist = false;
                    vm.constructionTableData.forEach(function (element, index, array) {
                        if (spans[0] == element.decoratorId) {
                            isExist = true;
                        }
                    });
                    if (!isExist) {
                        vm.constructionTableData.push({
                            decoratorId: spans[0],
                            name: spans[1],
                            decoratorType: spans[2]
                        });
                    }
                },
                handleConstructionTableDataDelete: function (index) {
                    vm.constructionTableData.splice(index, 1);
                },
                loadFinancenItem() {
                    let that = this;
                    let url = 'api/finance/finance-item/initFinancenItem';
                    ajax.get(url, null, function (data) {
                        that.financeItemptions = data.collectionItemOption;
                        if (data.payItems) {
                            that.payItems = data.payItems;
                        }
                        that.auditStatus = data.auditStatus;
                        that.payForm.needPay = data.needPay;

                        if (data.payDate) {
                            that.payForm.payDate = data.payDate;
                        }
                    });
                },
                isModify: function(obj) {
                    if (obj.row.isModified == '1') {
                        return "modify_row";
                    } else if (obj.row.isModified == '0') {
                        return 'new_row';
                    } else if (obj.row.isModified == '2') {
                        return 'delete_row';
                    }
                },

                activeRowMethod ({ row, rowIndex }) {
                    if (row.auditStatus == '1') {
                        return false;
                    }

                    return true;
                },

                checkMethod: function({row}) {

                   // return row.auditStatus == '1';
                },
                popupDialog() {
                    this.dialogVisible = true;
                },

                getSubmitData() {
                    let payDate = this.payForm.payDate;
                    let needPay = this.payForm.needPay;
                    let data = {
                        payDate: payDate,
                        needPay: needPay,
                        payItems: [],
                        payments: [],
                        auditRemark: this.auditRemark,
                        auditStatus: this.auditStatus,
                        payNo: this.payNo


                    };
                    for (let i = 0; i < this.payItems.length; i++) {
                        let payItem = {};

                        payItem.payItemId = this.payItems[i].payItemId.split("_")[1];
                        payItem.subjectId = this.payItems[i].subjectId.split("_")[1];
                        payItem.projectId = this.payItems[i].projectId.split("_")[1];
                        payItem.money = this.payItems[i].money;
                        data.payItems.push(payItem);
                    }

                    for (let i = 0; i < this.payments.length; i++) {
                        let payment = {};
                        payment.paymentType = this.payments[i].paymentType;
                        payment.money = this.payments[i].money;
                        data.payments.push(payment);
                    }
                    return data;
                },
                submit: async function () {
                    let isValid = await this.valid().then(isValid => isValid);
                    if (isValid) {
                        let data = this.getSubmitData();
                        ajax.post('api/bss.order/finance/nonCollectFee', data);
                    }
                },
                submitForUpdate: async function () {
                    let isValid = await this.valid().then(isValid => isValid);
                    if (isValid) {
                        let data = this.getSubmitData();
                        ajax.post('api/bss.order/finance/nonCollectFeeUpdate', data);
                    }
                },
                submitForAudit: async function () {
                    let isValid = await this.valid().then(isValid => isValid);
                    if (isValid) {
                        let data = this.getSubmitData();
                        data.auditStatus = "1";
                        ajax.post('api/bss.order/finance/nonCollectFeeForAudit', data);
                    }
                },
                submitForNo: async function () {
                    let isValid = await this.valid().then(isValid => isValid);
                    if (isValid) {
                        let data = this.getSubmitData();
                        data.auditStatus = "2";
                        ajax.post('api/bss.order/finance/nonCollectFeeForAudit', data);
                    }
                },
                submitForReview: async function () {
                    let isValid = await this.valid().then(isValid => isValid);
                    if (isValid) {
                        let data = this.getSubmitData();
                        data.auditStatus = "0";
                        ajax.post('api/bss.order/finance/nonCollectFeeForAudit', data);
                    }
                },
            }
        })
    ;
    //vm.init();
    return vm;
})