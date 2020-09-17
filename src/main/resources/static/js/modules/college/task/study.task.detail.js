require(['vue','ELEMENT','ajax', 'vxe-table', 'vueselect', 'org-orgtree','house-select', 'util', 'cust-info', 'order-info', 'order-worker', 'order-search-employee', 'order-file-upload', 'upload-file'], function(Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util, custInfo, orderInfo, orderWorker, orderSearchEmployee, orderFileUpload) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                queryCond: {
                    studyTaskId: '',
                    limit: 20,
                    page: 1,
                    count: null
                },
                studyTaskId: util.getRequest("studyTaskId"),
                studyTaskInfo: {},
                subActiveTab: 'baseInfo',
                taskDetailInfo: {},
                collegeCourseChapterInfo: [],
                addChaptersDialogVisible: false,
                addChaptersInfo: {},
                editChaptersDialogVisible: false,
                editChaptersInfo: {}
            }
        },
        methods: {
            initTaskInfo: function(){
                let that = this;
                let studyTaskId = that.studyTaskId;
                if(studyTaskId=='undefined'){
                    studyTaskId = null;
                }
                ajax.get('api/CollegeStudyTaskCfg/getCollegeStudyTaskByStudyTaskId', {studyTaskId:studyTaskId}, function(responseData){
                    that.studyTaskInfo = responseData;
                    that.collegeCourseChapterInfo = responseData.collegeCourseChaptersList;
                });
            },
            cancel: function () {
                let that = this;
                that.addChaptersDialogVisible = false;
                that.editChaptersDialogVisible = false;
            },
            addChapters: function () {
                let that = this;
                that.addChaptersDialogVisible = true;
                that.addChaptersInfo = JSON.parse(JSON.stringify(that.studyTaskInfo));
            },
            submitAddChapters: function () {
                this.$confirm('是否提交保存?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let that = this
                    ajax.post('api/CollegeCourseChaptersCfg/addChapters', that.addChaptersInfo, function(responseData){
                        that.addChaptersDialogVisible = false;
                        that.$message({
                            message: '章节配置新增成功',
                            type: 'success'
                        });
                        that.collegeCourseChapterInfo.push(responseData);
                        that.addChaptersInfo = {};
                        that.addChaptersDialogVisible = false;
                    },null, true);
                })
            },
            editChapters: function (row) {
                let that = this;
                that.editChaptersDialogVisible = true;
                that.editChaptersInfoSource = row
                that.editChaptersInfo = JSON.parse(JSON.stringify(row));
            },
            submitEditChapters: function (editChaptersInfo) {
                this.$confirm('是否提交修改?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let that = this
                    ajax.post('api/CollegeCourseChaptersCfg/editChapters', editChaptersInfo, function(responseData){
                        that.editChaptersInfoSource = editChaptersInfo
                        that.editChaptersDialogVisible = false;
                        let collegeCourseChapterInfo = that.collegeCourseChapterInfo;
                        for(let i = 0 ; i < collegeCourseChapterInfo.length ; i++){
                            if(collegeCourseChapterInfo[i].chaptersId == editChaptersInfo.chaptersId){
                                collegeCourseChapterInfo[i] = editChaptersInfo;
                                that.editChaptersDialogVisible = false;
                                break;
                            }
                        }
                        that.$message({
                            message: '学习任务修改成功',
                            type: 'success'
                        });
                    },null, true);
                })
            },
        },
        mounted () {
            this.initTaskInfo();
        },
    });

    return vm;
});