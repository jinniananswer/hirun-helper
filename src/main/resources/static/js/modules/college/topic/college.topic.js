require(['vue', 'ELEMENT', 'ajax', 'vxe-table', 'vueselect', 'org-orgtree', 'house-select', 'util', 'cust-info', 'order-info', 'order-worker', 'order-search-employee'], function (Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util, custInfo, orderInfo, orderWorker, orderSearchEmployee) {
    Vue.use(table);
    let vm = new Vue({
        el: '#topic',
        data: function () {
            return {
                queryCond: {
                    topicText: '',
                    type: '',
                    current: 1,
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
                    examId: '',
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
                addTopicOptionInfos: [],
                addTopicOptionInfo: {},
                showTree: 'display:block',

                importUrl:'www.baidu.com',//后台接口config.admin_url+'rest/schedule/import/'
                importHeaders:{
                    enctype:'multipart/form-data',
                    cityCode:''
                },
                name: 'import',
                fileList: [],
                withCredentials: true,
                processing: false,
                uploadTip:'点击上传',
                importFlag:1,
                dialogImportVisible:false,
                errorResults:[],
                topicAddRules: {
                    name: [
                        { required: true, message: '习题内容不能为空', trigger: 'blur' }
                    ],
                    type: [
                        { required: true, message: '请选择习题内容', trigger: 'change' }
                    ],
                    correctAnswer: [
                        { required: true, message: '正确选项不能为空', trigger: 'blur' }
                    ],
                    score: [
                        { required: true, message: '习题分数不能为空', trigger: 'blur' }
                    ],
                },
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
                let that = this;
                that.addTopicDialogVisible = true;
                that.topicAddCond = {};
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
                ajax.post('api/ExamTopic/updateTopicOption', topicOption, function (responseData) {
                    that.query();
                    that.editTopicOptionDialogVisible = false;
                    that.topicOptionEditCond = {};
                    that.$message({
                        type: 'success',
                        message: '修改成功!'
                    });
                }, null, true);
            },

            submitAdd(addTopicOptionInfos) {
                this.$refs.addTopicOptionInfos.validate((valid) => {
                    if(valid){
                        let that = this;
                        this.$confirm('是否保存新增习题?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            that.topicAddCond.topicOptions = that.addTopicOptionInfos;
                            ajax.post('api/ExamTopic/addTopic', that.topicAddCond, function(responseData){
                                that.query();
                                that.$message({
                                    showClose: true,
                                    message: '习题新增成功',
                                    type: 'success'
                                });
                                that.addTopicDialogVisible = false;
                                that.addTopicOptionInfos = [];
                            });
                        })
                    }
                });
            },

            cancel: function () {
                let that = this;
                that.addTopicDialogVisible = false;
                that.topicAddCond = {};
                that.addTopicOptionInfos = [];
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

            addTopicOption() {
                this.addTopicOptionInfo = {
                    topicId: this.topicId,
                    symbol: '',
                    name: '',
                    status: '0'
                };

                let that = this;
                that.$refs.addTopicOptionInfos.insertAt(that.addTopicOptionInfo, 0);
                that.addTopicOptionInfos.push(this.addTopicOptionInfo);
                that.topicAddCond.topicOptions = that.addTopicOptionInfos;
            },

            deleteTopicOptionBatch() {

            },

            deleteTopicOption(row) {

            },

            importData() {
                this.importFlag = 1;
                this.fileList = [];
                this.uploadTip = '点击上传';
                this.processing = false;
                this.dialogImportVisible = true;
            },

            outportData() {
                scheduleApi.downloadTemplate();
            },
            handlePreview(file) {
                //可以通过 file.response 拿到服务端返回数据
            },
            handleRemove(file, fileList) {
                //文件移除
            },
            beforeUpload(file){
                //上传前配置
                this.importHeaders.cityCode='上海';//可以配置请求头
                let excelfileExtend = ".xls,.xlsx";//设置文件格式
                let fileExtend = file.name.substring(file.name.lastIndexOf('.')).toLowerCase();
                if (excelfileExtend.indexOf(fileExtend) <= -1) {
                    this.$message.error('文件格式错误');
                    return false;
                }
                this.uploadTip = '正在处理中...';
                this.processing = true;
            },
            //上传错误
            uploadFail(err, file, fileList) {
                this.uploadTip = '点击上传';
                this.processing = false;
                this.$message.error(err);
            },
            //上传成功
            uploadSuccess(response, file, fileList) {
                this.uploadTip = '点击上传';
                this.processing = false;
                if (response.status === -1) {
                    this.errorResults = response.data;
                    if (this.errorResults) {
                        this.importFlag = 2;
                    } else {
                        this.dialogImportVisible = false;
                        this.$message.error(response.errorMsg);
                    }
                } else {
                    this.importFlag = 3;
                    this.dialogImportVisible = false;
                    this.$message.info('导入成功');
                    this.doSearch();
                }
            },
            //下载模板
            download() {
                //调用后台模板方法,和导出类似
                scheduleApi.downloadTemplate();
            },
        },
    });

    return vm;
});