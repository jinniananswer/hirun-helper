require(['vue', 'ELEMENT', 'axios', 'ajax', 'vxe-table', 'vueselect', 'org-selectemployee', 'org-orgtree', 'order-search-employee', 'util'], function (Vue, element, axios, ajax, table, vueselect, orgSelectEmployee, orgTree, orgSearchEmployee, util) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function () {
            return {
                voucher: {
                    voucherDate: util.getNowDate(),
                    voucherItems: []
                },
                queryCond : {
                    count: 0,
                    limit: 20,
                    page: 1
                },
                materialVoucher: {
                    voucherDate: util.getNowDate()
                },
                materialVoucherVisible : false,
                materialDatas: [],
                financeItems: null,
                financeItemId: null,
                employeeId: null,
                employeeName: null,
                voucherTab: 'voucherTab'
            }
        },
        methods: {
            init() {
                let that = this;
                ajax.get('api/finance/finance-item/loadFinanceItems', null, function(data){
                    that.financeItems = data;
                });
            },

            footerMethod({columns, data}) {
                return [
                    columns.map((column, columnIndex) => {
                        if (['fee'].includes(column.property)) {
                            let total = 0;
                            data.forEach(function (v, k) {
                                if (v.fee) {
                                    total += parseFloat(v.fee);
                                }
                            })
                            return "合计: " + total.toFixed(2) + "元"
                        }
                        return '-'
                    })
                ]
            },

            toChinese : function() {
                this.voucher.chineseTotalMoney = util.moneyToChinese(this.voucher.totalMoney);
            },

            clearEmployee : function() {
                this.employeeId = null;
                this.employeeName = null;
            },

            addVoucherItem : function() {
                let voucherItem = {};
                if (!this.financeItemId) {
                    this.$message.error('请选择财务科目！');
                    return false;
                }
                let length = this.financeItemId.length;
                let financeItemName = "";
                for (let i=0;i<length;i++) {
                    let name = this.findFinanceItemName(this.financeItemId[i], this.financeItems);
                    if (name != "") {
                        financeItemName += name + "/";
                    }
                }

                if (financeItemName.length > 0) {
                    financeItemName = financeItemName.substring(0, financeItemName.length - 1);
                }

                if (this.employeeId != null) {
                    let that = this;
                    ajax.get('api/organization/employee/getSimpleEmployeeInfo', {employeeId: this.employeeId}, function(data){
                        voucherItem.orgName = data.orgPath;
                        voucherItem.orgId = data.orgId;
                        voucherItem.employeeId = data.employeeId;
                        voucherItem.financeItemName = financeItemName;
                        voucherItem.projectType = '1';
                        voucherItem.projectName = data.name;
                        voucherItem.projectId = data.employeeId;
                        voucherItem.financeItemId = that.financeItemId[that.financeItemId.length - 1];
                        that.voucher.voucherItems.push(voucherItem);
                    });
                } else {
                    voucherItem.projectType = '2'
                    voucherItem.financeItemName = financeItemName;
                    voucherItem.financeItemId = this.financeItemId[this.financeItemId.length - 1];
                    this.voucher.voucherItems.push(voucherItem);
                }
            },

            findFinanceItemName : function(financeItemId, financeItems) {
                if (!financeItems) {
                    return "";
                }

                for (let i=0;i<financeItems.length;i++) {
                    if (financeItemId == financeItems[i].value) {
                        return financeItems[i].label;
                    }

                    if (financeItems[i].children) {
                        let name = this.findFinanceItemName(financeItemId, financeItems[i].children);

                        if (name != "") {
                            return name;
                        }
                    }
                }

                return "";
            },

            activeCellMethod ({ row, column, columnIndex }) {
                if (columnIndex == '1' && row.projectType == '1') {
                    return false;
                }
                return true;
            },

            changeVoucherType : function() {

            },

            valid : function() {
                let isFormValid = false;
                this.$refs['financePayForm'].validate(valid => {
                    isFormValid = valid;
                });

                if (!isFormValid) {
                    this.$message.error('填写信息不完整，请亲仔细检查哦~~~~~~~！');
                    return false;
                }

                if (this.voucher.totalMoney == '') {
                    this.$message.error('付款总金额不能为空');
                    return false;
                }

                if (this.voucher.totalMoney == 0) {
                    this.$message.error('付款总金额不能为0');
                    return false;
                }

                let totalMoney = 0;
                if (this.voucher.voucherItems.length > 0) {
                    for (let i=0;i<this.voucher.voucherItems.length;i++) {
                        let voucherItem = this.voucher.voucherItems[i];
                        if (voucherItem.fee != '') {
                            totalMoney += voucherItem.fee;
                        }
                    }
                }

                if (totalMoney != this.voucher.totalMoney) {
                    this.$message.error('付款总金额与明细金额不相等，请仔细检查');
                    return false;
                }

                return true;
            },

            deleteVoucherItem: function(row) {
                this.$confirm('此操作将删除款项, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$refs.voucherItems.remove(row);
                    this.$refs.voucherItems.updateFooter();
                    for (let i=0;i<this.voucher.voucherItems.length;i++) {
                        let item = this.voucher.voucherItems[i];
                        if (item._XID == row._XID) {
                            this.voucher.voucherItems.splice(i,1);
                            break;
                        }
                    }
                });
            },

            activeRowMethod : function() {

            },

            queryMaterials: function() {
                let that = this;
                ajax.get('api/bss.supply/supply-order/querySupplyInfo', this.queryCond, function (responseData) {
                    that.materialDatas = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;
                });
            },

            showMaterialVoucher: function() {
                let data = this.$refs.materialTable.getCheckboxRecords();
                if (data == null || data.length <= 0) {
                    this.$message.error('没有选中任何记录，无法提交');
                    return;
                }
                this.materialVoucherVisible = true;
            },

            checkMethod: function({row}) {
                return row.auditStatus == '0';
            },

            submit: function () {
                let isValid = this.valid();
                if (isValid) {
                    ajax.post('api/finance/finance-voucher/createVoucher', this.voucher);
                }
            },

            submitMaterial : function() {
                if (!this.materialVoucher.voucherDate) {
                    this.$message.error('请选择制单日期');
                    return false;
                }

                if (!this.materialVoucher.financeItemId) {
                    this.$message.error('请选择财务科目');
                    return false;
                }
                this.materialVoucher.financeItemId = this.materialVoucher.financeItemId[this.materialVoucher.financeItemId.length - 1]
                let data = this.$refs.materialTable.getCheckboxRecords();
                if (data == null || data.length <= 0) {
                    this.$message.error('没有选中任何记录，无法提交');
                    return;
                }
                this.materialVoucher['details'] = data;
                ajax.post('api/bss.supply/supply-order/submitSupplyDetail', this.materialVoucher);
            },

            auditMaterial : function() {

            }
        },

        mounted () {
            this.init();
        }
    });
    return vm;
})