require(['vue', 'ELEMENT', 'axios', 'ajax', 'vxe-table', 'vueselect', 'org-selectemployee', 'org-orgtree', 'util'], function (Vue, element, axios, ajax, table, vueselect, orgSelectEmployee, orgTree, util) {
    Vue.use(table);
    let vm = new Vue({
            el: '#app',
            data: function () {
                return {
                    payItems: [],
                    payItem: {},
                    payForm: {
                        needPay: null,
                        payDate: util.getNowDate()
                    },
                    payNo: util.getRequest("payNo"),
                    queryCond: {
                        auditStatus: '',
                        employeeId: '',
                        startDate: '',
                        endDate: '',
                    },
                    normalPayInfos: [],
                    payments: [],
                    payItemOptions: [],
                    extendDisplay: 'display:none',
                    decoratorDisplay: 'display:none',
                    employeeDisplay: 'display:none',
                    companyDisplay: 'display:none',
                    shopDisplay: 'display:none',
                    brandDisplay: 'display:none',
                    brandIds: null,
                    decoratorIds: null,
                    companyIds: null,
                    companyNames: null,
                    shopIds: null,
                    shopNames: null,
                    decorators : [],
                    brands: [],
                    employeeIds: null,
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
                    }
                }
            },
            methods: {
                init() {
                    let that = this;
                    let url = 'api/bss.order/finance/initCollectionComponent';
                    if (this.payNo != null) {
                        url += '?payNo=' + this.payNo;
                    } else {
                        url += '?payNo=0';
                    }
                    ajax.get(url, null, function (data) {
                        that.payments = data.payments;
                        that.payItemOptions = data.collectionItemOption;
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

                footerMethod({columns, data}) {
                    return [
                        columns.map((column, columnIndex) => {
                            if (['money'].includes(column.property)) {
                                let total = 0;
                                data.forEach(function (v, k) {
                                    total += parseFloat(v.money);
                                })
                                return "合计: " + total.toFixed(2) + "元"
                            }
                            return '-'
                        })
                    ]
                },

                addPayItem : function() {
                    let payItemId = this.payItem.selectedPayItem[this.payItem.selectedPayItem.length - 1];

                    let values = this.$refs['payItems'].getCheckedNodes(false);
                    let payItemName = values[0].data.self.name;
                    let extend = values[0].data.self.extend;
                    let parentPayItemId = values[0].data.self.parentCollectionItemId;
                    let parentPayItemName = null;

                    if (parentPayItemId) {
                        let parentPayItem = this.findParentPayItem(parentPayItemId);
                        if (parentPayItem) {
                            parentPayItemName = parentPayItem.name;
                            payItemName = parentPayItemName + "-" + payItemName;
                        }
                    }

                    if (extend == 'D') {
                        //工人师傅
                        if (this.decoratorIds == null) {
                            this.$message.error('请先选择师傅~~~~~~~！');
                            return;
                        }

                        this.decoratorIds.forEach((decoratorId) => {
                            if (this.existsPayItem(payItemId, decoratorId)) {
                                return;
                            }
                            let decoratorName = this.findDecoratorName(decoratorId);
                            let tempName = payItemName + "-" + decoratorName;
                            let payItem = {
                                payItemId : payItemId,
                                payItemName : tempName,
                                parentPayItemId: parentPayItemId,
                                projectId: decoratorId,
                                money: 0
                            };
                            this.payItems.push(payItem);

                        });
                    } else if (extend == 'B') {
                        if (this.brandIds == null) {
                            this.$message.error('请先选择品牌~~~~~~~！');
                            return
                        }
                        //供应商品牌

                        this.brandIds.forEach((brandId) => {
                            if (this.existsPayItem(payItemId, brandId)) {
                                return;
                            }
                            let brandName = this.findBrandName(brandId);
                            let tempName = payItemName + "-" + brandName;
                            let payItem = {
                                payItemId : payItemId,
                                payItemName : tempName,
                                parentPayItemId: parentPayItemId,
                                projectId: brandId,
                                money: 0
                            };
                            this.payItems.push(payItem);
                        })
                    } else if (extend == 'C') {
                        //公司
                        if (this.companyIds == null) {
                            this.$message.error('请先选择公司~~~~~~~！');
                            return
                        }

                        let companyIds = this.companyIds.split(",");
                        let companyNames = this.companyNames.split("\/");

                        let length = companyIds.length;
                        for (let i=0;i<length;i++) {
                            if (this.existsPayItem(payItemId, companyIds[i])) {
                                continue;
                            }
                            let tempName = payItemName + "-" + companyNames[i];
                            let payItem = {
                                payItemId : payItemId,
                                payItemName : tempName,
                                parentPayItemId: parentPayItemId,
                                projectId: companyIds[i],
                                money: 0
                            };
                            this.payItems.push(payItem);
                        }
                    } else if (extend == 'S') {
                        //店面
                        if (this.shopIds == null) {
                            this.$message.error('请先选择部门~~~~~~~！');
                            return;
                        }

                        let shopIds = this.shopIds.split(",");
                        let shopNames = this.shopNames.split("\/");

                        let length = shopIds.length;
                        for (let i=0;i<length;i++) {
                            if (this.existsPayItem(payItemId, shopIds[i])) {
                                continue;
                            }
                            let tempName = payItemName + "-" + shopNames[i];
                            let payItem = {
                                payItemId : payItemId,
                                payItemName : tempName,
                                parentPayItemId: parentPayItemId,
                                projectId: shopIds[i],
                                money: 0
                            };
                            this.payItems.push(payItem);
                        }

                    } else if (extend == 'E') {
                        //员工
                        if (this.employeeIds == null) {
                            this.$message.error('请先选择员工~~~~~~~！');
                            return;
                        }

                        let employeeIds = this.employeeIds;

                        employeeIds.forEach((employeeId) => {
                            if (this.existsPayItem(payItemId, employeeId)) {
                                return;
                            }
                            let employeeName = this.findEmployeeName(employeeId);
                            let tempName = payItemName + "-" + employeeName;
                            let payItem = {
                                payItemId : payItemId,
                                payItemName : tempName,
                                parentPayItemId: parentPayItemId,
                                projectId: employeeId,
                                money: 0
                            };
                            this.payItems.push(payItem);
                        })

                    }

                },

                findParentPayItem : function(parentItemId, payItemOptions) {
                    if (!payItemOptions) {
                        payItemOptions = this.payItemOptions;
                    }

                    if (!payItemOptions || payItemOptions.length <= 0) {
                        return null;
                    }

                    for (let i=0;i<payItemOptions.length;i++) {
                        let self = payItemOptions[i].self;
                        if (self.id == parentItemId) {
                            return self;
                        }

                        let children = self.children;
                        let result = null;
                        if (children && children.length > 0) {
                            result = this.findParentPayItem(parentItemId, children);
                        }

                        if (result) {
                            return result;
                        }
                    }
                },

                findDecoratorName : function(decoratorId) {
                    if (this.decorators == null || this.decorators.length <= 0) {
                        return null;
                    }

                    let length = this.decorators.length;
                    for (let i=0;i<length;i++) {
                        let decorator = this.decorators[i];
                        if (decorator.decoratorId == decoratorId) {
                            return decorator.name;
                        }
                    }

                    return null;
                },

                findBrandName : function(brandId) {
                    if (this.brands == null || this.brands.length <= 0) {
                        return null;
                    }

                    let length = this.brands.length;
                    for (let i=0;i<length;i++) {
                        let brand = this.brands[i];
                        if (brand.id == brandId) {
                            return brand.name;
                        }
                    }

                    return null;
                },

                findEmployeeName : function(employeeId) {
                    let values = this.$refs['selectEmployees'].options;
                    if (values == null || values.length <= 0) {
                        return null;
                    }

                    let length = values.length;
                    for (let i=0;i<length;i++) {
                        let option = values[i];
                        if (option.employeeId == employeeId) {
                            return option.name;
                        }
                    }

                    return null;
                },

                existsPayItem: function(payItemId, projectId) {
                    if (this.payItems == null || this.payItems.length <= 0) {
                        return false;
                    }

                    let length = this.payItems.length;
                    for (let i=0;i<length;i++) {
                        let payItem = this.payItems[i];
                        if (projectId == null && payItemId == payItem.payItemId) {
                            return true;
                        }

                        if (payItemId == payItem.payItemId && projectId == payItem.projectId) {
                            return true;
                        }
                    }
                    return false;
                },

                findPayItemName(payItems, payItemOptions, payItemName, initValue) {
                    for (let item of payItemOptions) {
                        if (item.value == payItems[initValue]) {
                            initValue++;

                            payItemName += item.label + "-";
                            if (initValue == payItems.length) {
                                return payItemName.substring(0, payItemName.length - 1);
                            } else {
                                return this.findPayItemName(payItems, item.children, payItemName, initValue)
                            }
                        }
                    }
                },
                popupDialog() {
                    this.dialogVisible = true;
                },

                handlePayItem(value) {
                    let values = this.$refs['payItems'].getCheckedNodes();
                    let extend = values[0].data.self.extend;
                    let that = this;
                    if (extend == 'D') {
                        ajax.get('/api/bss.order/decorator/initDecorators', null, function (data) {
                            that.decorators = data;
                            that.extendDisplay = 'display:block';
                            that.decoratorDisplay = 'display:block';
                            that.employeeDisplay = 'display:none';
                            that.shopDisplay = 'display:none';
                            that.companyDisplay = 'display:none';
                            that.brandDisplay = 'display:none';
                            that.employeeIds = null;
                            that.brandIds = null;
                            that.shopIds = null;
                            that.companyIds = null;
                            that.companyNames = null;
                            that.shopNames = null;
                        });
                    } else if (extend == 'B') {
                        ajax.get('/api/bss.supply/supplier-brand/initBrands', null, function (data) {
                            that.brands = data;
                            that.extendDisplay = 'display:block';
                            that.decoratorDisplay = 'display:none';
                            that.employeeDisplay = 'display:none';
                            that.shopDisplay = 'display:none';
                            that.companyDisplay = 'display:none';
                            that.brandDisplay = 'display:block';
                            that.employeeIds = null;
                            that.decoratorIds = null;
                            that.shopIds = null;
                            that.companyIds = null;
                            that.companyNames = null;
                            that.shopNames = null;
                        });
                    } else if (extend == 'C') {
                        that.extendDisplay = 'display:block';
                        that.decoratorDisplay = 'display:none';
                        that.employeeDisplay = 'display:none';
                        that.shopDisplay = 'display:none';
                        that.companyDisplay = 'display:block';
                        that.brandDisplay = 'display:none';
                        that.employeeIds = null;
                        that.brandIds = null;
                        that.shopIds = null;
                        that.decoratorIds = null;
                        that.companyNames = null;
                        that.shopNames = null;
                    } else if (extend == 'S') {
                        that.extendDisplay = 'display:block';
                        that.decoratorDisplay = 'display:none';
                        that.employeeDisplay = 'display:none';
                        that.shopDisplay = 'display:block';
                        that.companyDisplay = 'display:none';
                        that.brandDisplay = 'display:none';
                        that.employeeIds = null;
                        that.brandIds = null;
                        that.decoratorIds = null;
                        that.companyIds = null;
                        that.companyNames = null;
                        that.shopNames = null;
                    } else if (extend == 'E') {
                        that.extendDisplay = 'display:block';
                        that.decoratorDisplay = 'display:none';
                        that.employeeDisplay = 'display:block';
                        that.shopDisplay = 'display:none';
                        that.companyDisplay = 'display:none';
                        that.brandDisplay = 'display:none';
                        that.decoratorIds = null;
                        that.brandIds = null;
                        that.shopIds = null;
                        that.companyIds = null;
                        that.companyNames = null;
                        that.shopNames = null;
                    } else {
                        that.extendDisplay = 'display:none';
                        that.decoratorDisplay = 'display:none';
                        that.employeeDisplay = 'display:none';
                        that.shopDisplay = 'display:none';
                        that.companyDisplay = 'display:none';
                        that.brandDisplay = 'display:none';
                        that.employeeIds = null;
                        that.brandIds = null;
                        that.decoratorIds = null;
                        that.shopIds = null;
                        that.companyIds = null;
                        that.companyNames = null;
                        that.shopNames = null;
                    }
                },

                handleFeeTableDelete: function (index) {
                    vm.employeeTableData.splice(index, 1);
                },

                handleSelectionChange(val) {
                    this.multipleSelection = val;
                }
                ,
                handleFeeTableDelete: function (index) {
                    vm.feeTableData.splice(index, 1);
                },
                async valid() {
                    let isFormValid = false;
                    this.$refs['payForm'].validate(valid => {
                        isFormValid = valid;
                    });

                    if (!isFormValid) {
                        this.$message.error('填写信息不完整，请亲仔细检查哦~~~~~~~！');
                        return false;
                    }

                    if (this.payForm.needPay == 0) {
                        this.$message.error('收款金额不能为0~~~~~~~！');
                        return false;
                    }

                    const payMoneyError = await this.$refs.payMoney.fullValidate().catch(errMap => errMap);
                    const payItemError = await this.$refs.payItem.fullValidate().catch(errMap => errMap);
                    if (payMoneyError || payItemError) {
                        return false;
                    }

                    let totalPay = 0.00;
                    if (this.payments) {
                        for (let payment of this.payments) {
                            totalPay += parseFloat(payment.money);
                        }
                    }
                    totalPay = totalPay.toFixed(2);

                    let totalFee = 0.00;
                    if (this.payItems) {
                        for (let item of this.payItems) {
                            totalFee += parseFloat(item.money);
                        }
                    }
                    totalFee = totalFee.toFixed(2);

                    this.payForm.needPay = parseFloat(this.payForm.needPay).toFixed(2);
                    if (this.payForm.needPay != totalFee) {
                        this.$message.error('收款金额与款项金额的合计不一致，请亲仔细检查哦~~~~~~~！');
                        return false;
                    }

                    if (totalPay != totalFee) {
                        this.$message.error('款项金额与付款金额不一致，请亲仔细检查哦~~~~~~~！');
                        return false;
                    }
                    return true;
                },

                deletePayItem: function(row) {
                    this.$confirm('此操作将删除款项, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.$refs.payItem.remove(row);
                        this.$refs.payItem.updateFooter();

                        for (let i = 0; i < this.payItems.length; i++) {
                            let item = this.payItems[i];
                            if (item.payItemId == row.payItemId) {
                                this.payItems.splice(i, 1);
                                break;
                            }
                        }
                    });
                },
                queryPayInfoByCond: function () {
                    let that = this;
                    ajax.get('api/bss.order/finance/queryPayInfoByCond', this.queryCond, function (responseData) {

                        that.normalPayInfos = responseData;
                    });
                },
                toPayDetail(payNo) {
                    util.openPage('openUrl?url=modules/bss/order/finance/cashier/non_business_collection&payNo='+payNo, '非主营收款详情');
                },
                getSubmitData() {
                    let payDate = this.payForm.payDate;
                    let needPay = this.payForm.needPay;
                    let data = {
                        payDate: payDate,
                        needPay: needPay,
                        payItems: [],
                        payments: [],
                        remark: this.auditRemark,
                        auditStatus: this.auditStatus,
                        payNo: this.payNo


                    };

                    data.payItems = [];
                    for (let i = 0; i < this.payItems.length; i++) {
                        let payItem = {};

                        payItem.payItemId = this.payItems[i].payItemId.split("_")[1];
                        payItem.parentPayItemId = this.payItems[i].parentPayItemId;
                        payItem.projectId = this.payItems[i].projectId;
                        payItem.money = this.payItems[i].money;
                        data.payItems.push(payItem);
                    }

                    data.payments = this.payments;

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
                }
            }
        })
    ;
    vm.init();
    return vm;
})