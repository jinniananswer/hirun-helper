require(['vue', 'ELEMENT', 'vxe-table','house-select','order-selectemployee','ajax'], function (Vue, element, vxetable, houseSelect,orderSelectEmployee,ajax) {
    Vue.use(vxetable)
    let vm = new Vue({
        el: '#app',
        data: function () {
            return {
                sexList: [],
                tableData: [],
                queryCond: {
                },
                validRules: {
                    name: [
                        {required: true, message: '必填'},
                        {min: 2, max: 10, message: '名称长度在 2 到 10 个字符'}
                    ],
                    age: [
                        {required: true, message: '必填'},
                        {pattern: /^[1-9][0-9]?$/, message: '格式不正确'}
                    ],
                    idCard: [
                        {required: true, message: '必填'},
                        {pattern: /^(\d{15}$)|(^\d{18}$)/, message: '格式不正确'}
                    ]
                }
            }
        },

        methods: {
            changeEvent() {

            },

            query: function() {
                let that = this;
                ajax.get('api/bss.order/order-material-contract/queryMaterialContract', this.queryCond, function(data){
                    that.tableData = data;
                });
            },

            footerMethod({columns, data}) {
                return [
                    columns.map((column, columnIndex) => {
                        if (columnIndex === 0) {
                            return '汇总'
                        }
                        if (['age'].includes(column.property)) {
                            let total = 0;
                            data.forEach(function (v, k) {
                                total += parseInt(v.age);
                            })
                            return "平均: " + (total / data.length).toFixed(2)
                        }
                        if (['salary'].includes(column.property)) {
                            let total = 0;
                            data.forEach(function (v, k) {
                                total += parseInt(v.salary);
                            })
                            return "合计: " + total.toFixed(2)
                        }
                        return '-'
                    })
                ]
            },

        },
        created: function () {

        },
    });
})