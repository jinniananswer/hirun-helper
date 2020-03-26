require(['vue', 'ELEMENT', 'axios', 'ajax',  'vxe-table','vueselect', 'util'], function (Vue, element, axios, ajax, table,vueselect, util) {
    Vue.use(table);
    let vm = new Vue({
            el: '#app',
            data: function () {
                return {
                    payItems: [],
                    payItem:{},
                    payForm:{
                        needPay: null,
                        payDate: util.getNowDate()
                    },
                    periodDisabled: true,
                    payments: [],
                    payItemOptions:[],
                    dialogVisible: false,
                    validRules: {
                        money: [
                            {type:'number', message: '金额必须为数字'},
                            {pattern:/^[0-9]+(\.\d+)?$/, message: '金额必须为正数'}
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
                    ajax.get(url, null, function(data) {
                        that.payments = data.payments;
                        that.payItemOptions = data.collectionItemOption;
                    });
                },

                footerMethod ({ columns, data }) {
                    return [
                        columns.map((column, columnIndex) => {
                            if (['money'].includes(column.property)) {
                                let total = 0;
                                data.forEach(function(v, k) {
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
                        let periodName = '';
                        let projectName = '';
                        let periodValue = null;
                        let projectValue = null;
                        // if (items[items.length-1].indexOf('period') < 0) {
                        //     payItemValue = items[items.length - 1];
                        //     periodName = '-';
                        // } else {
                            let payItemNames = payItemName.split("-");
                            payItemName = '';
                            for (let i=0;i<payItemNames.length - 1;i++) {
                                payItemName += payItemNames[i] + "-";
                            }
                            payItemName = payItemNames[payItemNames.length - 3];//payItemName.substring(0, payItemName.length - 3);
                            periodName = payItemNames[payItemNames.length - 2];
                            projectName = payItemNames[payItemNames.length - 1];
                            payItemValue = items[items.length - 2];
                            periodValue = items[items.length - 1];
                            projectValue = items[items.length - 3];
                       // }
                        let payItem = {
                            payItemName : payItemName,
                            periodName : periodName,
                            payItemId: payItemValue,
                            projectName: projectName,
                            period: periodValue,
                            project: projectValue,
                            money:0
                        };

                        let isFind = false;
                        if (this.payItems.length > 0) {
                            for (let payItem of this.payItems) {
                                if (payItem.payItemId == payItemValue&&payItem.period==periodValue&&payItem.project==projectValue) {
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

                            payItemName += item.label+"-";
                            if (initValue == payItems.length) {
                                return payItemName.substring(0, payItemName.length-1);
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
                }
                ,

                handleSelectionChange(val) {
                    this.multipleSelection = val;
                }
                ,
                handleFeeTableDelete: function (index) {
                    vm.feeTableData.splice(index, 1);
                    console.log(JSON.stringify(this.feeTableData))
                },
                submit : async function() {
                    let isValid = await this.$refs.pay.valid().then(isValid=>isValid);
                    if (isValid) {
                        let data = this.$refs.pay.getSubmitData();
                        data['orderId'] = this.orderId;
                        ajax.post('api/bss.order/finance/collectFee', data);
                    }
                }
            }
        })
    ;
    vm.init();
    return vm;
})