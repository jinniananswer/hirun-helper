require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-selectemployee','cust-visit','order-search-employee','order-file-upload'], function(Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee,custVisit,orderSearchEmployee,orderFileUpload) {
    let vm = new Vue({
        el: '#app',
        data:  {

            id : util.getRequest('id'),
            wholeRoomDrawing: {
                id : '',
                orderId : util.getRequest('orderId'),
                designer: '',
                preTime: util.getNowDate(),//预约看图时间
                startTime: '',
                endTime: '',
                drawingAuditor :'',
                drawingAuditorName : '',
                productionLeader: '金念',//制作组长
                assistantDesigner: '',//助理设计师
                hydropowerDesigner: '',//水电设计师
                drawingAssistant: '',//绘图助理
                adminAssistant: '',//行政助理
                designerRemarks: '',//设计师备注
                reviewedComments: ''//审核意见
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
            isShow : true,
            isBackToDesigner : false,
            isAudit : false,
            custId: util.getRequest('custId'),
            orderStatus : util.getRequest('orderStatus'),
            eid:null,
            employeeName:'',
            downloadFileUrl : ''
        },

        methods: {

            init:function(){
                let data = {
                    orderId : util.getRequest('orderId'),
                }

                ajax.get('api/bss.order/order-wholeRoomDrawing/getWholeRoomDraw', data, (responseData)=>{
                    Object.assign(this.wholeRoomDrawing, responseData);
                    //alert(JSON.stringify(this.wholeRoomDrawing));
                this.orderWorkActions = responseData.orderWorkActions;
                });

                this.wholeRoomDrawing.orderId = util.getRequest('orderId');
                if (this.orderStatus=='36') {
                    this.isShow = false;
                } else if (this.orderStatus=='37') {
                    this.isBackToDesigner = true;
                } else if (this.orderStatus=='12') {
                    this.isAudit = true;
                }
                this.downloadFileUrl = 'api/bss.order/order-file/download/' + util.getRequest("orderId") + "/567";
            },
            getDatas : function() {
                let orderId = this.wholeRoomDrawing.orderId;
                let reviewedComments = this.wholeRoomDrawing.reviewedComments;
                let designerRemarks = this.wholeRoomDrawing.designerRemarks;
                let adminAssistant = this.wholeRoomDrawing.adminAssistant ;
                let drawingAssistant = this.wholeRoomDrawing.drawingAssistant;
                let hydropowerDesigner = this.wholeRoomDrawing.hydropowerDesigner;
                let productionLeader = this.wholeRoomDrawing.productionLeader;
                let designer = this.wholeRoomDrawing.designer;
                let drawingAuditor = this.wholeRoomDrawing.drawingAuditor;
                let orderWorkActions = this.orderWorkActions;
                let data = {
                    orderId: orderId,
                    id: '',
                    measureTime: util.getNowDate(),
                    reviewedComments: reviewedComments,
                    designerRemarks : designerRemarks,
                    adminAssistant : adminAssistant,
                    drawingAssistant : drawingAssistant,
                    productionLeader : productionLeader,
                    hydropowerDesigner : hydropowerDesigner,
                    drawingAuditor : drawingAuditor,
                    designer: designer,
                    orderWorkActions: orderWorkActions
                };
                return data;
            },
            addSuccessEmployeeName : function() {
                this.orderWorkAction = {};
                this.orderWorkAction.employeeId = this.wholeRoomDrawing.assistantDesigner;
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
                this.orderWorkAction.orderId = this.wholeRoomDrawing.orderId;
                this.orderWorkAction.action = 'draw_construct';
                this.orderWorkAction.roleId = '19';
                this.orderWorkAction.orderStatus = this.orderStatus;
                this.orderWorkActions.push(this.orderWorkAction);
            },
            /*addSuccessHydropower : function() {
                this.orderWorkAction = {};
                this.orderWorkAction.employeeId = this.wholeRoomDrawing.hydropowerDesigner;
                if (this.orderWorkAction.employeeId == '' || this.orderWorkAction.employeeId == null) {
                    Vue.prototype.$message({
                        message: '请先选择水电设计师再添加设计师！',
                        type: 'error'
                    });
                    return false;
                }
                ajax.get('api/bss.order/order-measurehouse/getEmployeeNameEmployeeId', {employeeId:this.orderWorkAction.employeeId}, (responseData)=>{
                    this.orderWorkAction.employeeName = responseData.employeeName ;
                });
                this.orderWorkAction.orderId = this.wholeRoomDrawing.orderId;
                this.orderWorkAction.action = 'water_elec_design';
                this.orderWorkAction.roleId = '19';
                this.orderWorkAction.orderStatus = this.orderStatus;
                this.orderWorkActions.push(this.orderWorkAction);
            },*/
            deleteRow : function(index, rows) {
                rows.splice(index, 1);
            },
            checkBeforeOrder: function () {
            },
            handleCommand : function(command) {
                if ( command == 'submitToAuditPicturesFlow') {
                    if (this.wholeRoomDrawing.productionLeader == '') {
                        Vue.prototype.$message({
                            message: '请选择制作组长！',
                            type: 'error'
                        });
                        return false;
                    }
                    this.$confirm('确定要执行[不做场景，提交审图]吗?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                            this.$message({
                            type: 'success',
                            message: '流程操作[不做场景，提交审图]成功!单据发往[占位员工]'
                        });
                    this.submitToAuditPicturesFlow();
                    }).catch(() => {
                            this.$message({
                            type: 'info',
                            message: '已取消'
                        });
                    });

                } else if (command == 'submitToConfirmFlow') {
                    this.submitToConfirmFlow();
                } else if (command == 'submitCancelDesignExpensesFlow') {
                    this.submitCancelDesignExpensesFlow();
                } else if (command == 'submitToWholeRoomDelayTimeFlow') {
                    this.submitToWholeRoomDelayTimeFlow();
                } else if (command == 'submitToBackToDesignerFlow') {
                    this.submitToBackToDesignerFlow();
                }
            },
            save: function () {
                if (this.wholeRoomDrawing.productionLeader == '') {
                    Vue.prototype.$message({
                        message: '请选择制作组长！',
                        type: 'error'
                    });
                    return false;
                }
                let data = this.getDatas();
                ajax.post('api/bss.order/order-wholeRoomDrawing/submitWholeRoomDrawing', data,null,null,true);
            },
            submitToAuditPicturesFlow : function () {
                if (this.wholeRoomDrawing.drawingAuditorName == null || this.wholeRoomDrawing.drawingAuditorName == '') {
                    Vue.prototype.$message({
                        message: '您正在提交订单至图纸审核阶段，请先选择图纸审核人员！',
                        type: 'error'
                    });
                    return false;
                }
                ajax.post('api/bss.order/order-wholeRoomDrawing/submitToAuditPicturesFlow', this.wholeRoomDrawing);
            },
            submitToConfirmFlow : function () {
                ajax.post('api/bss.order/order-wholeRoomDrawing/submitToConfirmFlow', this.wholeRoomDrawing);
            },
            submitCancelDesignExpensesFlow : function () {
                ajax.post('api/bss.order/order-wholeRoomDrawing/submitCancelDesignExpensesFlow', this.wholeRoomDrawing);
            },
            submitToWholeRoomDelayTimeFlow : function () {
                ajax.post('api/bss.order/order-wholeRoomDrawing/submitToWholeRoomDelayTimeFlow', this.wholeRoomDrawing);
            },
            submitToBackToDesignerFlow : function () {
                ajax.post('api/bss.order/order-wholeRoomDrawing/submitToBackToDesignerFlow', this.wholeRoomDrawing);
            }
        },
        mounted () {
            this.init();
        }
    });
    return vm;
})