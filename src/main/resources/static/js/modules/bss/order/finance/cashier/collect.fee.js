require(['vue', 'ELEMENT', 'axios', 'ajax', 'vxe-table', 'vueselect', 'util'], function (Vue, element, axios, ajax, table, vueselect, util) {
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
                    periodDisabled: true,
                    payments: [],
                    payItemOptions: [],
                    dialogVisible: false,
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
                init() {
                    let that = this;
                    let url = 'api/bss.order/finance/initCollectionComponent';
                    ajax.get(url, null, function (data) {
                        that.payments = data.payments;
                        that.payItemOptions = data.collectionItemOption;
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
                confirmSelectPayItem() {
                    if (this.payItem.selectedPayItem == null || this.payItem.selectedPayItem.length === 0) {
                        return;
                    }

                    for (let items of this.payItem.selectedPayItem) {
                        let payItemName = this.findPayItemName(items, this.payItemOptions, '', 0);

                        let payItemValue = '';
                        let subjectName = '';
                        let projectName = '';
                        let subjectValue = null;
                        let projectValue = null;
                        // if (items[items.length-1].indexOf('period') < 0) {
                        //     payItemValue = items[items.length - 1];
                        //     periodName = '-';
                        // } else {
                        let payItemNames = payItemName.split("-");
                        payItemName = '';
                        for (let i = 0; i < payItemNames.length - 1; i++) {
                            payItemName += payItemNames[i] + "-";
                        }
                        payItemName = payItemNames[payItemNames.length - 3];//payItemName.substring(0, payItemName.length - 3);
                        subjectName = payItemNames[payItemNames.length - 2];
                        projectName = payItemNames[payItemNames.length - 1];
                        payItemValue = items[items.length - 3];
                        subjectValue = items[items.length - 2];
                        projectValue = items[items.length - 1];
                        alert("payItemValue======"+payItemValue);
                        alert("payItemName======"+payItemName);
                        alert("subjectValue======"+subjectValue);
                        alert("subjectName======"+subjectName);
                        alert("projectValue======"+projectValue);
                        alert("projectName======"+projectName);
                        // }
                        let payItem = {
                            payItemName: payItemName,
                            subjectName: subjectName,
                            payItemId: payItemValue,
                            projectName: projectName,
                            subjectId: subjectValue,
                            projectId: projectValue,
                            money: 0
                        };

                        let isFind = false;
                        if (this.payItems.length > 0) {
                            for (let payItem of this.payItems) {
                                if (payItem.payItemId == payItemValue && payItem.subjectId == subjectValue && payItem.projectId == projectValue) {
                                    isFind = true;
                                    break;
                                }
                            }
                        }

                        if (!isFind) {
                            this.payItems.push(payItem);
                        }
                    }
                    this.dialogVisible = false;
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
                    this.periodDisabled = false;
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
                    console.log(JSON.stringify(this.feeTableData))
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
                deletePayItem(row) {
                    this.$confirm('此操作将删除款项, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.$refs.payItem.remove(row);
                        this.$refs.payItem.updateFooter();

                        for (let i=0;i<this.payItems.length;i++) {
                            let item = this.payItems[i];
                            if (item.payItemId == row.payItemId) {
                                this.payItems.splice(i,1);
                                break;
                            }
                        }
                    });
                },

                getSubmitData() {
                    let payDate = this.payForm.payDate;
                    let needPay = this.payForm.needPay;
                    let data = {
                        payDate : payDate,
                        needPay : needPay,
                        payItems:[],
                        payments:[]
                    };
                    for (let i=0;i<this.payItems.length;i++) {
                        let payItem = {};
                        alert("project==========1========"+this.payItems[i].projectId);
                        alert("subject==========1========"+this.payItems[i].subjectId);
                        alert("payItemId========1=========="+this.payItems[i].payItemId);
                        alert("payItemId========2=========="+this.payItems[i].payItemId.split("_")[1]);
                        alert("subject===========2======="+this.payItems[i].subjectId.split("_")[1]);
                        alert("project=========2========="+this.payItems[i].projectId.split("_")[1]);
                        payItem.payItemId = this.payItems[i].payItemId.split("_")[1];
                        payItem.subjectId = this.payItems[i].subjectId.split("_")[1];
                        payItem.projectId = this.payItems[i].projectId.split("_")[1];
                        payItem.money = this.payItems[i].money;
                        data.payItems.push(payItem);
                    }

                    for (let i=0;i<this.payments.length;i++) {
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
                        data['orderId'] = this.orderId;
                        ajax.post('api/bss.order/finance/nonCollectFee', data);
                    }
                }
            }
        })
    ;
    vm.init();
    return vm;
})