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
                productionLeader: '制作组长测试一',//制作组长
                assistantDesigner: '助理设计师测试一',//助理设计师
                hydropowerDesigner: '水电设计师测试一',//水电设计师
                drawingAssistant: '绘图助理测试一',//绘图助理
                adminAssistant: '行政助理测试一',//行政助理
                designerRemarks: '',//设计师备注
                reviewedComments: ''//审核意见
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
                //alert(JSON.stringify(data));
                ajax.get('api/bss.order/order-wholeRoomDrawing/getWholeRoomDraw', data, (responseData)=>{
                    Object.assign(this.wholeRoomDrawing, responseData);
                });

                this.wholeRoomDrawing.orderId = util.getRequest('orderId');
                if (this.orderStatus=='36') {
                    this.isShow = false;
                } else if (this.orderStatus=='37') {
                    this.isBackToDesigner = true;
                } else if (this.orderStatus=='12') {
                    this.isAudit = true;
                }
                this.downloadFileUrl = 'api/bss.order/order-file/download/' + util.getRequest("orderId") + "/456";
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
                ajax.post('api/bss.order/order-wholeRoomDrawing/submitWholeRoomDrawing', this.wholeRoomDrawing,null,null,true);
            },
            submitToAuditPicturesFlow : function () {
                if (this.employeeName == null || this.employeeName == '') {
                    Vue.prototype.$message({
                        message: '您正在提交订单至图纸审核阶段，请先选择图纸审核人员！',
                        type: 'error'
                    });
                    return false;
                }
                ajax.post('api/bss.order/order-wholeRoomDrawing/submitToAuditPicturesFlow', this.wholeRoomDrawing);
                ajax.get('api/bss.order/order-planSketch/updateOrderWork', {
                    orderId : this.wholeRoomDrawing.orderId,
                    roleId : '44',
                    employeeId : this.eid,
                });
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