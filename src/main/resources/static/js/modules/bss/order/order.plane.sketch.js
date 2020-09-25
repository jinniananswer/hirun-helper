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
                financeEmployeeName : '',
                endTime: '',
                planeSketchStartDate : '',
                planeSketchEndDate : '',
                firstLookTime: util.getNowDate(),
                financeEmployeeId : '' ,
                employeeId : '',
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
            planSketchRules : {
                designFeeStandard: [
                    { required: true, message: '请选择设计费标准！', trigger: 'change' }
                ],
                assistantDesigner: [
                    { required: true, message: '请选择平面助理！', trigger: 'change' }
                ],
                indoorArea: [
                    { required: true, message: '请填套内面积！', trigger: 'change' }
                ],
                contractDesignFee : [
                    { required: true, message: '请输入合同设计费！', trigger: 'blur' }
                ],
                designTheme : [
                    { required: true, message: '请选择设计主题！', trigger: 'blur' }
                ],
                designTheme : [
                    { required: true, message: '请选择设计主题！', trigger: 'blur' }
                ],
                financeEmployeeId : [
                    { required: true, message: '请选择客户文员！', trigger: 'blur' }
                ],
            },
        },

        methods: {
            init:function(){
                let data = {
                    orderId : util.getRequest('orderId'),
                }
                ajax.get('api/bss.order/order-planSketch/getPlaneSketch', data, (responseData)=>{
                    Object.assign(this.planFigureInfos, responseData);
                });
                if (this.orderStatus=='35') {
                    this.isBackToDesigner = true;
                }
                if (this.orderStatus=='34') {
                    this.isConfirm = true;
                }
            },

            changeDesignFeeStandard: function(newVal) {
                this.planFigureInfos.designFeeStandard = newVal;
                this.planFigureInfos.contractDesignFee = this.planFigureInfos.designFeeStandard * this.planFigureInfos.indoorArea;
            },

            handleChangePlanNum : function(value) {
                this.planFigureInfos.designerPlanNum = value;
            },

            handleChangeInJacketAreaNum : function(value) {
                this.planFigureInfos.indoorArea=value;
                this.planFigureInfos.contractDesignFee = this.planFigureInfos.designFeeStandard * this.planFigureInfos.indoorArea;
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
                this.$refs["planFigureInfos"].validate((valid) => {
                    if (valid) {
                        ajax.post('api/bss.order/order-planSketch/submitPlaneSketch', this.planFigureInfos,null,null,true);
                    } else {
                        this.$message.error('填写信息不完整，请亲仔细检查哦~~~~~~~！');
                        return;
                    }
                });
            },

            //延迟平面图时间
            submitToDelayTimeFlow : function() {
                ajax.post('api/bss.order/order-planSketch/submitToDelayTimeFlow', this.planFigureInfos);
            },

            //等待客户确认平面图
            submitToConfirmFlow : function() {
                ajax.post('api/bss.order/order-planSketch/submitToConfirmFlow', this.planFigureInfos);
            },

            //签订设计合同
            submitToSignContractFlow : function () {
                if (this.planFigureInfos.financeEmployeeId == null || this.planFigureInfos.financeEmployeeId == '') {
                    Vue.prototype.$message({
                        message: '您正在提交订单至财务签订设计合同，请先选客户文员！',
                        type: 'error'
                    });
                    return false;
                }

                this.planFigureInfos.employeeId = this.planFigureInfos.financeEmployeeId;
                ajax.post('api/bss.order/order-planSketch/submitToSignContractFlow', this.planFigureInfos,null);
            },

            toSignContractFlow : function () {
                this.planFigureInfos.employeeId = this.eid;
                ajax.post('api/bss.order/order-planSketch/submitToSignContractFlow', this.planFigureInfos,(responseData)=>{

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