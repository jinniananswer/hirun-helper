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
                orderWorkActions: [],
                orderWorkAction :{
                    orderId : '',
                    orderStatus : '',
                    employeeId :'',
                    employeeName : '',
                    action:'',
                    roleId : ''
                },
                custId: util.getRequest('custId'),
                isShow : true,
                id : util.getRequest('id'),
                orderStatus : util.getRequest('orderStatus'),
                quantityRoomInfosRules : {
                    measureArea: [
                        { required: true, message: '请填写量房面积', trigger: 'change' }
                    ],
                },
            }
        },

        methods: {
            getDatas : function() {
                let orderId = this.quantityRoomInfos.orderId;
                let measureArea = this.quantityRoomInfos.measureArea;
                let designer = this.quantityRoomInfos.designer;
                let orderWorkActions = this.orderWorkActions;
                let data = {
                    orderId: orderId,
                    id: '',
                    measureTime: util.getNowDate(),
                    measureArea: measureArea,
                    designer: designer,
                    orderWorkActions: orderWorkActions
                };
                //alert(JSON.stringify(data));
                return data;
            },
            addSuccess : function() {
                this.orderWorkAction = {};
                this.orderWorkAction.employeeId = this.quantityRoomInfos.assistantDesigner;
                if (this.orderWorkAction.employeeId == '' || this.orderWorkAction.employeeId == null) {
                    Vue.prototype.$message({
                        message: '请先选择助理设计师再添加设计师！',
                        type: 'error'
                    });
                    return false;
                }
                ajax.get('api/bss.order/order-measurehouse/getEmployeeNameEmployeeId', {employeeId:this.orderWorkAction.employeeId}, (responseData)=>{
                    this.orderWorkAction.employeeName = responseData.employeeName ;
                });
                this.orderWorkAction.orderId = this.quantityRoomInfos.orderId;
                this.orderWorkAction.action = 'measure';
                this.orderWorkAction.roleId = '19';
                this.orderWorkAction.orderStatus = this.orderStatus;
                this.orderWorkActions.push(this.orderWorkAction);
                //alert(JSON.stringify(this.orderWorkActions));

            },
            deleteRow : function(index, rows) {
                rows.splice(index, 1);
            },
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
            init:function() {
                let that = this;
                let data = {
                    orderId : util.getRequest('orderId'),
                }
                ajax.get('api/bss.order/order-measurehouse/getMeasureHouse', data, (responseData)=>{
                    //alert(JSON.stringify(responseData));
                    Object.assign(this.quantityRoomInfos, responseData);
                    this.orderWorkActions = responseData.orderWorkActions;
                });
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
                } else if (command == 'submitToOnlyWoodworkFlow') {
                    this.submitToOnlyWoodworkFlow();
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
                let data = this.getDatas();
                ajax.post('api/bss.order/order-measurehouse/saveMeasureHouseInfos', data,null,null,true);
            },
            submitToOnlyWoodworkFlow : function() {
                ajax.post('api/bss.order/order-measurehouse/submitToOnlyWoodworkFlow', this.quantityRoomInfos);
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