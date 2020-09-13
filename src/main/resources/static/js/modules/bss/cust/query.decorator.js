require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util', 'order-selectemployee', 'vue-router','house-select'], function (Vue, element, axios, ajax, vueselect, util, orderSelectEmployee, vueRouter,houseSelect) {
    let vm = new Vue({
        el: '#app',
        data: function () {
            return {
                decoratorQueryCond: {
                    decoratorId: '',
                    name: '',
                    identityNo: '',
                    mobileNo: '',
                    decoratorType: '',
                    agreementTag: '',
                    page:1,
                    size:10,
                    total:0
                },

                decoratorInfo: [],
                checked: null,
                modifyTag: "",
                display: 'display:block',
                editDecoratorDialogVisible: false,
                decoratorEditCond: {},
                decoratorRules: {},
                addDecoratorDialogVisible: false,
                decoratorAddCond: {},
                decoratorIds: [],
            }
        },

        methods: {
            queryDecorator: function () {
                let that = this;
                ajax.get('api/bss.order/decorator/queryDecoratorInfo', this.decoratorQueryCond, function (responseData) {
                    vm.decoratorInfo = responseData.records;
                    that.decoratorQueryCond.page = responseData.current;
                    that.decoratorQueryCond.total = responseData.total;
                });
            },

            handleSizeChange: function (size) {
                this.decoratorQueryCond.size = size;
                this.decoratorQueryCond.page = 1;
                this.queryDecorator();
            },

            handleCurrentChange: function(currentPage){
                this.decoratorQueryCond.page = currentPage;
                this.queryDecorator();
            },

            handleSelectionChange(val) {
                this.decoratorIds = val;
            },

            addDecorator() {
                this.modifyTag = "0";
                this.addDecoratorDialogVisible = true;
            },

            deleteDecoratorBatch() {
                let val = this.decoratorIds;
                var that = this;
                if (val == undefined || val == 'undefined' || val.length <= 0) {
                    this.$message({
                        showClose: true,
                        duration: 3000,
                        message: '您未选择需要删除的工人！请选择后再点击删除。',
                        center: true
                    });
                    return;
                }

                this.$confirm('确认删除选中工人?', '提示', {
                    confirmButtonText: '确认',
                    cancelButtonText: '取消',
                    center: true
                }).then(() => {
                    ajax.post('api/bss.order/decorator/deleteDecoratorBatch', val, function () {
                        that.queryDecorator();
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

            editDecoratorById(decorator) {
                this.$nextTick(()=>{
                    this.$refs.decoratorEditCond.resetFields();
                });
                this.modifyTag = "2";
                this.decoratorEditCond = JSON.parse(JSON.stringify(decorator));
                this.editDecoratorDialogVisible = true;
            },

            submitEdit(decorator) {
                let that = this;
                ajax.post('api/bss.order/decorator/updateById', decorator, function(responseData){
                    that.editDecoratorDialogVisible = false;
                    that.queryDecorator();
                    that.$message({
                        type: 'success',
                        message: '修改成功!'
                    });
                },null, true);
            },

            submitAdd(decoratorAddCond) {
                let that = this;
                ajax.post('api/bss.order/decorator/addDecorator', decoratorAddCond, function(responseData){
                    that.addDecoratorDialogVisible = false;
                    that.queryDecorator();
                    that.$message({
                        type: 'success',
                        message: '新增成功!'
                    });
                },null, true);
            },

            cancel() {
                this.addDecoratorDialogVisible = false;
                this.$refs.decoratorAddCond.resetFields();
            },

            cancelEdit() {
                this.editDecoratorDialogVisible = false;
                this.$refs.decoratorEditCond.resetFields();
            },
        }
    });

    return vm;
})