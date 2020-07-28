require(['vue','ELEMENT','ajax', 'vxe-table', 'vueselect', 'org-orgtree', 'util','house-select','order-selectemployee'], function(Vue, element, ajax, table, vueSelect, orgTree, util,houseSelect,orderSelectEmployee) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                queryCond: {
                    startTime: util.getNowDate(),
                    endTime: util.getNowDate(),
                    page : 1,
                    limit : -1
                },
                serviceRepairHistoryRecordList: []
            }
        },

        methods: {
            query: function() {
                let that = this;
                ajax.get('api/bss.service/service-complain/queryComplainAllRecord', this.queryCond, function(data){
                    that.serviceRepairHistoryRecordList = data;
                });
            },

        },

        mounted () {
        }
    });

    return vm;
});