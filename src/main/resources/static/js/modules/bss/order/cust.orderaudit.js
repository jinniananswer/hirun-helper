require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-selectemployee','cust-visit'], function(Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee,custVisit) {
    let vm = new Vue({
        el: '#app',
        data:  {

            wholeRoomDrawing: {
                id : util.getRequest('id'),
                orderId : util.getRequest('orderId'),
                designer: '',
                preTime: '',//预约看图时间
                startTime: '',
                endTime: '',
                productionLeader: '',//制作组长
                assistantDesigner: '',//助理设计师
                hydropowerDesigner: '',//水电设计师
                drawingAssistant: '',//绘图助理
                adminAssistant: '',//行政助理
                designerRemarks: '',//设计师备注
                reviewedComments: ''//审核意见
            },
            custId: util.getRequest('custId'),
        },

        mounted: function() {
            let data = {
                orderId : util.getRequest('orderId'),
            }
        },

        methods: {
            checkBeforeOrder: function () {
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
                let drawStartDate = this.wholeRoomDrawing.drawStartDate;
                let drawEndDate = this.wholeRoomDrawing.drawEndDate;
                let preTime = this.wholeRoomDrawing.preTime;
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
                    orderWorkActions: orderWorkActions,
                    drawStartDate : drawStartDate,
                    drawEndDate : drawEndDate,
                    preTime : preTime,
                };
                return data;
            },
            handleCommand : function(command) {
                if ( command == 'submitToTwoLevelActuarialCalculationFlow') {
                    this.submitToTwoLevelActuarialCalculationFlow();
                }
            },
            save: function () {
                let data = this.getDatas();
                ajax.post('api/bss.order/order-wholeRoomDrawing/submitWholeRoomDrawing', data,null,null,true);
            },
            submitToTwoLevelActuarialCalculationFlow : function () {
                ajax.post('api/bss.order/order-wholeRoomDrawing/submitToTwoLevelActuarialCalculationFlow', this.wholeRoomDrawing);
            }
        }
    });
    return vm;
})