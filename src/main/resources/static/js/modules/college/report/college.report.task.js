require(['vue', 'ELEMENT', 'ajax', 'vxe-table', 'vueselect', 'org-orgtree', 'house-select', 'util', 'cust-info', 'order-info', 'order-worker', 'order-search-employee'], function (Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util, custInfo, orderInfo, orderWorker, orderSearchEmployee) {
    Vue.use(table);
    let vm = new Vue({
        el: '#topic',
        data: function () {
            return {
                queryCond: {
                    topicText: '',
                    type: '',
                    limit: 20,
                    page: 1,
                    count: null
                },
                options: [{
                    value: '0',
                    label: '时间倒序'
                }, {
                    value: '1',
                    label: '点击量'
                }],
                value: '',
                topicInfo: [],
                dialogVisible: false,
                taskInfos: [],
                taskInfo: {},
            }
        },

        // 页面初始化触发点
        created: function () {
            this.query();
        },

        methods: {
            format(percentage) {
                return percentage === 100 ? '满' : `${percentage}%`;
            },
            showTaskDetailReport() {

            },
            query: function () {

            },

            queryByCond: function () {
                this.taskInfo = {
                    taskName: '企业文化学习',
                    rates: '66.7',
                    allNums: '12',
                    completeNums: '8',
                    examNums: '10',
                    exerciseNums: '11',
                };
                this.taskInfos.push(this.taskInfo);
                this.taskInfo = {
                    taskName: '家装知识学习',
                    rates: '100',
                    allNums: '12',
                    completeNums: '12',
                    examNums: '12',
                    exerciseNums: '12',
                };
                this.taskInfos.push(this.taskInfo);
                this.taskInfo = {
                    taskName: '通用知识学习',
                    rates: '50',
                    allNums: '12',
                    completeNums: '6',
                    examNums: '7',
                    exerciseNums: '9',
                };
                this.taskInfos.push(this.taskInfo);
                this.taskInfo = {
                    taskName: '整体产品材料任务',
                    rates: '100',
                    allNums: '8',
                    completeNums: '8',
                    examNums: '8',
                    exerciseNums: '8',
                };
                this.taskInfos.push(this.taskInfo);
                this.taskInfo = {
                    taskName: '客户服务',
                    rates: '83.3',
                    allNums: '6',
                    completeNums: '5',
                    examNums: '6',
                    exerciseNums: '6',
                };
                this.taskInfos.push(this.taskInfo);
                this.queryCond.count = 5;
            },
        },
    });

    return vm;
});