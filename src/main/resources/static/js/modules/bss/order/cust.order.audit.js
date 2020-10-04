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
                reviewedComments: '',//审核意见
                pageTag : 'orderaudit'
            },
            custId: util.getRequest('custId'),
        },

        mounted: function() {
            let data = {
                orderId : util.getRequest('orderId'),
            }
            ajax.get('api/bss.order/order-wholeRoomDrawing/getWholeRoomDraw', data, (responseData)=>{
                Object.assign(this.wholeRoomDrawing, responseData);
            });
        },

        methods: {
            save: function () {
                ajax.post('api/bss.order/order-wholeRoomDrawing/saveAuditOrder', this.wholeRoomDrawing,null,null,true);
            },
            submitToTwoLevelActuarialCalculationFlow : function () {
                ajax.post('api/bss.order/order-wholeRoomDrawing/submitToTwoLevelActuarialCalculationFlow', this.wholeRoomDrawing);
            }
        }
    });
    return vm;
})