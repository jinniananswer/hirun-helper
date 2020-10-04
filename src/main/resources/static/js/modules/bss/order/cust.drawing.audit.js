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
            handleCommand : function(command) {
                if ( command == 'submitToCustomerLeaderFlow') {
                    this.$confirm('确定要执行[审核通过，提交下单审核员]吗?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
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
                ajax.post('api/bss.order/order-drawingaudit/submitDrawingauditInfo', this.wholeRoomDrawing, null, null, true);
            },

            submitToCustomerLeaderFlow : function () {
                if (this.wholeRoomDrawing.customerLeader == null || this.wholeRoomDrawing.customerLeader == '') {
                    this.$message.error('请选择下单审核员！');
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