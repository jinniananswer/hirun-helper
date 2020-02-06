require(['vue', 'ELEMENT', 'axios', 'upload-file'], function (Vue, element, axios, uploadFile) {
    let vm = new Vue({
        el: '#app',
        data() {
            return {

            };
        },

        computed: {

        },

        methods: {
            handleClick() {
                console.log("fileId: " + this.$refs.upload.fileId);
                console.log("fileList: " + JSON.stringify(this.$refs.upload.fileList));
            },
        },

    });
    return vm;
})