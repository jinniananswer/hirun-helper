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
                    questionId: '',
                    questionReply: '',
                    replyTitle: '',
                    showType: '',
                    status:'',
                    respondent: '',
                    limit: 20,
                    page: 1,
                    count: null
                },
                form: {
                    respondent: '',
                    questionType: '',
                },
                formLabelWidth: '120px',
                options: [{
                    value: '0',
                    label: '时间倒序'
                }, {
                    value: '1',
                    label: '点击量'
                }],
                value: '',
                questionInfo: [],
                wikiInfos: [],
                wikiInfo: {},
                replyInfo: {},
                dialogVisible: false,
                addQuestionDialogVisible: false,
                replyDialogVisible: false,
                wikiDialogVisible: false,
                activeName: 'questionSquare',
                teachers: [],
                thumbsUpInfos: [],
                thumbsUpInfo: {},
                cancelTag: '',
            }
        },

        // 页面初始化触发点
        created: function () {
            this.queryCond.showType = 'questionSquare';
            this.query();
            this.queryTeacher();
        },

        methods: {
            queryWiki() {
                this.wikiInfo = {
                    wikiTitle: '十二生肖',
                    wikiContent: '鼠牛虎兔龙蛇马羊猴鸡狗猪',
                    wikiType: '1',
                    wikier: '金念',
                    wikiId: '1',
                    createTime: '2020-09-11 14:53:22'
                };
                this.wikiInfos.push(this.wikiInfo);
            },
            comment() {

            },
            showWikiDetail(wikiId) {
                this.wikiDialogVisible = true;
            },
            thumbsUpWiki(row) {
                let that = this;

            },
            thumbsUp(row) {
                let that = this;
                let id = row.questionId;
                let tag = false;

                this.thumbsUpInfos.forEach(t => {
                    if (t.questionId == id) {
                        tag = t.thumbsUpTag;
                    }
                });
                this.thumbsUpInfo = {
                    questionId: id,
                    thumbsUpTag: !tag,
                };
                this.thumbsUpInfos.push(this.thumbsUpInfo);
                this.queryCond.cancelTag = this.thumbsUpInfo.thumbsUpTag ? '0' : '1';
                this.queryCond.questionId = row.questionId;
                let tempInfo = {
                    questionId: id,
                    cancelTag: this.thumbsUpInfo.thumbsUpTag ? '0' : '1',
                };
                ajax.post('api/CollegeQuestion/thumbsUp', this.queryCond, function (responseData) {
                    that.$message({
                        showClose: true,
                        message: that.thumbsUpInfo.thumbsUpTag ? '点赞成功！' : '点赞取消！',
                        type: that.thumbsUpInfo.thumbsUpTag ? 'success' : 'info',
                    });
                    that.queryCond.showType = 'questionSquare';
                    that.query();
                });
            },
            queryTeacher() {
                var that = this;
                ajax.get('api/CollegeQuestion/queryTeacher', null, function (responseData) {
                    if (0 == responseData.total) {
                        that.teachers = [];
                        return;
                    }
                    that.teachers = responseData;
                });
            },

            handleClick(val) {
                if ('selfQuestion' == val.name) {
                    this.wikiInfos = [];
                    this.queryCond.showType = val.name;
                    this.queryByCond();
                } else if ('questionSquare' == val.name) {
                    this.wikiInfos = [];
                    this.queryCond.showType = val.name;
                    this.query();
                } else if ('letMeResponse' == val.name) {
                    this.wikiInfos = [];
                    this.queryCond.showType = val.name;
                    this.queryCond.optionTag = 'REPLY';
                    this.queryCond.relationType = '1';
                    this.queryByCond();
                } else {
                    this.queryWiki();
                    this.queryCond.count = 1;
                }
                this.queryCond.questionText = '';
                this.queryCond.questionType = '';
                this.queryCond.sortType = '';
            },
            query: function () {
                var that = this;
                ajax.get('api/CollegeQuestion/initKnowhowPage', this.queryCond, function (responseData) {
                    if (0 == responseData.total) {
                        that.questionInfo = '';
                        return;
                    }
                    that.questionInfo = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;
                });
            },

            queryByCond: function () {
                var that = this;
                if ('questionSquare' == that.queryCond.showType) {
                    that.queryCond.optionTag = 'SQUARE';
                } else if ('letMeResponse' == that.queryCond.showType) {
                    that.queryCond.optionTag = 'REPLY';
                    that.queryCond.relationType = '1';
                }
                ajax.get('api/CollegeQuestion/querySelfQuestion', this.queryCond, function (responseData) {
                    that.queryCond.optionTag = '';
                    that.queryCond.relationType = '';
                    if (0 == responseData.total) {
                        that.questionInfo = '';
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
                ajax.get('api/CollegeQuestion/queryByEmployeeIdAndRelaType', this.queryCond, function (responseData) {
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
                    return '待审批'
                } else if(status === '2'){
                    return '待回答'
                } else if(status === '3'){
                    return '待发布'
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
                } else if(type === '4'){
                    return '设计类'
                }
            },

            wikiTypeTransfer: function (row, column) {
                let type = row.wikiType;
                if(type === '1'){
                    return '生活'
                } else if(type === '2'){
                    return '设计'
                } else if(type === '3'){
                    return '施工'
                }
            },

            handleClose(done) {
                done();
            },

            queryReply(row, column) {
                var that = this;
                this.queryCond.questionId = row.questionId;
                this.queryCond.replyTitle = row.questionTitle;
                ajax.get('api/CollegeQuestion/queryReplyByQuestionId', this.queryCond, function (responseData) {
                    that.replyInfo = responseData;
                    that.dialogVisible = true;
                });
            },

            reply(row, column) {
                let id = row.questionId;
                this.queryCond.questionId = id;
                this.queryCond.replyTitle = row.questionTitle;
                this.replyDialogVisible = true;
            },

            replyQuestion: function () {
                var that = this;
                ajax.post('api/CollegeQuestion/replyQuestion', this.queryCond, function(responseData) {
                    that.$message({
                        type: 'success',
                        message: '回复成功!'
                    });
                    that.replyDialogVisible = false;
                    that.queryByEmployeeIdAndRelaType('1');
                });
            },

            addQuestion: function () {
                let questionTitle = this.queryCond.questionTitle;
                let questionContent = this.queryCond.questionContent;
                var that = this;
                that.addQustionInfo = {
                    questionTitle: questionTitle,
                    questionContent: questionContent,
                    questionType: this.queryCond.questionType,
                    respondent: this.queryCond.respondent,
                };
                ajax.post('api/CollegeQuestion/addQuestion', that.addQustionInfo, function (responseData) {
                    that.$message({
                        showClose: true,
                        message: '问题新增成功',
                        type: 'success'
                    });

                    that.addQuestionDialogVisible = false;
                    that.queryCond = {};
                    that.queryByEmployeeIdAndRelaType();
                }, null, true);
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

            },
        },
    });

    return vm;
});