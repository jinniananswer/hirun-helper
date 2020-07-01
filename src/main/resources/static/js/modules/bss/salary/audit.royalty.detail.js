require(['vue','ELEMENT','ajax', 'vxe-table', 'vueselect', 'org-orgtree','house-select', 'util'], function(Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                queryCond: {
                    custName: '',
                    mobileNo: '',
                    housesId: null,
                    orgIds: null,
                    orgName: null,
                    startDate: null,
                    endDate: null,
                    count: null
                },
                designRoyaltyDetails: [],
                projectRoyaltyDetails: [],
                activeTab:'designRoyaltyTab'
            }
        },

        methods: {
            init: function() {

            },

            query: function() {
                let that = this;
                ajax.get('api/bss.salary/salary-royalty-detail/queryRoyaltyDetails', this.queryCond, function(responseData){
                    if (responseData) {
                        that.designRoyaltyDetails = responseData.designRoyaltyDetails;
                        that.projectRoyaltyDetails = responseData.projectRoyaltyDetails;
                    } else {
                        that.designRoyaltyDetails = [];
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

            auditDesignPass : function() {
                let data = this.$refs.designRoyaltyDetail.getCheckboxRecords();
                if (data == null || data.length <= 0) {
                    this.$message.error('没有选中任何记录，无法提交');
                    return;
                }
                ajax.post('api/bss.salary/salary-royalty-detail/auditDesignRoyaltyPass', this.$refs.designRoyaltyDetail.getCheckboxRecords());
            },

            auditDesignNo : function() {
                let data = this.$refs.designRoyaltyDetail.getCheckboxRecords();
                if (data == null || data.length <= 0) {
                    this.$message.error('没有选中任何记录，无法提交');
                    return;
                }
                ajax.post('api/bss.salary/salary-royalty-detail/auditDesignRoyaltyNo', this.$refs.designRoyaltyDetail.getCheckboxRecords());
            },

            auditProjectPass : function() {
                let data = this.$refs.projectRoyaltyDetail.getCheckboxRecords();
                if (data == null || data.length <= 0) {
                    this.$message.error('没有选中任何记录，无法提交');
                    return;
                }
                ajax.post('api/bss.salary/salary-royalty-detail/auditProjectRoyaltyPass', this.$refs.projectRoyaltyDetail.getCheckboxRecords());
            },

            auditProjectNo : function() {
                let data = this.$refs.projectRoyaltyDetail.getCheckboxRecords();
                if (data == null || data.length <= 0) {
                    this.$message.error('没有选中任何记录，无法提交');
                    return;
                }
                ajax.post('api/bss.salary/salary-royalty-detail/auditProjectRoyaltyNo', this.$refs.projectRoyaltyDetail.getCheckboxRecords());
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
                if (row.auditStatus == '1') {
                    return false;
                }

                return true;
            },

            checkMethod: function({row}) {
                return row.auditStatus == '1';
            },
        },

        mounted () {
            this.init();
        }
    });

    return vm;
});