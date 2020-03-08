require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-selectemployee','cust-visit'], function(Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee,custVisit) {
        let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                quantityRoomInfos: {
                    id : '',
                    orderId : util.getRequest('orderId'),
                    customerComments: '',
                    measureTime: util.getNowDate(),
                    measureArea: '',
                    designer: '',
                    assistantDesigner :''
                },
                custId: util.getRequest('custId'),
                isShow : true,
                id : util.getRequest('id'),
                orderStatus : util.getRequest('orderStatus')
            }
        },

        methods: {

            checkBeforeOrder : function () {
                if (this.quantityRoomInfos.measureArea == '0' || this.quantityRoomInfos.measureArea == null) {
                    Vue.prototype.$message({
                        message: '量房面积不能为0！',
                        type: 'error'
                    });
                    return false;
                }
            },

            handleChangeMeasureAreaNum : function(value) {
                this.quantityRoomInfos.measureArea = value;
            },
            init:function(){
                alert(this.custId);
                alert(this.quantityRoomInfos.orderId);
                if (this.orderStatus=='4') {
                    this.isShow = false;
                }
            },
            handleCommand : function(command) {
                if ( command == 'submitToPlanesketchFlow') {
                    if (this.quantityRoomInfos.measureArea == '0' || this.quantityRoomInfos.measureArea == null) {
                        Vue.prototype.$message({
                            message: '量房面积不能为0！',
                            type: 'error'
                        });
                        return false;
                    }
                    this.$confirm('确定要执行[已量房，设计平面图]吗?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                    }).then(() => {
                            this.$message({
                                type: 'success',
                                message: '流程操作[已量房，设计平面图]成功!单据发往[占位员工]'
                            });
                        this.submitToPlanesketchFlow();
                    }).catch(() => {
                            this.$message({
                            type: 'info',
                            message: '已取消'
                        });
                    });

                } else if (command == 'submitToDelayTimeFlow') {
                    this.$confirm('确定要执行[暂时不能去量房]吗?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.submitToDelayTimeFlow();
                    }).catch(() => {
                            this.$message({
                            type: 'info',
                            message: '已取消'
                        });
                    });
                } else if (command == 'submitToSneakFlow') {
                    this.submitToSneakFlow();
                    if (this.orderStatus='4') {
                        this.isShow = false;
                    }
                }
            },
            save : function () {
                if (this.quantityRoomInfos.measureArea == '0' || this.quantityRoomInfos.measureArea == null) {
                    Vue.prototype.$message({
                        message: '量房面积不能为0！',
                        type: 'error'
                    });
                    return false;
                }
                ajax.post('api/bss.order/order-measurehouse/saveMeasureHouseInfos', this.quantityRoomInfos,null,null,true);
            },
            submitToPlanesketchFlow : function() {
                ajax.post('api/bss.order/order-measurehouse/submitToPlanesketchFlow', this.quantityRoomInfos);
            },
            submitToDelayTimeFlow : function() {
                ajax.post('api/bss.order/order-measurehouse/submitToMeasureSuspendFlow', this.quantityRoomInfos);
            },
            submitToSneakFlow : function () {
                ajax.post('api/bss.order/order-measurehouse/submitToSneakFlow', this.quantityRoomInfos);
            }
        },
        mounted () {
            this.init();
        }
    });
    return vm;
})