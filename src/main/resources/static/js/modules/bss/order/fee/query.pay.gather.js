require(['vue','ELEMENT','ajax', 'vxe-table', 'vueselect', 'org-orgtree','house-select', 'util', 'xe-utils'], function(Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util, XEUtils) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                queryCond: {

                },
                pays: [],
                pickerOptions: {
                    shortcuts: [{
                        text: '最近一周',
                        onClick(picker) {
                            const end = util.getNowDate();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                            let begin = util.formatDate(start, "YYYY-MM-DD");
                            picker.$emit('pick', [begin, end]);
                        }
                    }, {
                        text: '最近一个月',
                        onClick(picker) {
                            const end = util.getNowDate();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                            let begin = util.formatDate(start, "YYYY-MM-DD");
                            picker.$emit('pick', [begin, end]);
                        }
                    }, {
                        text: '最近三个月',
                        onClick(picker) {
                            const end = util.getNowDate();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                            let begin = util.formatDate(start, "YYYY-MM-DD");
                            picker.$emit('pick', [begin, end]);
                        }
                    }]
                }
            }
        },

        methods: {
            init: function() {

            },

            footerCellClassName: function({ $rowIndex, column, columnIndex }) {
                return "footer";
            },

            query: function() {
                let that = this;
                ajax.post('api/bss/order/order-pay-no/queryPayGather', this.queryCond, function(responseData){
                    if (responseData) {
                        that.pays = responseData;
                    }

                });
            },

            footerMethod: function({ columns, data }) {
                const sums = [];
                columns.forEach((column, columnIndex) => {
                    if (columnIndex === 0) {
                        sums.push('合计');
                    } else {
                        let sumCell = null;
                        let otherCell = '-'
                        switch (column.property) {
                            case 'money':
                                sumCell = XEUtils.sum(data, column.property);
                                break;
                        }
                        sums.push(sumCell);
                    }
                })
                // 返回一个二维数组的表尾合计
                return [ sums];
            }
        },

        mounted () {
            this.init();
        }
    });

    return vm;
});