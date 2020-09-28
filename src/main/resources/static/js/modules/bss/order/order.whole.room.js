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
                drawStartDate: '',
                drawEndDate: '',
                drawingAuditor :'',
                drawingAuditorName : '',
                productionLeader: '',//制作组长
                assistantDesigner: '',//助理设计师
                hydropowerDesigner: '',//水电设计师
                drawingAssistant: '',//绘图助理
                adminAssistant: '',//行政助理
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
            wholeRoomRules : {
                drawingAuditor: [
                    { required: true, message: '请选择图纸审核员！', trigger: 'change' }
                ],
                assistantDesigner: [
                    { required: true, message: '请选择全房图助理！', trigger: 'change' }
                ]
            },
        },

        methods: {

            init:function(){
                let data = {
                    orderId : util.getRequest('orderId'),
                }

                ajax.get('api/bss.order/order-wholeRoomDrawing/getWholeRoomDraw', data, (responseData)=>{
                    Object.assign(this.wholeRoomDrawing, responseData);
                });

                if (this.orderStatus=='36') {
                    this.isShow = false;
                } else if (this.orderStatus=='37') {
                    this.isBackToDesigner = true;
                } else if (this.orderStatus=='12') {
                    this.isAudit = true;
                }
            },

            handleCommand : function(command) {
                if ( command == 'submitToAuditPicturesFlow') {
                    this.$confirm('确定要执行[不做场景，提交审图]吗?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
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
                this.$refs["wholeRoomForm"].validate((valid) => {
                    if (valid) {
                        ajax.post('api/bss.order/order-wholeRoomDrawing/submitWholeRoomDrawing', this.wholeRoomDrawing,null,null,true);
                    } else {
                        this.$message.error('填写信息不完整，请亲仔细检查哦~~~~~~~！');
                        return;
                    }
                });
            },
            submitToAuditPicturesFlow : function () {
                this.$refs["wholeRoomForm"].validate((valid) => {
                    if (valid) {
                        ajax.post('api/bss.order/order-wholeRoomDrawing/submitToAuditPicturesFlow', this.wholeRoomDrawing);
                    } else {
                        this.$message.error('填写信息不完整，请亲仔细检查哦~~~~~~~！');
                        return;
                    }
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