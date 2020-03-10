require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-selectemployee','cust-visit','order-search-employee'], function(Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee,custVisit,orderSearchEmployee) {
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
            eid:null,
            employeeName:'',
        },

        mounted: function() {
            let data = {
                orderId : util.getRequest('orderId'),
            }
            alert(this.wholeRoomDrawing.orderId);
            ajax.get('api/bss.order/order-wholeRoomDrawing/getWholeRoomDraw', data, (responseData)=>{
                Object.assign(this.wholeRoomDrawing, responseData);
            });
        },

        methods: {
            checkBeforeOrder: function () {
            },
            handleCommand : function(command) {
                if ( command == 'submitToCustomerLeaderFlow') {
                    this.$confirm('确定要执行[审核通过，提交客户部主管]吗?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.$message({
                        type: 'success',
                        message: '流程操作[审核通过，提交客户部主管]成功!单据发往[占位员工]'
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
                alert(JSON.stringify(this.wholeRoomDrawing));
                ajax.post('api/bss.order/order-wholeRoomDrawing/submitWholeRoomDrawing', this.wholeRoomDrawing,null,null,true);
            },
            submitToCustomerLeaderFlow : function () {
                if (this.employeeName == null || this.employeeName == '') {
                    Vue.prototype.$message({
                        message: '请先选择客户部主管！',
                        type: 'error'
                    });
                    return false;
                }
                ajax.post('api/bss.order/order-wholeRoomDrawing/submitToCustomerLeaderFlow', this.wholeRoomDrawing);
                ajax.post('api/bss.order/order-planSketch/updateOrderWork', {
                    orderId : this.wholeRoomDrawing.orderId,
                    roleId : '19',
                    employeeId : this.eid,
                });
            },
            submitToBackWholeRoomFlow : function () {
                ajax.post('api/bss.order/order-wholeRoomDrawing/submitToBackWholeRoomFlow', this.wholeRoomDrawing);
            }
        }
    });
    return vm;
})