require(['vue','ELEMENT','ajax', 'vxe-table', 'vueselect', 'org-orgtree','house-select', 'util'], function(Vue, element, ajax, table, vueSelect, orgTree, houseSelect, util) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                queryCond: {
                    count: 0,
                    limit: 20,
                    page: 1
                },
                statRoyaltyDetails: [],
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

            query: function() {
                let that = this;
                ajax.get('api/bss.salary/salary-royalty-detail/statByCustOrder', this.queryCond, function(responseData){
                    if (responseData) {
                        that.statRoyaltyDetails = responseData.records;
                        that.queryCond.page = responseData.current;
                        that.queryCond.count = responseData.total;
                    }

                });
            }
        },

        mounted () {
            this.init();
        }
    });

    return vm;
});