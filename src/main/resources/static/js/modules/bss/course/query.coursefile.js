require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util', 'order-selectemployee', 'vue-router','house-select','order-file-upload', 'upload-file'], function (Vue, element, axios, ajax, vueselect, util, orderSelectEmployee, vueRouter,houseSelect,orderFileUpload) {
    let vm = new Vue({
        el: '#app',
        data: function () {
            return {
                courseFileQueryCond: {
                    fileId: '',
                    name: '',
                    courseId: '',
                    page:1,
                    size:10,
                    total:0
                },
                courseFileEditCond: {
                    name: '',
                    storagePath: '',
                    status: ''
                },
                courseFileAddCond: {},
                courseFileRules: {},

                editCourseFileDialogVisible: false,
                addCourseFileDialogVisible: false,
                modifyTag: "",
                courseFileInfo: [],
                checked: null,
                display: 'display:block',
                dialogVisible: false,
            }
        },

        methods: {
            queryCourseFileInfo: function () {
                let that = this;
                ajax.get('api/organization/coursefile/queryCourseFileInfo', this.courseFileQueryCond, function (responseData) {
                    vm.courseFileInfo = responseData.records;
                    that.courseFileQueryCond.page = responseData.current;
                    that.courseFileQueryCond.total = responseData.total;
                });
            },

            handleSizeChange: function (size) {
                this.courseFileQueryCond.size = size;
                this.courseFileQueryCond.page = 1;
                this.queryDecorator();
            },

            handleCurrentChange: function(currentPage){
                this.courseFileQueryCond.page = currentPage;
                this.queryDecorator();
            },
            deleteCourseFileById(courseFile){
                ajax.post('api/organization/coursefile/deleteCourseFileById', courseFile, function(responseData){

                },null, true);
            },
            editCourseFileById(courseFile){
                this.$nextTick(()=>{
                    this.$refs.courseFileEditCond.resetFields();
                });
                this.modifyTag = "2";
                this.courseFileEditCond = JSON.parse(JSON.stringify(courseFile));
                this.editCourseFileDialogVisible = true;
            },
            submitEdit(courseFileEditCond){
                this.$refs.courseFileEditCond.validate((valid) => {
                    if(valid){
                        let that = this;
                        this.$confirm('是否提交修改课件信息?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            ajax.post('api/organization/coursefile/updateCourseFileById', this.courseFileEditCond, function(responseData){
                                that.editCourseFileDialogVisible = false;
                                let fileSize = that.courseFileInfo.length;
                                for(let i = 0 ; i < fileSize ; i++){
                                    if(that.courseFileInfo[i].id == courseFileEditCond.id){
                                        that.courseFileInfo[i] = courseFileEditCond;
                                        alert(JSON.stringify(that.courseFileInfo[i]))
                                    }
                                }
                            },null, true);
                        })
                    }
                });
            },
            cancel(){
                let that = this;
                if (that.modifyTag == '2'){
                    that.editCourseFileDialogVisible = false;
                }else if (that.modifyTag == '0'){
                    that.addCourseFileDialogVisible = false;
                }
            },
            addCourseFile(){
                this.modifyTag = "0";
                this.addCourseFileDialogVisible = true;
            },
            submitAdd(courseFileAddCond){
                let that = this;
                ajax.post('api/organization/coursefile/addCourseFile', courseFileAddCond, function(responseData){
                    that.addCourseFileDialogVisible = false;
                },null, true);
            },
            deleteCourseFileBatch(){
                let val = this.multipleSelection
                if(val == undefined || val == 'undefined' || val.length <= 0){
                    this.$message({
                        showClose: true,
                        duration: 3000,
                        message: '您未选择需要删除的课件！请选择后再点击删除。',
                        center: true
                    });
                    return;
                }
                alert("勾选了删除：" + val.length + "个元素")
                let fileIdList = [];
                val.forEach(v => {
                    fileIdList.push({fileId:v.id});
                });
                alert(JSON.stringify(fileIdList));
                ajax.post('api/organization/coursefile/deleteCourseFileByIds', val, function(responseData){

                },null, true);
            },
            handleClose(done) {
                this.$confirm('确认关闭？')
                    .then(_ => {
                        done();
                    })
                    .catch(_ => {});
            },
            handleSuccess: function (fileList) {
                alert("fileId: " + JSON.stringify(fileList));
            },
        }
    });

    return vm;
})