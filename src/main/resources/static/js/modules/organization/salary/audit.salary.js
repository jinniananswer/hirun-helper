require(['vue','ELEMENT','ajax', 'vxe-table', 'vueselect', 'org-orgtree', 'util'], function(Vue, element, ajax, table, vueSelect, orgTree, util) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                queryCond: {
                    salaryMonth: util.getNowMonth(),
                    page : 1,
                    limit : -1
                },
                employees: [],
                auditNoReason: '',
                dialogVisible: false
            }
        },

        methods: {
            query: function() {
                let that = this;
                ajax.get('api/organization/employee-salary/queryAuditSalary', this.queryCond, function(data){
                    that.employees = data;
                });
            },

            auditPass : function() {
                ajax.post('api/organization/employee-salary/auditPass', this.$refs.employeeSalary.getCheckboxRecords());
            },

            auditNo : function() {
                this.dialogVisible = true;
            },

            isModify: function(obj) {
                if (obj.row.isModified == '1') {
                    return "modify_row";
                }
            },

            activeRowMethod ({ row, rowIndex }) {
                if (row.auditStatus ==  '1' || row.auditStatus == '2' || row.auditStatus == '4') {
                    return false;
                }

                return true;
            },

            confirmAuditNoReason: function() {
                let salaries = this.$refs.employeeSalary.getCheckboxRecords();
                this.dialogVisible = false;
                if (salaries && salaries.length > 0) {
                    salaries.forEach((salary) => {
                        salary.auditRemark = this.auditNoReason;
                    })
                    ajax.post('api/organization/employee-salary/auditNo', this.$refs.employeeSalary.getCheckboxRecords());
                }

            },

            checkMethod: function({row}) {
                return row.auditStatus == '1';
            }
        },

        mounted () {
        }
    });

    return vm;
});