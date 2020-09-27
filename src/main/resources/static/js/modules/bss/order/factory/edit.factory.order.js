require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util', 'order-info', 'order-worker', 'order-selectemployee', 'vxe-table'], function(Vue, element, axios, ajax, vueselect, util, orderInfo, orderWorker, orderSelectEmployee, table) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data:  {
            factoryOrder : {
                orderId : util.getRequest('orderId')
            },
            saveDisabled : false
        },

        methods: {

            init : function() {
                let that = this;
                ajax.get('api/bss/order/order-factory-order/getFactoryOrder', {orderId:this.factoryOrder.orderId}, function(responseData){
                    if (responseData) {
                        that.factoryOrder = responseData;
                        if (that.factoryOrder.status == '1') {
                            that.saveDisabled = true;
                        }
                    }
                });
            },

            save : function() {
                if (this.factoryOrder.status == '1') {
                    this.$message.error('该单据已关闭，不能修改');
                    return;
                }
                this.setTableData();
                ajax.post('api/bss/order/order-factory-order/saveFactoryOrder', this.factoryOrder, null, null, true);
            },

            close: function() {
                if (this.factoryOrder.status == '1') {
                    this.$message.error('该单据已关闭，不能修改');
                    return;
                }

                ajax.post('api/bss/order/order-factory-order/closeFactoryOrder', this.factoryOrder, null, null, true);
            },

            createFollow : function() {
                let data = {
                    followDate : util.getNowDate(),
                    orderDate : util.getNowDate(),
                    deliverDate : util.getNowDate(),
                    nature : '',
                    content : '',
                    responsible : '',
                    orderMan : ''
                }
                this.$refs.followTable.insertAt(data, 0);
            },

            saveFollows : function() {
                if (this.factoryOrder.status == '1') {
                    this.$message.error('该单据已关闭，不能修改');
                    return;
                }
                if (this.factoryOrder.id == null) {
                    this.$message.error('工厂产品订单信息没有保存，请点击保存工厂产品订单信息');
                    return false;
                }
                this.setTableData();
                ajax.post('api/bss/order/order-factory-order/saveFollows', this.factoryOrder.follows, null, null, true);
            },

            setTableData : function() {
                let insertRecords = this.$refs.followTable.getInsertRecords();
                let updateRecords = this.$refs.followTable.getUpdateRecords();
                let removeRecords = this.$refs.followTable.getRemoveRecords();
                if (this.factoryOrder.follows == null) {
                    this.factoryOrder.follows = [];
                }

                this.factoryOrder.follows = this.factoryOrder.follows.concat(insertRecords);
                this.factoryOrder.follows = this.factoryOrder.follows.concat(updateRecords);
                this.factoryOrder.follows = this.factoryOrder.follows.concat(removeRecords);

                let that = this;
                if (this.factoryOrder.follows.length > 0) {
                    this.factoryOrder.follows.forEach( follow => {
                        follow.factoryOrderId = that.factoryOrder.id;
                    })
                }
            },

            editMethod ({ row, column }) {
                // 重写默认的覆盖式，改为追加式
                this.$refs.followTable.setActiveCell(row, column.property);
                // 返回 false 阻止默认行为;
                return false
            }
        },
        mounted () {
            this.init();
        }
    });
    return vm;
})