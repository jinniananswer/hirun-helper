require(['vue','ELEMENT','ajax', 'vxe-table', 'vueselect', 'org-orgtree','house-select', 'util', 'cust-info', 'order-info', 'order-worker', 'order-search-employee'], function(Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util, custInfo, orderInfo, orderWorker, orderSearchEmployee) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                custId: util.getRequest('custId'),
                orderId: util.getRequest('orderId'),
                status: util.getRequest('status'),
                repairNo: util.getRequest('repairNo'),
                queryCond: {
                    custName: '',
                    sex: '',
                    mobileNo: '',
                    housesId: null,
                    orderStatus: '',
                    limit: 20,
                    page: 1,
                    count: null
                },
                designItem: {
                    IsFee: '',
                    repairWorkerType: '',
                    repairItem: '',
                    repairReason: '',
                    dutyShare:'',
                    repairMaterial:'',
                    buildHeadman:'',
                    repairWorkerCount:'',
                    repairWorker:'',
                    receiptTime:'',
                    acceptTime:'',
                    offerTime:'',
                },
                repairWorkerTypeList: [
                    {
                        label: '水电',
                        value: '1',
                        disabled: false
                    },
                    {
                        label: '木工',
                        value: '2',
                        disabled: false
                    },
                    {
                        label: '油工',
                        value: '3',
                        disabled: false
                    },
                    {
                        label: '泥工',
                        value: '4',
                        disabled: false
                    },
                    {
                        label: '工厂',
                        value: '5',
                        disabled: false
                    },
                    {
                        label: '主材',
                        value: '6',
                        disabled: false
                    }
                ],
                isFeeList: [
                    {
                        label: '是',
                        value: '1',
                        disabled: false
                    },
                    {
                        label: '否',
                        value: '2',
                        disabled: false
                    },
                ],
                designDialogVisible: false,
                projectDialogVisible: false,
                dialogVisible: false,
                show: 'display:none',
                showBeginRepair: 'display:none',
                showCheckData: 'display:none',
                queryShow: 'display:none',
                showQueryInput:'display:block',
                custOrder: [],
                serviceRepairRecordList: [],
                serviceRepairHistoryRecordList:[],
                activeTab:'repairActiveTable',
                guaranteeInfo:{
                    guaranteeStartDate:'',
                    guaranteeEndDate:'',
                    createTime:''
                },
                validRules: {
                    repairWorker: [
                        { required: true, message: '维修人员不能为空' },
                    ],
                }
            }
        },

        methods: {
            init: function() {
                this.show = 'display:none';
                this.queryShow = 'display:block';
                if(this.status==1||this.status==2){
                    this.showQueryInput='display:none';
                    this.selectCustOrder(this.orderId,this.custId);
                }
            },

            query: function() {
                let that = this;
                ajax.get('api/bss.order/finance/queryCustOrderInfo', this.queryCond, function(responseData){
                    that.custOrder = responseData.records;
                    that.queryCond.page = responseData.current;
                    that.queryCond.count = responseData.total;
                });
            },

            showCustQuery: function() {
                this.dialogVisible = true;
            },

            selectCustOrder: function(orderId, custId) {
                this.custId = custId;
                this.orderId = orderId;
                this.dialogVisible = false;
                this.show = 'display:block';
                if(this.status=='1'){
                    this.showBeginRepair = 'display:block';
                }
                if(this.status=='2'){
                    this.showCheckData = 'display:block';
                }
                let that = this;
                ajax.get('api/bss.service/service-repair-order/queryRepairRecordInfo', {orderId: this.orderId,customerId:this.custId,repairNo:this.repairNo}, function(responseData) {
                    if (responseData.historyRepairRecord!=null) {
                        that.serviceRepairHistoryRecordList =responseData.historyRepairRecord;
                    }
                    if(responseData.repairNoRecord!=null){
                        that.serviceRepairRecordList =responseData.repairNoRecord;
                    }
                    that.guaranteeInfo=responseData.guaranteeInfo;
                });
            },

            async insertRecord (row) {
                let record = {
                    isFee: '2',
                    acceptTime: util.getNowTime(),
                }
                let { row: newRow } = await this.$refs.serviceRepairRecordList.insertAt(record, row)
                await this.$refs.serviceRepairRecordList.setActiveCell(newRow, 'sex')
            },

            saveData :function(){
                let  insertRecords= this.$refs.serviceRepairRecordList.getInsertRecords();
                let removeRecords=this.$refs.serviceRepairRecordList.getRemoveRecords();
                let updateRecords =this.$refs.serviceRepairRecordList.getUpdateRecords();
                ajax.post('api/bss.service/service-repair-order/saveRepairOrder', {insertRecords:insertRecords,removeRecords:removeRecords,updateRecords:updateRecords,customerId:this.custId,orderId:this.orderId,repairNo:this.repairNo});
            },




            async removeRecord (row) {
                let that = this;
                await that.$refs.serviceRepairRecordList.remove(row)
            }
        },

        mounted () {
            this.init();
        }
    });

    return vm;
});