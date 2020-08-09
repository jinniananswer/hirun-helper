require(['vue', 'ELEMENT', 'ajax', 'vxe-table', 'vueselect', 'org-orgtree', 'house-select', 'util', 'cust-info', 'order-info', 'order-worker', 'order-search-employee', 'order-selectemployee', 'order-selectdecorator'], function (Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util, custInfo, orderInfo, orderWorker, orderSearchEmployee, orderSelectEmployee, orderSelectDecorator) {
    Vue.use(table);
    let vm = new Vue({
            el: '#app',
            data: function () {
                return {
                    supplyOrderDetailDetailList: [],
                    supplyOrderDetail: false,
                    voucherTab: '',
                    materialTab: '',
                    constructionTab: '',
                    otherTab: '',
                    materialTableData: [],
                    materialDetailTable: '',
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
                    dialogOtherVisible: false,
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
                toSupplyDetail: function (id, supplierId) {
                    this.supplyOrderDetail = true;
                    let data = {
                        id: id,
                        supplierId: supplierId
                    };
                    ajax.get('api/bss.supply/supply-order/querySupplyDetailInfo', data, (responseData) => {
                        this.supplyOrderDetailDetailList = responseData;
                    });

                },
                auditSupplyDetail: function () {
                    let data = this.$refs.materialTable.getCheckboxRecords();
                    if (data == null || data.length <= 0) {
                        this.$message.error('没有选中任何记录，无法提交');
                        return;
                    }
                    ajax.post('api/bss.supply/supply-order/auditSupplyDetail', this.$refs.materialTable.getCheckboxRecords());
                },
                voucherPreparationForSupply: function () {
                    let data = this.$refs.materialTable.getCheckboxRecords();
                    if (data == null || data.length <= 0) {
                        this.$message.error('没有选中任何记录，无法提交');
                        return;
                    }
                    ajax.post('api/finance/finance-voucher/voucherPreparationForSupply', this.$refs.materialTable.getCheckboxRecords());
                },
                voucherPreparationForConstruction: function () {
                    let data = this.$refs.constructionItem.getCheckboxRecords();
                    if (data == null || data.length <= 0) {
                        this.$message.error('没有选中任何记录，无法提交');
                        return;
                    }
                    ajax.post('api/finance/finance-voucher/voucherPreparationForConstruction', this.$refs.constructionItem.getCheckboxRecords());
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
                    let url = 'api/finance/finance-item/loadFinancenItem';
                    ajax.get(url, null, function (data) {
                        that.financeItemptions = data;
                    });
                },
                confirmSelectFinanceItem() {
                    if (this.financeItem.selectedFinanceItem == null || this.financeItem.selectedFinanceItem.length === 0) {
                        return;
                    }

                    for (let items of this.financeItem.selectedFinanceItem) {
                        let financeItemName = this.findFinanceItemName(items, this.financeItemptions, '', 0);

                        let financeItemValue = '';
                        let childFinanceItemtValue = '';
                        let childFinanceItemtName = '';
                        let financeItemNames = financeItemName.split("-");
                        financeItemName = '';
                        for (let i = 0; i < financeItemNames.length - 1; i++) {
                            financeItemName += financeItemNames[i] + "-";
                        }
                        if(financeItemNames[financeItemNames.length - 2]!=null){
                            financeItemName = financeItemNames[financeItemNames.length - 2];
                            childFinanceItemtName = financeItemNames[financeItemNames.length - 1];
                            financeItemValue = items[items.length - 2];
                            childFinanceItemtValue = items[items.length - 1];
                        }
                        else{
                            financeItemName = financeItemNames[financeItemNames.length - 1];
                            financeItemValue = items[items.length - 1];
                        }

                        // projectValue = items[items.length - 1];
                        let financeItem = {
                            financeItemName: financeItemName,
                            childFinanceItemtName: childFinanceItemtName,
                            financeItemId: financeItemValue,
                            childFinanceItemId: childFinanceItemtValue,
                            money: 0
                        };

                        let isFind = false;
                        if (this.financeItems.length > 0) {
                            for (let financeItem of this.financeItems) {
                                if (financeItem.financeItemId == financeItemValue) {
                                    isFind = true;
                                    break;
                                }
                            }
                        }

                        if (!isFind) {
                            this.financeItems.push(financeItem);
                        }
                    }
                    this.dialogOtherVisible = false;
                },
                findFinanceItemName(financeItems, financeItemptions, financeItemName, initValue) {
                    for (let item of financeItemptions) {
                        if (item.value == financeItems[initValue]) {
                            initValue++;

                            financeItemName += item.label + "-";
                            if (initValue == financeItems.length) {
                                return financeItemName.substring(0, financeItemName.length - 1);
                            } else {
                                return this.findFinanceItemName(financeItems, item.children, financeItemName, initValue)
                            }
                        }
                    }
                },
                deleteFinanceItem(row) {
                    this.$confirm('此操作将删除会计科目, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.$refs.financeItem.remove(row);
                        this.$refs.financeItem.updateFooter();

                        for (let i = 0; i < this.financeItems.length; i++) {
                            let item = this.financeItems[i];
                            if (item.financeItemId == row.financeItemId) {
                                this.financeItems.splice(i, 1);
                                break;
                            }
                        }
                    });
                },

                voucherPreparationForOther: function () {
                    let data = this.financeItem;
                    if (data == null || data.length <= 0) {
                        this.$message.error('没有新增任何记录，无法提交');
                        return;
                    }
                    ajax.post('api/finance/finance-voucher/voucherPreparationForOther', data);
                },
                isModify: function (obj) {
                    if (obj.row.isModified == '1') {
                        return "modify_row";
                    } else if (obj.row.isModified == '0') {
                        return 'new_row';
                    } else if (obj.row.isModified == '2') {
                        return 'delete_row';
                    }
                },

                activeRowMethod({row, rowIndex}) {
                    if (row.auditStatus == '1') {
                        return false;
                    }

                    return true;
                },

                checkMethod: function ({row}) {

                    // return row.auditStatus == '1';
                },
                popupDialog() {
                    this.dialogOtherVisible = true;
                    this.loadFinancenItem();
                },

            }
        })
    ;
    //vm.init();
    return vm;
})