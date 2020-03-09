require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-selectemployee','cust-visit','order-search-employee','order-file-upload'], function(Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee,custVisit,orderSearchEmployee,orderFileUpload) {
    let vm = new Vue({
        el: '#app',
        data:  {

            planFigureInfos: {
                id : '',
                orderId : util.getRequest('orderId'),
                designer: '',
                projectAssistant: '',
                indoorArea: '',
                contractDesignFee: '',
                designerPlanNum: '',
                designTheme: '',
                designFeeStandard: '',
                customerComments: '',
                assistantDesigner: '',
                startTime: '',
                endTime: '',
                firstLookTime: util.getNowDate(),
                employeeId : ''
            },
            accountingInfos: [],
            isShow: false,
            id : util.getRequest('id'),
            custId: util.getRequest('custId'),
            orderStatus : util.getRequest('orderStatus'),
            eid:null,
            isBackToDesigner : false ,
            isConfirm : false,
            employeeName:'',
            downloadFileUrl : '',
        },

        methods: {
            init:function(){
                let data = {
                    orderId : util.getRequest('orderId'),
                }
                /*ajax.get('api/bss.order/order-planSketch/getPlaneSketch', data, (responseData)=>{
                    Object.assign(this.planFigureInfos, responseData);
                 });*/
                //alert(JSON.stringify(this.planFigureInfos));
                if (this.orderStatus=='35') {
                    this.isBackToDesigner = true;
                }
                if (this.orderStatus=='34') {
                    this.isConfirm = true;
                }
                this.downloadFileUrl = 'api/bss.order/order-file/download/' + util.getRequest("orderId") + "/456";
            },
            checkBeforeOrder : function () {
            },
            changeDesignFeeStandard: function(newVal) {
                this.planFigureInfos.designFeeStandard = newVal;
            },
            changeDefaultDesignTheme: function(newVal) {
                this.planFigureInfos.designTheme=newVal;
            },
            handleChangePlanNum : function(value) {
                this.planFigureInfos.designerPlanNum=value;
            },
            handleChangeInJacketAreaNum : function(value) {
                this.planFigureInfos.indoorArea=value;
            },
            handleCommand : function(command) {
                if ( command == 'submitToSignContractFlow') {
                    this.submitToSignContractFlow();
                } else if (command == 'submitToConfirmFlow') {
                    this.submitToConfirmFlow();
                } else if (command == 'submitToDelayTimeFlow') {
                    this.submitToDelayTimeFlow();
                } else if (command =='submitToBackToDesignerFlow') {
                    this.submitToBackToDesignerFlow();
                } else if (command == 'submitToSneakFlow') {
                    this.submitToSneakFlow();
                }
            },
            save : function () {
                if (this.planFigureInfos.indoorArea == '0' || this.planFigureInfos.indoorArea == 1) {
                    Vue.prototype.$message({
                        message: '请输入正确的套内面积！',
                        type: 'error'
                    });
                    return false;
                }
                if (this.planFigureInfos.contractDesignFee == '') {
                    Vue.prototype.$message({
                        message: '合同设计费不能为空！',
                        type: 'error'
                    });
                    return false;
                }
                ajax.post('api/bss.order/order-planSketch/submitPlaneSketch', this.planFigureInfos,null,null,true);
            },
            //修改平面图时间
            submitToDelayTimeFlow : function() {
                ajax.post('api/bss.order/order-planSketch/submitToDelayTimeFlow', this.planFigureInfos);
            },
            //等待客户确认平面图
            submitToConfirmFlow : function() {
                ajax.post('api/bss.order/order-planSketch/submitToConfirmFlow', this.planFigureInfos);
            },
            //签订设计合同
            submitToSignContractFlow : function () {
                if (this.employeeName == null || this.employeeName == '') {
                    Vue.prototype.$message({
                        message: '您正在提交订单至财务签订设计合同，请先选择财务人员！',
                        type: 'error'
                    });
                    return false;
                }
                let url = 'api/bss.order/order-file/view/' + util.getRequest("orderId") + "/456";
                let rn = false;
                ajax.get(url, null,function(data){
                    if (data == null) {

                        Vue.prototype.$message({
                            message: '请先上传平面图！',
                            type: 'error'
                        });
                        return;
                    }

                });
                this.planFigureInfos.employeeId = this.eid;
                ajax.post('api/bss.order/order-planSketch/submitToSignContractFlow', this.planFigureInfos,null,null,true);
                ajax.get('api/bss.order/order-planSketch/updateOrderWork', {
                    orderId : this.planFigureInfos.orderId,
                    roleId : '34',
                    employeeId : this.eid,
                });
                return false;
            },
            toSignContractFlow : function () {
                this.planFigureInfos.employeeId = this.eid;
                ajax.post('api/bss.order/order-planSketch/submitToSignContractFlow', this.planFigureInfos,(responseData)=>{

                });
            },
            backOrderWork : function () {
                //回写订单状态
                ajax.post('api/bss.order/order-planSketch/updateOrderWork', {
                    orderId : this.planFigureInfos.orderId,
                    roleId : '34',
                    employeeId : this.eid,
                });
            },
            submitToBackToDesignerFlow : function () {
                ajax.post('api/bss.order/order-planSketch/submitToBackToDesignerFlow', this.planFigureInfos);
            },
            submitToSneakFlow : function () {
                ajax.post('api/bss.order/order-measurehouse/submitToSneakFlow', this.planFigureInfos);
            }
        },
        mounted () {
            this.init();
        }
    });
    return vm;
})