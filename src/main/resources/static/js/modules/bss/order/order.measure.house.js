require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util','cust-info', 'order-info', 'order-worker', 'order-selectemployee','cust-visit'], function(Vue, element, axios, ajax, vueselect, util, custInfo, orderInfo, orderWorker, orderSelectEmployee,custVisit) {
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                quantityRoomInfos: {
                    id : '',
                    orderId : util.getRequest('orderId'),
                    customerComments: '',
                    measureTime: util.getNowDate(),
                    measureArea: '',
                    designer: '',
                    assistantDesigner :''
                },

                custId: util.getRequest('custId'),
                isShow : true,
                id : util.getRequest('id'),
                orderStatus : util.getRequest('orderStatus'),
                quantityRoomInfosRules : {
                    measureArea: [
                        { required: true, message: '请填写量房面积', trigger: 'blur'},
                        {type: 'number', message: '金额必须为数字', trigger: 'blur'}
                    ],

                    assistantDesigner : [
                        { required: true, message: '请填写量房人员', trigger: 'blur'}
                    ]
                },
            }
        },

        methods: {

            init:function() {
                let that = this;
                ajax.get('api/bss.order/order-measurehouse/getMeasureHouse', {orderId: this.quantityRoomInfos.orderId}, (responseData)=>{
                    that.quantityRoomInfos = responseData;
                    if (!that.quantityRoomInfos.measureTime) {
                        that.quantityRoomInfos.measureTime = util.getNowDate();
                    }
                });
                if (this.orderStatus=='4') {
                    this.isShow = false;
                }
            },

            handleCommand : function(command) {
                if ( command == 'submitToPlanesketchFlow') {
                    this.$confirm('确定要执行[已量房，设计平面图]吗?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.submitToPlanesketchFlow();
                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消'
                        });
                    });

                } else if (command == 'submitToDelayTimeFlow') {
                    this.$confirm('确定要执行[暂时不能去量房]吗?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.submitToDelayTimeFlow();
                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消'
                        });
                    });
                } else if (command == 'submitToSneakFlow') {
                    this.submitToSneakFlow();
                } else if (command == 'submitToOnlyWoodworkFlow') {
                    this.submitToOnlyWoodworkFlow();
                }
            },

            save : function () {
                this.$refs["measureForm"].validate((valid) => {
                    if (valid) {
                        ajax.post('api/bss.order/order-measurehouse/saveMeasureHouseInfos', this.quantityRoomInfos, null, null, true);
                    } else {
                        this.$message.error('填写信息不完整，请亲仔细检查哦~~~~~~~！');
                        return;
                    }
                });
            },

            submitToOnlyWoodworkFlow : function() {
                ajax.post('api/bss.order/order-measurehouse/submitToOnlyWoodworkFlow', this.quantityRoomInfos);
            },

            submitToPlanesketchFlow : function() {
                this.$refs["measureForm"].validate((valid) => {
                    if (valid) {
                        ajax.post('api/bss.order/order-measurehouse/submitToPlanesketchFlow', this.quantityRoomInfos);
                    } else {
                        this.$message.error('填写信息不完整，请亲仔细检查哦~~~~~~~！');
                        return;
                    }
                });
            },

            submitToDelayTimeFlow : function() {
                ajax.post('api/bss.order/order-measurehouse/submitToMeasureSuspendFlow', this.quantityRoomInfos);
            },

            submitToSneakFlow : function () {
                ajax.post('api/bss.order/order-measurehouse/submitToSneakFlow', this.quantityRoomInfos);
            }
        },
        mounted () {
            this.init();
        }
    });
    return vm;
})