require(['vue','ELEMENT','ajax', 'vxe-table', 'vueselect', 'org-orgtree', 'util','shop-select'], function(Vue, element, ajax, table, vueSelect, orgTree, util,shopSelect) {
    Vue.use(table);
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                queryCond: {
                    queryType : '1'
                },
                employeeDisplay: 'display:block',
                orgDisplay : 'display:none',
                pushDatas: [],
            }
        },

        methods: {
            init: function() {

            },

            query: function() {
                let that = this;
                ajax.get('api/MidprodSend/querySendCountData', this.queryCond, function(responseData){
                        that.pushDatas = responseData;
                });
            },

            footerMethod ({ columns, data }) {
                return [
                    columns.map((column, columnIndex) => {
                        if (columnIndex === 0) {
                            return `合计`
                        }
                        switch (column.property) {
                            case 'pushNum':
                                return `合计推送 ${XEUtils.sum(data, 'pushNum')}`
                            case 'openNum':
                                return `合计打开 ${XEUtils.sum(data, 'openNum')}`
                            case 'openRate':
                                let pushNum=XEUtils.sum(data, 'pushNum');
                                let openNum=XEUtils.sum(data, 'openNum')
                                return `平均打开率 ${(Math.round(openNum / pushNum * 10000) / 100.00)+"%"}`
                        }
                        return '-'
                    })
                ]
            },


            changeType : function(value) {
                if (value == '1') {
                    this.orgDisplay = 'display:none';
                    this.employeeDisplay = 'display:block';
                } else if (value == '2') {
                    this.orgDisplay = 'display:block';
                    this.employeeDisplay = 'display:none';
                }
            }
        },

        mounted () {
            this.init();
        }
    });

    return vm;
});