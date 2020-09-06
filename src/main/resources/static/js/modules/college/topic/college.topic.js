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
                topicEditCond: {
                    name: '',
                    type: '',
                    correctAnswer: '',
                    score: '',
                },
                topicOptionEditCond: {
                    symbol: '',
                    name: '',
                    optionId: '',
                },
                topicAddCond: {
                    name: '',
                    type: '',
                    correctAnswer: '',
                    score: '',
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
                editTopicDialogVisible: false,
                editTopicOptionDialogVisible: false,
                addTopicDialogVisible: false,
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

            addTopic() {

            },

            deleteTopicBatch() {
                let val = this.topicIds;
                var that = this;
                if (val == undefined || val == 'undefined' || val.length <= 0) {
                    this.$message({
                        showClose: true,
                        duration: 3000,
                        message: '您未选择需要删除的习题！请选择后再点击删除。',
                        center: true
                    });
                    return;
                }

                this.$confirm('确认删除选中习题?', '提示', {
                    confirmButtonText: '确认',
                    cancelButtonText: '取消',
                    center: true
                }).then(() => {
                    ajax.post('api/ExamTopic/deleteTopicBatch', val, function (responseData) {
                        that.query();
                        that.$message({
                            type: 'success',
                            message: '删除成功!'
                        });
                    }, null, true);
                }).catch(() => {
                    that.$message({
                        type: 'info',
                        message: '取消删除!'
                    });
                });
            },

            submitEdit(topic) {
                var that = this;
                ajax.post('api/ExamTopic/updateByTopic', topic, function (responseData) {
                    that.query();
                    that.editTopicDialogVisible = false;
                    that.topicEditCond = {};
                    that.$message({
                        type: 'success',
                        message: '修改成功!'
                    });
                }, null, true);

            },

            submitTopicOptionEdit(topicOption) {
                var that = this;
                this.topicOptionEditCond.optionId = topicOption.optionId;
                ajax.post('api/ExamTopic/updateTopicOption', this.topicOptionEditCond, function (responseData) {
                    that.query();
                    that.editTopicOptionDialogVisible = false;
                    that.topicOptionEditCond = {};
                    that.$message({
                        type: 'success',
                        message: '修改成功!'
                    });
                }, null, true);
            },

            submitAdd() {

            },

            editTopicById(topic){
                this.$nextTick(()=>{
                    this.$refs.topicEditCond.resetFields();
                });
                this.topicEditCond = JSON.parse(JSON.stringify(topic));
                this.editTopicDialogVisible = true;
            },

            editTopicOptionById(option){
                this.$nextTick(()=>{
                    this.$refs.topicOptionEditCond.resetFields();
                });
                this.topicOptionEditCond = JSON.parse(JSON.stringify(option));
                this.editTopicOptionDialogVisible = true;
            },
        },
    });

    return vm;
});