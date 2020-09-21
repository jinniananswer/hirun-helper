define(['vue', 'ELEMENT', 'ajax'], function (Vue, element, ajax) {
    Vue.component('upload-file', {
        props: ['accept', 'limit'],
        template: `
<div>
<el-input name="fileId" id="fileId" type="hidden" v-model="fileId"></el-input>
<el-upload
    class="upload-file"
    action="api/system/file/uploadOne"
    :limit="limit"
    :accept="accept"
    :on-change="handleChange"
    :file-list="fileList"
    :on-success="handleSuccess"
    :on-remove="handleRemove">
<el-button size="small" type="primary">选取文件</el-button>
<el-tag>文件类型: {{accept}}，文件个数: {{limit}}</el-tag>
</el-upload>
</div>
`,
        data: function () {
            return {
                limit: this.limit,
                accept: this.accept,
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
            handleSuccess: function (response, file, fileList) {

            },
            handleChange(file, fileList) {
                this.fileList = fileList.slice(-this.limit);
            },
            handleRemove(file, fileList) {
                this.fileList = fileList.slice(-this.limit);
            },
            handlePreview(file) {
                console.log(file);
            }
        },

        watch: {

        },

        mounted() {

        }
    })
})


