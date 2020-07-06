require(['vue', 'ELEMENT', 'axios', 'order-file-upload'], function (Vue, element, axios, orderFileUpload) {
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