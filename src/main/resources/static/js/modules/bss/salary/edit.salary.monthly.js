require(['vue','ELEMENT','ajax', 'vxe-table', 'vueselect', 'org-orgtree', 'util'], function(Vue, element, ajax, table, vueSelect, orgTree, util) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                queryCond: {
                    salaryMonth : util.getNowMonth(),
                    page : 1,
                    limit : -1
                },
                employees: []
            }
        },

        methods: {
            query: function() {
                let that = this;
                ajax.get('api/bss.salary/salary-monthly/querySalary', this.queryCond, function(data){
                    that.employees = data;
                });
            },

            submit : function() {
                ajax.post('api/bss.salary/salary-monthly/submitSalaries', this.employees);
            },

            audit : function() {
                ajax.post('api/bss.salary/salary-monthly/auditSalaries', this.employees);
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
            }
        },

        mounted () {
        }
    });

    return vm;
});