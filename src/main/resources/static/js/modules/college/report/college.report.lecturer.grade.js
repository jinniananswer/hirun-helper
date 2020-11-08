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
            }
        },

        // 页面初始化触发点
        created: function () {
            this.query();
        },

        methods: {
            query: function () {
                var that = this;
                ajax.get('api/CollegeTopic/init', this.queryCond, function (responseData) {
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
                ajax.get('api/CollegeTopic/queryByCond', this.queryCond, function (responseData) {
                    if (0 == responseData.total) {
                        return;
                    }
                    that.topicInfo = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;
                });
            },
        },
    });

    return vm;
});