require(['vue','ELEMENT','ajax', 'vxe-table', 'vueselect', 'org-orgtree', 'util', 'order-search-employee'], function(Vue, element, ajax, table, vueSelect, orgTree, util, orderSearchEmployee) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                queryCond: {
                    page : 1,
                    limit : -1
                },
                salaryRedebit: {
                    inDate : util.getNowDate()
                },
                employees: [],
                dialogVisible: false,
                rules: {
                    employeeName: [
                        { required: true, message: '员工信息不能为空', trigger: 'change' }
                    ],
                    salaryItem: [
                        { required: true, message: '工资项目不能为空', trigger: 'blur' }
                    ],
                    redebitItem: [
                        { required: true, message: '补扣类型不能为空', trigger: 'blur' }
                    ],
                    salaryMonth: [
                        { required: true, message: '工资月份不能为空', trigger: 'blur' }
                    ],
                    money: [
                        { required: true, message: '补扣金额不能为空', trigger: 'blur'},
                        { type: 'number', message: '总提成必须为数字值', trigger: 'blur'}
                    ],
                    inDate: [
                        { required: true, message: '录入时间不能为空', trigger: 'blur'}
                    ],
                    reason: [
                        { required: true, message: '原因说明不能为空', trigger: 'blur' }
                    ]
                }
            }
        },

        methods: {
            query: function() {
                let that = this;
                ajax.post('api/bss.salary/salary-redebit/queryRedebits', this.queryCond, function(data){
                    that.employees = data.records;
                    that.queryCond.page = data.current;
                    that.queryCond.count = data.total;
                });
            },

            initNewRedebit: function() {
                this.dialogVisible = true;
            },

            create : function() {
                this.$refs['redebitForm'].validate((valid) => {
                    if (valid) {
                        ajax.post('api/bss.salary/salary-redebit/createRedebit', this.salaryRedebit);
                    }
                });
            },

            deleteRedebit : function() {
                let datas = this.$refs.employeeRedebitTable.getCheckboxRecords();
                if (datas == null || datas.length <= 0) {
                    this.$message.error('没有选中任何记录，无法提交');
                } else {
                    ajax.post('api/bss.salary/salary-redebit/deleteRedebits', this.$refs.employeeRedebitTable.getCheckboxRecords());
                }
            },

            checkMethod ({ row, rowIndex }) {
                if (row.auditStatus ==  '1' || row.auditStatus == '2' || row.auditStatus == '4') {
                    return false;
                }

                return true;
            }
        },

        mounted () {
        }
    });

    return vm;
});