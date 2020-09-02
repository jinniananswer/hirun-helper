require(['vue', 'ELEMENT', 'ajax', 'vxe-table', 'vueselect', 'org-orgtree', 'house-select', 'util', 'cust-info', 'order-info', 'order-worker', 'order-search-employee'], function (Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util, custInfo, orderInfo, orderWorker, orderSearchEmployee) {
    Vue.use(table);
    let vm = new Vue({
        el: '#knowhow',
        data: function () {
            return {
                employeeId: util.getRequest('employeeId'),
                queryCond: {
                    questionText: '',
                    questionId: '',
                    sortType: '',
                    relationType: '',
                    optionTag: '',
                    status:'',
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
                questionInfo: [],
                dialogVisible: false,
            }
        },

        // 页面初始化触发点
        created: function () {
            this.queryByEmployeeIdAndRelaType('1');
        },

        methods: {
            queryByCond: function () {
                var that = this;
                this.queryCond.relationType = '1';
                this.queryCond.optionTag = 'REPLY';
                ajax.get('api/CollegeQuestion/querySelfQuestion', this.queryCond, function (responseData) {
                    if (0 == responseData.total) {
                        return;
                    }
                    that.questionInfo = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;
                });
            },

            queryByEmployeeIdAndRelaType: function (relationType) {
                var that = this;
                this.queryCond.relationType = relationType;
                this.queryCond.optionTag = 'REPLY';
                ajax.get('api/CollegeQuestion/querySelfQuestion', this.queryCond, function (responseData) {
                    if (0 == responseData.total) {
                        that.questionInfo = '';
                        return;
                    }
                    that.questionInfo = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;
                });
            },

            statusTransfer: function (row, column) {
                let status = row.status;
                if(status === '0'){
                    return '已失效'
                } else if(status === '1'){
                    return '未审批'
                } else if(status === '2'){
                    return '未回答'
                } else if(status === '3'){
                    return '未发布'
                } else if(status === '4'){
                    return '已发布'
                }
            },

            handleClose(done) {
                this.$confirm('确认关闭？')
                    .then(_ => {
                        done();
                    })
                    .catch(_ => {});
            },

            reply(row, column) {
                let id = row.questionId;
                this.queryCond.questionId = id;
                this.dialogVisible = true;
            },

            replyQuestion: function () {
                var that = this;
                ajax.post('api/CollegeQuestion/replyQuestion', this.queryCond, function(responseData) {
                    that.$message({
                        type: 'success',
                        message: '回复成功!'
                    });
                    that.dialogVisible = false;
                    that.queryByEmployeeIdAndRelaType('1');
                });
            },
        },
    });

    return vm;
});