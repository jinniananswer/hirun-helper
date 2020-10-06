require(['vue', 'ELEMENT', 'vxe-table','house-select','order-selectemployee','ajax','xe-utils'], function (Vue, element, vxetable, houseSelect,orderSelectEmployee,ajax,XEUtils) {
    Vue.use(vxetable)
    let vm = new Vue({
        el: '#app',
        data: function () {
            return {
                sexList: [],
                tableData: [],
                detailData: [],
                showDetails: false,
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

            editRowEvent (row) {
                this.$refs.xTable.setActiveRow(row)
            },

            saveRowEvent (row) {
                this.$refs.xTable.clearActived().then(() => {
                    row.contractFee = Math.round(row.contractFee * 100);
                    row.discountFee=  Math.round(row.discountFee * 100);
                    row.actualFee=  Math.round(row.actualFee * 100);

                    ajax.post('api/bss.order/order-material-contract/update',row,null,null,true);
                })
            },

            cancelRowEvent (row) {
                const xTable = this.$refs.xTable
                xTable.clearActived().then(() => {
                    // 还原行数据
                    xTable.revertData(row)
                })
            },

            countContractFee (row) {
                return (XEUtils.subtract(row.contractFee, row.discountFee)).toFixed(2);
            },

            countDifference (row) {
                return (XEUtils.subtract(this.countContractFee(row), row.actualFee)).toFixed(2);
            },

            query: function() {
                let that = this;
                ajax.get('api/bss.order/order-material-contract/queryMaterialContract', this.queryCond, function(data){
                    that.tableData = data;
                });
            },


            getDetail (row) {
                let that = this;
                ajax.get('api/bss.order/order-material-contract/getDetail', {orderId:row.orderId}, function(data){
                    that.detailData = data;
                });
                this.showDetails = true
            },

            footerMethod({columns, data}) {
                //                        show-footer
                //                         :footer-method="footerMethod"
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

    });
})