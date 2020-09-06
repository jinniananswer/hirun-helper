require(['vue', 'ELEMENT', 'ajax', 'vxe-table', 'vueselect', 'org-orgtree', 'house-select', 'util', 'cust-info', 'order-info', 'order-worker', 'order-search-employee'], function (Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util, custInfo, orderInfo, orderWorker, orderSearchEmployee) {
    Vue.use(table);
    let vm = new Vue({
        el: '#topic',
        data: function () {
            return {
                queryCond: {
                    topicText: '',
                    type: '',
                    page: 1,
                    size: 10,
                    total: 0,
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
                topicIds: [],
            }
        },

        // 页面初始化触发点
        created: function () {
            this.query();
        },

        methods: {
            query: function () {
                var that = this;
                ajax.get('api/ExamTopic/init', this.queryCond, function (responseData) {
                    if (0 == responseData.total) {
                        that.topicInfo = '';
                        return;
                    }
                    that.topicInfo = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;
                });
            },

            queryByCond: function () {
                var that = this;
                ajax.get('api/ExamTopic/queryByCond', this.queryCond, function (responseData) {
                    if (0 == responseData.total) {
                        return;
                    }
                    that.topicInfo = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;
                });
            },

            statusTransfer: function (row, column) {
                let type = row.type;
                if(type === '1'){
                    return '单选题'
                } else if(type === '2'){
                    return '多选题'
                } else if(type === '3'){
                    return '判断题'
                } else if(type === '4'){
                    return '填空题'
                }
            },

            handleSelectChange(val) {
                this.topicIds = val;
            },

            editTopicById() {

            },

            addTopic() {

            },

            deleteTopicBatch() {

            },
        },
    });

    return vm;
});