require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util', 'order-selectemployee', 'vue-router','house-select'], function (Vue, element, axios, ajax, vueselect, util, orderSelectEmployee, vueRouter,houseSelect) {
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
            }
        },

        // 页面初始化触发点
        created: function () {
            this.queryCourseFileInfo();
        },

        methods: {
            queryCourseFileInfo: function () {
                let that = this;
                ajax.get('api/organization/course/queryCourseInfo', this.courseFileQueryCond, function (responseData) {
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
        }
    });

    return vm;
})