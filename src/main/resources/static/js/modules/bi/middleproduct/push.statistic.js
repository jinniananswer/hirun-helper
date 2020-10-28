require(['vue','ELEMENT','ajax', 'vxe-table', 'vueselect', 'org-orgtree', 'util'], function(Vue, element, ajax, table, vueSelect, orgTree, util) {
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
                    if (responseData) {
                        that.pushDatas = responseData;
                    }

                });
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