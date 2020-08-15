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
                employeeId : ''
            },
            orderWorkActions: [],
            orderWorkAction :{
                orderId : '',
                orderStatus : '',
                employeeId :'',
                employeeName : '',
                action:'',
                roleId : ''
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
                    { required: true, message: '请选择财务人员！', trigger: 'blur' }
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
                    if ( responseData.designFeeStandard != null) {
                        this.planFigureInfos.designFeeStandard = responseData.designFeeStandard/100;
                    }
                    this.orderWorkActions = responseData.orderWorkActions;
                    let array = [];
                    for(let i = 0; i < this.orderWorkActions.length; i++) {

                        if ( this.orderWorkActions[i].action == "draw_plane") {
                            this.orderWorkActions[i].action = "参与平面图设计中"
                        }
                        array.push({
                            action: this.orderWorkActions[i].action,
                            employeeName: this.orderWorkActions[i].employeeName,
                            orderStatus : this.orderWorkActions[i].orderStatus,
                            employeeId : this.orderWorkActions[i].employeeId,
                            roleId : this.orderWorkActions[i].roleId,
                            orderId : this.orderWorkActions[i].orderId
                        });
                    }
                    this.orderWorkActions = array;
                });
                if (this.orderStatus=='35') {
                    this.isBackToDesigner = true;
                }
                if (this.orderStatus=='34') {
                    this.isConfirm = true;
                }
                this.downloadFileUrl = 'api/bss.order/order-file/download/' + util.getRequest("orderId") + "/456";
            },
            getDatas : function() {
                let orderId = this.planFigureInfos.orderId;
                let indoorArea = this.planFigureInfos.indoorArea;
                let projectAssistant = this.planFigureInfos.projectAssistant;
                let contractDesignFee = this.planFigureInfos.contractDesignFee ;
                let designerPlanNum = this.planFigureInfos.designerPlanNum;
                let designTheme = this.planFigureInfos.designTheme;
                let designer = this.planFigureInfos.designer;
                let endTime = this.planFigureInfos.endTime;
                let startTime = this.planFigureInfos.startTime ;
                let planeSketchStartDate = this.planFigureInfos.planeSketchStartDate;
                let planeSketchEndDate = this.planFigureInfos.planeSketchEndDate;
                let firstLookTime = this.planFigureInfos.firstLookTime;
                let designFeeStandard = this.planFigureInfos.designFeeStandard;
                let customerComments = this.planFigureInfos.customerComments;
                let orderWorkActions = this.orderWorkActions;
                let financeEmployeeId = this.planFigureInfos.financeEmployeeId;
                let array = [];
                for(let i = 0; i < this.orderWorkActions.length; i++) {
                    if ( this.orderWorkActions[i].action == "参与平面图设计中") {
                        this.orderWorkActions[i].action = "draw_plane"
                    }
                    array.push({
                        action: this.orderWorkActions[i].action,
                        employeeName: this.orderWorkActions[i].employeeName,
                        orderStatus : this.orderWorkActions[i].orderStatus,
                        employeeId : this.orderWorkActions[i].employeeId,
                        roleId : this.orderWorkActions[i].roleId,
                        orderId : this.orderWorkActions[i].orderId
                    });
                }
                this.orderWorkActions = array;
                let data = {
                    orderId: orderId,
                    id: '',
                    measureTime: util.getNowDate(),
                    indoorArea: indoorArea,
                    projectAssistant : projectAssistant,
                    contractDesignFee : contractDesignFee,
                    designerPlanNum : designerPlanNum,
                    designTheme : designTheme,
                    designFeeStandard : designFeeStandard*100,
                    customerComments : customerComments,
                    designer: designer,
                    financeEmployeeId : financeEmployeeId,
                    orderWorkActions: orderWorkActions,
                    endTime : endTime,
                    startTime : startTime,
                    firstLookTime : firstLookTime,
                    planeSketchStartDate :planeSketchStartDate,
                    planeSketchEndDate : planeSketchEndDate
                };

                return data;
            },
            addSuccess : function() {
                this.orderWorkAction = {};
                this.orderWorkAction.employeeId = this.planFigureInfos.assistantDesigner;
                if (this.orderWorkAction.employeeId == '' || this.orderWorkAction.employeeId == null) {
                    Vue.prototype.$message({
                        message: '请先选择助理设计师再添加设计师！',
                        type: 'error'
                    });
                    return false;
                }
                ajax.get('api/bss.order/order-measurehouse/getEmployeeNameEmployeeId', {employeeId:this.orderWorkAction.employeeId}, (responseData)=>{
                    this.orderWorkAction.employeeName = responseData.employeeName ;
                });
                this.orderWorkAction.orderId = this.planFigureInfos.orderId;
                this.orderWorkAction.action = '参与平面设计中';
                this.orderWorkAction.roleId = '41';
                this.orderWorkAction.orderStatus = this.orderStatus;
                this.orderWorkActions.push(this.orderWorkAction);
            },
            deleteRow : function(index, rows) {
                rows.splice(index, 1);
            },

            checkBeforeOrder : function () {
            },
            changeDesignFeeStandard: function(newVal) {
                this.planFigureInfos.designFeeStandard = newVal;
                this.planFigureInfos.contractDesignFee = this.planFigureInfos.designFeeStandard*this.planFigureInfos.indoorArea;
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
                if (this.planFigureInfos.designerPlanNum == '0') {
                    Vue.prototype.$message({
                        message: '请输入正确的方案个数！',
                        type: 'error'
                    });
                    return false;
                }
                if (this.planFigureInfos.designFeeStandard == '') {
                    Vue.prototype.$message({
                        message: '请选择设计费标准！',
                        type: 'error'
                    });
                    return false;
                }
                if (this.planFigureInfos.financeEmployeeId == '') {
                    Vue.prototype.$message({
                        message: '请先选择财务人员！',
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
                let data = this.getDatas();
                //alert(JSON.stringify(data));
                ajax.post('api/bss.order/order-planSketch/submitPlaneSketch', data,null,null,true);
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
                if (this.planFigureInfos.financeEmployeeName == null || this.planFigureInfos.financeEmployeeName == '') {
                    Vue.prototype.$message({
                        message: '您正在提交订单至财务签订设计合同，请先选择财务人员！',
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