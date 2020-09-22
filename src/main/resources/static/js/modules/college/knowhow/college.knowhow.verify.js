require(['vue', 'ELEMENT', 'ajax', 'vxe-table', 'vueselect', 'org-orgtree', 'house-select', 'util', 'cust-info', 'order-info', 'order-worker', 'order-search-employee'], function (Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util, custInfo, orderInfo, orderWorker, orderSearchEmployee) {
    Vue.use(table);
    let vm = new Vue({
        el: '#knowhow',
        data: function () {
            return {
                employeeId: util.getRequest('employeeId'),
                queryCond: {
                    questionText: '',
                    sortType: '',
                    relationType: '',
                    optionTag: '',
                    replyTitle: '',
                    questionId: '',
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
                questionDetailDialogVisible: false,
                questionIds: [],
                activeName: 'questionVerify',
                replyInfo: [],
                questionDetail: {},
            }
        },

        // 页面初始化触发点
        created: function () {
            this.queryByEmployeeIdAndRelaType('2');
        },

        methods: {
            showQuestionDetail(row) {
                this.questionDetailDialogVisible = true;
                this.questionDetail = {
                    questionTitle: row.questionTitle,
                    questionContent: row.questionContent,
                    createTime: row.createTime,
                };
                // this.queryReply(row);
            },
            queryReply(row) {
                let that = this;
                this.queryCond.questionId = row.questionId;
                this.queryCond.replyTitle = row.questionContent;
                ajax.get('api/CollegeQuestion/queryReplyByQuestionId', this.queryCond, function (responseData) {
                    that.questionDetailDialogVisible = true;
                    if (null == responseData) {
                        return;
                    }
                    that.replyInfo = responseData;

                });
            },
            queryAllQuestion() {
                let that = this;
                ajax.get('api/CollegeQuestion/queryAllQuestion', this.queryCond, function (responseData) {
                    if (0 == responseData.total) {
                        return;
                    }
                    that.questionInfo = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;
                });
            },
            handleClick(val) {
                if ('questionVerify' == val.name) {
                    this.queryCond.showType = val.name;
                    this.queryCond.relationType = '2';
                    this.queryCond.optionTag = 'APPROVE';
                    this.queryByCond();
                } else if ('questionPublish' == val.name) {
                    this.queryCond.showType = val.name;
                    this.queryCond.relationType = '0';
                    this.queryCond.optionTag = 'PUBLISH';
                    this.queryByCond();
                } else {
                    this.queryCond.showType = val.name;
                    this.queryCond.optionTag = '';
                    this.queryCond.relationType = '';
                    this.queryAllQuestion();
                }
            },
            queryByCond: function () {
                var that = this;
                if ('questionList' == that.queryCond.showType) {
                    this.queryAllQuestion();
                    return;
                }
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
                this.queryCond.optionTag = 'APPROVE';
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
                } else if(status === '5'){
                    return '已驳回'
                }
            },

            typeTransfer: function (row, column) {
                let type = row.questionType;
                if(type === '1'){
                    return '施工类'
                } else if(type === '2'){
                    return '订单类'
                } else if(type === '3'){
                    return '售后类'
                }
            },

            handleClose(done) {
                done();
            },

            handleSelectChange(val) {
                this.questionIds = val;
            },

            verify: function (tag) {
                let val = this.questionIds;
                var that = this;
                if (val == undefined || val == 'undefined' || val.length <= 0) {
                    this.$message({
                        showClose: true,
                        duration: 3000,
                        message: '您未选择需要审批的问题！请选择后再点击审核。',
                        center: true
                    });
                    return;
                }

                if ('1' == tag) {
                    this.$confirm('是否审批通过?', '提示', {
                        confirmButtonText: '是',
                        cancelButtonText: '否',
                        center: true
                    }).then(() => {
                        val.forEach(v => {
                            v.approvedTag = '1'
                        })
                        ajax.post('api/CollegeQuestion/verifyQuestion', val, function (responseData) {
                            that.queryByEmployeeIdAndRelaType('2');
                            that.$message({
                                type: 'success',
                                message: '审批通过!'
                            });
                        }, null, true);
                    }).catch(() => {

                    });
                }

                if ('0' == tag) {
                    this.$confirm('是否审批驳回?', '提示', {
                        confirmButtonText: '是',
                        cancelButtonText: '否',
                        center: true
                    }).then(() => {
                        val.forEach(v => {
                            v.approvedTag = '0'
                        })
                        ajax.post('api/CollegeQuestion/verifyQuestion', val, function (responseData) {
                            that.queryByEmployeeIdAndRelaType('2');
                            that.$message({
                                type: 'info',
                                message: '审批驳回!'
                            });
                        }, null, true);
                    }).catch(() => {

                    });
                }

            },
            publish: function () {
                let val = this.questionIds;
                var that = this;
                if (val == undefined || val == 'undefined' || val.length <= 0) {
                    this.$message({
                        showClose: true,
                        duration: 3000,
                        message: '您未选择需要发布的问题！请选择后再点击发布。',
                        center: true
                    });
                    return;
                }

                this.$confirm('确认发布该问题?', '提示', {
                    confirmButtonText: '确认',
                    cancelButtonText: '取消',
                    center: true
                }).then(() => {
                    ajax.post('api/CollegeQuestion/publishQuestion', val, function (responseData) {
                        that.queryByEmployeeIdAndRelaType('0');
                        that.$message({
                            type: 'success',
                            message: '发布成功!'
                        });
                    }, null, true);
                }).catch(() => {
                    that.$message({
                        type: 'info',
                        message: '取消发布!'
                    });
                });
            },
        },
    });

    return vm;
});