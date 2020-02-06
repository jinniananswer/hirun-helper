define(['vue', 'ELEMENT', 'ajax'], function (Vue, element, ajax) {
    Vue.component('order-file-upload', {
        props: {
            buttonName: {default: '上传文件'},
            accept: {required: true},
            limit: {
                type: Number,
                default: 1
            },
            orderId: {
                required: true,
                type: Number,
            },
            stage: {
                required: true,
                type: Number,
            },
        },
        template: `
<div>
<el-upload
    class="order-file-upload"
    action="api/bss.order/order-file/uploadOne"
    :data="data"
    :accept="accept"
    :limit="limit"
    :file-list="fileList"
    :before-upload="handleBefore"
    :on-exceed="handleExceed"
    :on-success="handleSuccess"
    :on-remove="handleRemove">
<el-button size="small" type="primary">{{buttonName}}</el-button>
<el-tag>文件类型: {{accept}}，文件个数: {{limit}}</el-tag>
</el-upload>
</div>
`,
        data: function () {
            return {
                limit: this.limit,
                accept: this.accept,
                data: {
                    orderId: this.orderId,
                    stage: this.stage
                },
                fileList: [],
            }
        },

        computed: {

            fileId: function () {
                let fileId = '';
                this.fileList.forEach(function (e, index, array) {
                    fileId += e.response
                    if (index < array.length - 1) {
                        fileId += ','
                    }
                })
                return fileId;

            }
        },

        methods: {
            handleBefore(file) {
                let filename = file.name
                let arr = filename.split('.')
                if (this.accept.indexOf(arr[1]) == -1) {
                    this.$message({
                        type: 'warning',
                        duration: 0,
                        showClose: true,
                        message: '只能上传格式为 ' + this.accept + ' 的文件!'
                    });
                    return false
                }
            },
            handleExceed(files, fileList) {
                this.$message({
                    type: 'warning',
                    duration: 0,
                    showClose: true,
                    message: '只能传 ' + this.limit + ' 个文件！'
                });
                return false
            },
            handleSuccess(response, file, fileList) {
                this.$message({
                    type: 'success',
                    message: '【' + file.name + '】上传成功！'
                });
            },
            handleRemove(file, fileList) {
                this.fileList = fileList.slice(-this.limit);
                this.$message({
                    type: 'success',
                    message: '【' + file.name + '】删除成功！'
                });
            },
        },

        watch: {},

        mounted() {

        }
    })
})


