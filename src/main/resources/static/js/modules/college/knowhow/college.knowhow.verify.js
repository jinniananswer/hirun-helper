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
                dialogVisible1: false,
                questionIds: [],
            }
        },

        // 页面初始化触发点
        created: function () {
            this.queryByEmployeeIdAndRelaType('2');
        },

        methods: {
            queryByCond: function () {
                var that = this;
                this.queryCond.relationType = '2';
                this.queryCond.optionTag = 'APPROVE';
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
                }
            },

            handleClose(done) {
                this.$confirm('确认关闭？')
                    .then(_ => {
                        done();
                    })
                    .catch(_ => {});
            },

            handleSelectChange(val) {
                this.questionIds = val;
            },

            verify: function () {
                let val = this.questionIds;
                var that = this;
                if (val == undefined || val == 'undefined' || val.length <= 0) {
                    this.$message({
                        showClose: true,
                        duration: 3000,
                        message: '您未选择需要审核的问题！请选择后再点击审核。',
                        center: true
                    });
                    return;
                }

                this.$confirm('是否审核通过?', '提示', {
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
                    val.forEach(v => {
                        v.approvedTag = '0'
                    })
                    ajax.post('api/CollegeQuestion/verifyQuestion', val, function (responseData) {
                        that.queryByEmployeeIdAndRelaType('2');
                        that.$message({
                            type: 'info',
                            message: '审批不通过!'
                        });
                    }, null, true);
                });
            }
        },
    });

    return vm;
});