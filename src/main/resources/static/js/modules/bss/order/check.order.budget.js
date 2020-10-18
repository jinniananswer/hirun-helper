require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-selectemployee','cust-visit'], function(Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee,custVisit) {    let vm = new Vue({
        el: '#app',
        data() {
            return {
                budget: {
                    id : '',
                    orderId : '',
                    totalFeeCheckResult : '一致',
                    locationRemarkCheckResult : '一致',
                    contentExpressionCheckResult : '一致',
                    unitPriceCheckResult : '一致',
                    logoCheckResult : '一致',
                    unitPriceConsistenceCheckResult : '一致',
                    materialRemarkCheckResult : '一致',
                    serialNumberCheckResult : '一致',
                    materialRemarkConsistenceCheckResult : '一致',
                    fontSizeCheckResult : '一致',
                    selfPurchaseRemarkCheckResult : '一致',
                    numberConsistenceCheckResult : '一致',
                    checkDate : '',
                },
                orderId:util.getRequest("orderId"),
                custId:util.getRequest("custId"),

                budgetRules : {
                    checkDate: [
                        {required: true, message: '请选择对审时间', trigger: 'change'}
                    ],
                    totalFeeCheckResult: [
                        {required: true, message: '请填写汇总金额是否正确', trigger: 'blur'},
                    ],
                    locationRemarkCheckResult: [
                        {required: true, message: '请填写所做项目位置标注是否不清', trigger: 'blur'}
                    ],
                    contentExpressionCheckResult: [
                        {required: true, message: '请填写格式文字表达是否正确', trigger: 'blur'}
                    ],
                    unitPriceCheckResult: [
                        {required: true, message: '请填写表格上单价是否正确', trigger: 'blur'}
                    ],
                    logoCheckResult: [
                        {required: true, message: '请填写徽标是否错误、变形', trigger: 'blur'}
                    ],
                    unitPriceConsistenceCheckResult: [
                        {required: true, message: '请填写表格上单价前后是否一致', trigger: 'blur'}
                    ],
                    materialRemarkCheckResult: [
                        {required: true, message: '请填写表格上材料是否标注清楚', trigger: 'blur'}
                    ],
                    serialNumberCheckResult: [
                        {required: true, message: '请填写序号排列是否清楚', trigger: 'blur'}
                    ],
                    materialRemarkConsistenceCheckResult: [
                        {required: true, message: '请填写预算材料与预算表后说明是否统一', trigger: 'blur'}
                    ],
                    fontSizeCheckResult: [
                        {required: true, message: '请填写字体大小有没有调整', trigger: 'blur'}
                    ],
                    selfPurchaseRemarkCheckResult: [
                        {required: true, message: '请填写对客户自购的材料是否标注清楚', trigger: 'blur'}
                    ],
                    numberConsistenceCheckResult: [
                        {required: true, message: '请填写汇总表与明细表数字前后是否一致', trigger: 'blur'}
                    ],
                },
                downloadFileUrl : ''
            }
        },
        mounted: function() {
            let data = {
                orderId : util.getRequest("orderId")
            }
            ajax.get('api/bss.order/order-budget/getBudgetByOrderId', data, (responseData)=>{
                Object.assign(this.budget, responseData);
            });
            this.budget.checkDate = util.getNowDate();
            this.downloadFileUrl = 'api/bss.order/order-file/download/' + util.getRequest("orderId") + "/13";
        },
        methods: {
            submit : function(command) {
                this.$refs['budget'].validate((valid) => {
                    if (valid) {
                        let data = this.budget;
                        data.checkResult = command;
                        ajax.post('api/bss.order/order-budget/submitBudgetCheckedResult', data);
                    } else {
                        return false;
                    }
                });
            }
        }
    });

    return vm;
})