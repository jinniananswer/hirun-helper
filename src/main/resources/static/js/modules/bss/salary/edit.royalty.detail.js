require(['vue','ELEMENT','ajax', 'vxe-table', 'vueselect', 'org-orgtree','house-select', 'util', 'cust-info', 'order-info', 'order-worker', 'order-search-employee'], function(Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util, custInfo, orderInfo, orderWorker, orderSearchEmployee) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                custId: util.getRequest('custId'),
                orderId: util.getRequest('orderId'),
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
                    totalRoyalty: 0,
                    alreadyFetch: 0,
                    auditStatus: '0',
                    salaryMonth: util.getNowMonth()
                },
                projectItem: {
                    basicRoyalty: 0,
                    basicAlreadyFetch: 0,
                    doorRoyalty: 0,
                    doorAlreadyFetch: 0,
                    furnitureRoyalty: 0,
                    furnitureAlreadyFetch: 0,
                    auditStatus: '0',
                    salaryMonth: util.getNowMonth()
                },
                designRules: {
                    employeeName: [
                        { required: true, message: '员工信息不能为空', trigger: 'change' }
                    ],
                    orderStatus: [
                        { required: true, message: '提成节点条件不能为空', trigger: 'blur' }
                    ],
                    value: [
                        { required: true, message: '提成点/固定值不能为空', trigger: 'blur' }
                    ],
                    type: [
                        { required: true, message: '提成类型不能为空', trigger: 'blur' }
                    ],
                    item: [
                        { required: true, message: '提成项目不能为空', trigger: 'blur' }
                    ],
                    totalRoyalty: [
                        { required: true, message: '总提成不能为空', trigger: 'blur'},
                        { type: 'number', message: '总提成必须为数字值', trigger: 'blur'}
                    ],
                    alreadyFetch: [
                        { required: true, message: '已提不能为空', trigger: 'blur'},
                        { type: 'number', message: '已提必须为数字值', trigger: 'blur'}
                    ],
                    salaryMonth: [
                        { required: true, message: '发放月份不能为空', trigger: 'blur' }
                    ]
                },
                projectRules: {
                    employeeName: [
                        { required: true, message: '员工信息不能为空', trigger: 'change' }
                    ],
                    orderStatus: [
                        { required: true, message: '提成节点条件不能为空', trigger: 'blur' }
                    ],
                    value: [
                        { required: true, message: '提成点/固定值不能为空', trigger: 'blur' }
                    ],
                    basicRoyalty: [
                        { required: true, message: '基础提成不能为空', trigger: 'blur'},
                        { type: 'number', message: '基础提成必须为数字值', trigger: 'blur'}
                    ],
                    basicAlreadyFetch: [
                        { required: true, message: '基础已提不能为空', trigger: 'blur'},
                        { type: 'number', message: '基础已提必须为数字值', trigger: 'blur'}
                    ],
                    doorRoyalty: [
                        { required: true, message: '门提成不能为空', trigger: 'blur'},
                        { type: 'number', message: '门提成必须为数字值', trigger: 'blur'}
                    ],
                    doorAlreadyFetch: [
                        { required: true, message: '门已提不能为空', trigger: 'blur'},
                        { type: 'number', message: '门已提必须为数字值', trigger: 'blur'}
                    ],
                    furnitureRoyalty: [
                        { required: true, message: '家具提成不能为空', trigger: 'blur'},
                        { type: 'number', message: '家具提成必须为数字值', trigger: 'blur'}
                    ],
                    furnitureAlreadyFetch: [
                        { required: true, message: '家具已提不能为空', trigger: 'blur'},
                        { type: 'number', message: '家具已提必须为数字值', trigger: 'blur'}
                    ],
                    salaryMonth: [
                        { required: true, message: '发放月份不能为空', trigger: 'blur' }
                    ]
                },
                designDialogVisible: false,
                projectDialogVisible: false,
                dialogVisible: false,
                show: 'display:none',
                queryShow: 'display:none',
                custOrder: [],
                designRoyaltyDetails: [],
                projectRoyaltyDetails: [],
                activeTab:'designRoyaltyTab'
            }
        },

        methods: {
            init: function() {
                this.show = 'display:none';
                this.queryShow = 'display:block';
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
                let that = this;
                ajax.get('api/bss.salary/salary-royalty-detail/queryRoyaltyByOrderId', {orderId: this.orderId}, function(responseData) {
                    if (responseData.designRoyaltyDetails) {
                        that.designRoyaltyDetails = responseData.designRoyaltyDetails;
                    } else {
                        that.designRoyaltyDetails = [];
                    }
                    if (responseData.projectRoyaltyDetails) {
                        that.projectRoyaltyDetails = responseData.projectRoyaltyDetails;
                    } else {
                        that.projectRoyaltyDetails = [];
                    }
                });
            },

            minusThisMonthFetch : function(row) {
                return row.totalRoyalty - row.alreadyFetch;
            },

            minusProjectThisMonthFetch : function(row) {
                return (row.basicRoyalty - row.basicAlreadyFetch) + (row.doorRoyalty - row.doorAlreadyFetch) + (row.furnitureRoyalty - row.furnitureAlreadyFetch);
            },

            saveDesignRoyalty : function() {
                let data = this.$refs.designRoyaltyDetail.getCheckboxRecords();
                if (data == null || data.length <= 0) {
                    this.$message.error('没有选中任何记录，无法提交');
                    return;
                }
                ajax.post('api/bss.salary/salary-royalty-detail/saveDesignRoyaltyDetails', this.$refs.designRoyaltyDetail.getCheckboxRecords());
            },

            auditDesignRoyalty : function() {
                let data = this.$refs.designRoyaltyDetail.getCheckboxRecords();
                if (data == null || data.length <= 0) {
                    this.$message.error('没有选中任何记录，无法提交');
                    return;
                }
                ajax.post('api/bss.salary/salary-royalty-detail/auditDesignRoyaltyDetails', this.$refs.designRoyaltyDetail.getCheckboxRecords());
            },

            saveProjectRoyalty : function() {
                let data = this.$refs.projectRoyaltyDetail.getCheckboxRecords();
                if (data == null || data.length <= 0) {
                    this.$message.error('没有选中任何记录，无法提交');
                    return;
                }
                ajax.post('api/bss.salary/salary-royalty-detail/saveProjectRoyaltyDetails', this.$refs.projectRoyaltyDetail.getCheckboxRecords());
            },

            auditProjectRoyalty : function() {
                let data = this.$refs.projectRoyaltyDetail.getCheckboxRecords();
                if (data == null || data.length <= 0) {
                    this.$message.error('没有选中任何记录，无法提交');
                    return;
                }
                ajax.post('api/bss.salary/salary-royalty-detail/auditProjectRoyaltyDetails', this.$refs.projectRoyaltyDetail.getCheckboxRecords());
            },

            isModify: function(obj) {
                if (obj.row.isModified == '1') {
                    return "modify_row";
                } else if (obj.row.isModified == '0') {
                    return 'new_row';
                } else if (obj.row.isModified == '2') {
                    return 'delete_row';
                }
            },

            activeRowMethod ({ row, rowIndex }) {
                if (row.auditStatus ==  '1' || row.auditStatus == '2' || row.auditStatus == '4') {
                    return false;
                }

                return true;
            },

            checkMethod: function({row}) {
                return row.auditStatus == '0' || row.auditStatus == '3';
            },

            initNewDesignRoyalty: function() {
                this.designItem = {
                    totalRoyalty: 0,
                    alreadyFetch: 0,
                    auditStatus: '0',
                    salaryMonth: util.getNowMonth()
                };
                this.designDialogVisible = true;
            },

            createNewDesignRoyalty() {
                this.designItem.orderId = this.orderId;
                this.$refs['designForm'].validate((valid) => {
                    if (valid) {
                        this.designItem.isModified = "0";
                        let that = this;
                        ajax.get('api/bss.salary/salary-royalty-detail/afterCreateDesignDetail', this.designItem, function(responseData){
                            that.designItem = responseData;
                            that.$refs.designRoyaltyDetail.insertAt(that.designItem, 0);
                            that.designDialogVisible = false;
                        });
                    } else {

                    }
                });

            },

            deleteDesign: function(row) {
                row.isModified = "2";
            },

            initNewProjectRoyalty: function() {
                this.projectItem = {
                    basicRoyalty: 0,
                    basicAlreadyFetch: 0,
                    doorRoyalty: 0,
                    doorAlreadyFetch: 0,
                    furnitureRoyalty: 0,
                    furnitureAlreadyFetch: 0,
                    auditStatus: '0',
                    salaryMonth: util.getNowMonth()
                };
                this.projectDialogVisible = true;
            },

            createNewProjectRoyalty() {
                this.projectItem.orderId = this.orderId;
                this.$refs['projectForm'].validate((valid) => {
                    if (valid) {
                        this.projectItem.isModified = "0";
                        let that = this;
                        ajax.get('api/bss.salary/salary-royalty-detail/afterCreateProjectDetail', this.projectItem, function(responseData){
                            that.projectItem = responseData;
                            that.$refs.projectRoyaltyDetail.insertAt(that.projectItem, 0);
                            that.projectDialogVisible = false;
                        });
                    } else {

                    }
                });
            },

            deleteProject: function(row) {
                row.isModified = "2";
            }
        },

        mounted () {
            this.init();
        }
    });

    return vm;
});