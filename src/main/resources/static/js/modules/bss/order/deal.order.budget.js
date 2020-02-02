require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info'], function(Vue, element, axios, ajax, vueselect, util, custInfo) {
    let vm = new Vue({
        el: '#app',
        data() {
            return {
                customer: {
                    title : '客户基本信息',
                    name : '刘慧',
                    birthday : '1989-01-01',
                    mobileNo : '18573140029',

                },

                customerDefault: 'base',

                progress: [-10,70],

                activeTab:'orderInfo',



                marks: {
                    0: '酝酿',
                    10: '初选',
                    30: '初步决策',
                    50: '决策',
                    60: '施工',
                    95: '维护'
                },

                requirement : {
                    title : '客户需求信息',
                    style : '白色简约',
                    func : '功能列表'
                },

                workers: [
                    {
                        job: '家装顾问',
                        name: '左金虎'
                    },
                    {
                        job: '客户代表',
                        name: '张晓梅'
                    },
                    {
                        job: '设计师',
                        name: '罗厚石'
                    },
                    {
                        job: '项目经理',
                        name: '彭帅'
                    },
                    {
                        job: '工程助理',
                        name: '彭帅'
                    },
                    {
                        job: '工程文员',
                        name: '彭帅'
                    },
                    {
                        job: '出纳',
                        name: '彭帅'
                    },
                    {
                        job: '财务',
                        name: '彭帅'
                    },
                    {
                        job: '售后人员',
                        name: '彭帅'
                    }
                ],

                avatarUrl: 'static/img/male.jpg',
                budget: {
                    id : '',
                    orderId : '',
                    fee : 0,
                    reportedBudgetDate : util.getNowDate(),
                    activityId : '',
                    activities : [],
                    custIdea : '',
                    totalFeeCheckResult : '',
                    locationRemarkCheckResult : '',
                    contentExpressionCheckResult : '',
                    unitPriceCheckResult : '',
                    logoCheckResult : '',
                    unitPriceConsistenceCheckResult : '',
                    materialRemarkCheckResult : '',
                    serialNumberCheckResult : '',
                    materialRemarkConsistenceCheckResult : '',
                    fontSizeCheckResult : '',
                    selfPurchaseRemarkCheckResult : '',
                    numberConsistenceCheckResult : '',
                    checkDate : '',
                    checkUserId : ''
                },
                id : util.getRequest('id'),
                orderState : util.getRequest('orderState'),
                isCheckFailed : ''
            }
        },
        mounted: function() {
            if(this.orderState == 'fail') {
                let data = {
                    id : this.id
                }
                ajax.post('api/bss.order/order-budget/getBudgetById', data, (responseData)=>{
                    Object.assign(this.budget, responseData);
                });
            }
            this.isCheckFailed = this.orderState == 'fail' ? true : false;
            this.budget.activities = [
                {value : "1", name : "活动3"},
                {value : "2", name : "活动4"}
            ];
        },
        methods: {
            submit : function() {
                let url = 'api/bss.order/order-budget/submitBudget';
                if(orderState == 'fail') {
                    url = 'api/bss.order/order-budget/submitBudgetCheckedResult';
                }
                ajax.post(url, this.budget);
            }
        }
    });

    return vm;
})