require(['vue','ELEMENT','ajax', 'vxe-table', 'vueselect', 'org-orgtree','house-select', 'util', 'cust-info', 'order-info', 'order-worker', 'order-search-employee'], function(Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util, custInfo, orderInfo, orderWorker, orderSearchEmployee) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                custId: util.getRequest('custId'),
                orderId: util.getRequest('orderId'),
                status: util.getRequest('status'),
                complainNo: util.getRequest('complainNo'),
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
                complainTypeList: [
                    {
                        label: '木制品',
                        value: '1',
                        disabled: false
                    },
                    {
                        label: '设计',
                        value: '2',
                        disabled: false
                    },
                    {
                        label: '预算',
                        value: '3',
                        disabled: false
                    },
                    {
                        label: '施工',
                        value: '4',
                        disabled: false
                    },
                    {
                        label: '材料',
                        value: '5',
                        disabled: false
                    },
                    {
                        label: '其他',
                        value: '6',
                        disabled: false
                    }
                ],
                complainWayList: [
                    {
                        label: '上门投诉',
                        value: '1',
                        disabled: false
                    },
                    {
                        label: '电话投诉',
                        value: '2',
                        disabled: false
                    },
                    {
                        label: '网络投诉',
                        value: '3',
                        disabled: false
                    },
                    {
                        label: '其他',
                        value: '4',
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
                serviceComplainRecordList: [],
                serviceRepairHistoryRecordList:[],
                activeTab:'repairActiveTable',
                guaranteeInfo:{
                    guaranteeStartDate:'',
                    guaranteeEndDate:'',
                    createTime:''
                },
                validRules: {
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
                ajax.get('api/bss.service/service-complain/queryComplainRecordInfo', {orderId: this.orderId,customerId:this.custId,complainNo:this.complainNo}, function(responseData) {
                    if (responseData.historyComplainRecord!=null) {
                        that.serviceRepairHistoryRecordList =responseData.historyComplainRecord;
                    }
                    if(responseData.complainRecord!=null){
                        that.serviceComplainRecordList =responseData.complainRecord;
                    }
                    that.guaranteeInfo=responseData.guaranteeInfo;
                });
            },

            async insertRecord (row) {
                let record = {
                    complainTime: util.getNowTime(),
                }
                let { row: newRow } = await this.$refs.serviceComplainRecordList.insertAt(record, row)
                await this.$refs.serviceComplainRecordList.setActiveCell(newRow, 'sex')
            },

            saveData :function(){
                let  insertRecords= this.$refs.serviceComplainRecordList.getInsertRecords();
                let removeRecords=this.$refs.serviceComplainRecordList.getRemoveRecords();
                let updateRecords =this.$refs.serviceComplainRecordList.getUpdateRecords();

                if((this.complainNo==''||this.complainNo==null)&&insertRecords.length==0){
                    this.$message.error('未新增任意一条有效记录');
                    return false;
                }
                ajax.post('api/bss.service/service-complain/saveComplainOrder', {insertRecords:insertRecords,removeRecords:removeRecords,updateRecords:updateRecords,customerId:this.custId,orderId:this.orderId,complainNo:this.complainNo},null,null,true);

            },




            async removeRecord (row) {
                let that = this;
                await that.$refs.serviceComplainRecordList.remove(row)
            }
        },

        mounted () {
            this.init();
        }
    });

    return vm;
});