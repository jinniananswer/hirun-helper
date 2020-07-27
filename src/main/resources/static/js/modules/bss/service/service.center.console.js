require(['vue', 'ELEMENT', 'axios', 'ajax', 'util'], function(Vue, element, axios, ajax, util) {
    let vm = new Vue({
        el: '#app',
        data: function() {
            return {
                tasks: null
            }
        },

        methods: {
            init() {
                let that = this;
                ajax.get('api/bss.service/service-center-domain/queryServicePendingTask', null, function(data) {
                    that.tasks = data;
                })
            },

            openProcess(orderId, custId, type, status,code) {
                let url = null;
                if (type=='1') {
                    url = 'openUrl?url=modules/bss/service/service_repair_manager';
                    util.openPage(url+'&orderId='+orderId+'&custId='+custId+'&status='+status+'&repairNo='+code, '维修售后');
                } else {
                    url = 'openUrl?url=modules/bss/service/service_complain_manager';
                    util.openPage(url+'&orderId='+orderId+'&custId='+custId+'&status='+status+'&complainNo='+code, '投诉管理');
                }
            }
        },

        mounted () {
            this.init();
        }
    });

    return vm;
})