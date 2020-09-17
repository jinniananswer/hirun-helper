require(['vue', 'ELEMENT', 'ajax', 'vxe-table', 'vueselect', 'org-orgtree', 'house-select', 'util', 'cust-info', 'order-info', 'order-worker', 'order-search-employee'], function (Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util, custInfo, orderInfo, orderWorker, orderSearchEmployee) {
    Vue.use(table);
    let vm = new Vue({
        el: '#knowhow',
        data: function () {
            return {
                employeeId: util.getRequest('employeeId'),
                queryCond: {
                    questionText: '',
                    questionTitle: '',
                    questionContent: '',
                    sortType: '',
                    relationType: '',
                    status: '',
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
                addQustionInfo: {},
                dialogVisible: false,
                questionIds: [],
            }
        },

        // 页面初始化触发点
        created: function () {
            this.queryByEmployeeIdAndRelaType();
        },

        methods: {
            queryByCond: function () {
                var that = this;
                ajax.get('api/CollegeQuestion/querySelfQuestion', this.queryCond, function (responseData) {
                    if (0 == responseData.total) {
                        return;
                    }
                    that.questionInfo = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;
                });
            },

            queryByEmployeeIdAndRelaType: function () {
                var that = this;
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
                if (status === '0') {
                    return '已失效'
                } else if (status === '1') {
                    return '未审批'
                } else if (status === '2') {
                    return '未回答'
                } else if (status === '3') {
                    return '未发布'
                } else if (status === '4') {
                    return '已发布'
                } else if(status === '5'){
                    return '审批失败'
                }
            },

            addQuestion: function () {
                let questionTitle = this.queryCond.questionTitle;
                let questionContent = this.queryCond.questionContent;
                var that = this;
                that.addQustionInfo = {
                    questionTitle: questionTitle,
                    questionContent: questionContent
                };
                ajax.post('api/CollegeQuestion/addQuestion', that.addQustionInfo, function (responseData) {
                    that.$message({
                        showClose: true,
                        message: '问题新增成功',
                        type: 'success'
                    });

                    that.dialogVisible = false;
                    that.queryByEmployeeIdAndRelaType();
                }, null, true);
            },

            handleClose(done) {
                this.$confirm('确认关闭？')
                    .then(_ => {
                        done();
                    })
                    .catch(_ => {
                    });
            },

            handleSelectChange(val) {
                this.questionIds = val;
            },

            deleteQuestionBatch: function() {
                let val = this.questionIds;
                var that = this;
                if (val == undefined || val == 'undefined' || val.length <= 0) {
                    this.$message({
                        showClose: true,
                        duration: 3000,
                        message: '您未选择需要删除的问题！请选择后再点击删除。',
                        center: true
                    });
                    return;
                }

                this.$confirm('此操作将删除选中问题, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    center: true
                }).then(() => {
                    ajax.post('api/CollegeQuestion/deleteQuestionByIds', val, function (responseData) {
                        that.queryByEmployeeIdAndRelaType();
                    }, null, true);
                    this.$message({
                        type: 'success',
                        message: '删除成功!'
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });

            }
        },
    });

    return vm;
});