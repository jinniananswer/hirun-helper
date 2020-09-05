require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-selectemployee','cust-visit','order-search-employee'], function(Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee,custVisit,orderSearchEmployee) {
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
                drawEndDate: '',
                drawingAuditor :'',
                productionLeader: '',//制作组长
                assistantDesigner: '',//助理设计师
                hydropowerDesigner: '',//水电设计师
                drawingAssistant: '',//绘图助理
                adminAssistant: '',//行政助理
                designerRemarks: '',//设计师备注
                reviewedComments: '',//审核意见
                customerLeader: '',//客户部主管
                customerLeaderName : ''
            },
            custId: util.getRequest('custId'),
            eid:null,
            employeeName:'',
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
            getDatas : function() {
                let orderId = this.wholeRoomDrawing.orderId;
                let reviewedComments = this.wholeRoomDrawing.reviewedComments;
                let designerRemarks = this.wholeRoomDrawing.designerRemarks;
                let adminAssistant = this.wholeRoomDrawing.adminAssistant ;
                let drawingAssistant = this.wholeRoomDrawing.drawingAssistant;
                let customerLeader = this.wholeRoomDrawing.customerLeader;
                let hydropowerDesigner = this.wholeRoomDrawing.hydropowerDesigner;
                let productionLeader = this.wholeRoomDrawing.productionLeader;
                let designer = this.wholeRoomDrawing.designer;
                let drawingAuditor = this.wholeRoomDrawing.drawingAuditor;
                let orderWorkActions = this.orderWorkActions;
                let drawStartDate = this.wholeRoomDrawing.drawStartDate;
                let drawEndDate = this.wholeRoomDrawing.drawEndDate;
                let preTime = this.wholeRoomDrawing.preTime;
                let assistantDesigner = this.wholeRoomDrawing.assistantDesigner;

                let data = {
                    orderId: orderId,
                    id: '',
                    measureTime: util.getNowDate(),
                    reviewedComments: reviewedComments,
                    designerRemarks : designerRemarks,
                    adminAssistant : adminAssistant,
                    drawingAssistant : drawingAssistant,
                    productionLeader : productionLeader,
                    customerLeader : customerLeader ,
                    hydropowerDesigner : hydropowerDesigner,
                    drawingAuditor : drawingAuditor,
                    designer: designer,
                    orderWorkActions: orderWorkActions,
                    drawStartDate : drawStartDate,
                    drawEndDate : drawEndDate,
                    preTime : preTime,
                    assistantDesigner : assistantDesigner,
                };
                return data;
            },
            checkBeforeOrder: function () {
            },
            handleCommand : function(command) {
                if ( command == 'submitToCustomerLeaderFlow') {
                    this.$confirm('确定要执行[审核通过，提交下单审核员]吗?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.$message({
                        type: 'success',
                        message: '流程操作[审核通过，提交下单审核员]成功!单据发往[占位员工]'
                    });
                    this.submitToCustomerLeaderFlow();
                    }).catch(() => {
                            this.$message({
                            type: 'info',
                            message: '已取消'
                        });
                    });
                } else if (command == 'submitToBackWholeRoomFlow') {
                    this.submitToBackWholeRoomFlow();
                }
            },

            save: function() {
                let data = this.getDatas();
                ajax.post('api/bss.order/order-drawingaudit/submitDrawingauditInfo', data,null,null,true);
            },
            submitToCustomerLeaderFlow : function () {
                if (this.wholeRoomDrawing.customerLeader == null || this.wholeRoomDrawing.customerLeader == '') {
                    Vue.prototype.$message({
                        message: '请先选择下单审核员！',
                        type: 'error'
                    });
                    return false;
                }
                ajax.post('api/bss.order/order-wholeRoomDrawing/submitToCustomerLeaderFlow', this.wholeRoomDrawing);
            },
            submitToBackWholeRoomFlow : function () {
                ajax.post('api/bss.order/order-wholeRoomDrawing/submitToBackWholeRoomFlow', this.wholeRoomDrawing);
            }
        }
    });
    return vm;
})