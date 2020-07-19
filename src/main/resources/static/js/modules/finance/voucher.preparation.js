require(['vue', 'ELEMENT', 'ajax', 'vxe-table', 'vueselect', 'org-orgtree', 'house-select', 'util', 'cust-info', 'order-info', 'order-worker', 'order-search-employee', 'order-selectdecorator'], function (Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util, custInfo, orderInfo, orderWorker, orderSearchEmployee,orderSelectDecorator) {
    Vue.use(table);
    let vm = new Vue({
            el: '#app',
            data: function () {
                return {
                    voucherTab:'',
                    materialTab:'',
                    constructionTab: '',
                    otherTab: '',
                    materialTableData: [],
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
                    queryCond: {
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
                        that.queryCond.page = responseData.current;
                        that.queryCond.count = responseData.total;
                    });
                },
                query: function () {
                    let that = this;
                    ajax.get('api/bss.order/finance/queryCustOrderInfo', this.queryCondForMaterial, function (responseData) {
                        that.custOrder = responseData.records;
                        that.queryCond.page = responseData.current;
                        that.queryCond.count = responseData.total;
                    });
                },
                //查询当前流程的施工人员情况
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