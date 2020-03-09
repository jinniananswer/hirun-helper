require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-selectemployee','cust-visit'], function(Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee,custVisit) {
    let vm = new Vue({
        el: '#app',
        data:  {

            wholeRoomDrawing: {
                id : util.getRequest('id'),
                orderId : util.getRequest('orderId'),
                designer: '測試一',
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
            //alert(JSON.stringify(data));
            ajax.get('api/bss.order/order-wholeRoomDrawing/getWholeRoomDraw', data, (responseData)=>{
                Object.assign(this.wholeRoomDrawing, responseData);
            });
        },

        methods: {
            checkBeforeOrder: function () {
            },
            handleCommand : function(command) {
                if ( command == 'submitToTwoLevelActuarialCalculationFlow') {
                    this.submitToTwoLevelActuarialCalculationFlow();
                }
            },
            save: function () {
                this.wholeRoomDrawing.designer = this.valueDesigner;
                this.wholeRoomDrawing.hydropowerDesigner = this.valueHydropowerDesigners;
                this.wholeRoomDrawing.drawingAssistant = this.valueDrawingAssistant;
                this.wholeRoomDrawing.adminAssistant = this.valueAdminAssistant;
                alert(JSON.stringify(this.wholeRoomDrawing));
                ajax.post('api/bss.order/order-wholeRoomDrawing/submitWholeRoomDrawing', this.wholeRoomDrawing,null,null,true);
            },
            submitToTwoLevelActuarialCalculationFlow : function () {
                ajax.post('api/bss.order/order-wholeRoomDrawing/submitToTwoLevelActuarialCalculationFlow', this.wholeRoomDrawing);
            }
        }
    });
    return vm;
})