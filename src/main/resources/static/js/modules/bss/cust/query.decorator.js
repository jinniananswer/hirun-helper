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
                display: 'display:block',
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

            handleSelectionChange() {

            },

            addDecorator() {

            },

            deleteDecoratorBatch() {

            },
        }
    });

    return vm;
})