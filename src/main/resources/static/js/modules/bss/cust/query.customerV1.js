require(['vue', 'ELEMENT', 'axios', 'ajax', 'vueselect', 'util', 'order-selectemployee', 'org-orgtree','house-select'], function (Vue, element, axios, ajax, vueselect, util, orderSelectEmployee, orgOrgTree,houseSelect) {
    let vm = new Vue({
        el: '#app',
        data: function () {
            return {
                custQueryCond: {
                    custName: '',
                    customerServiceEmployeeId: '',
                    counselorEmployeeId: '',
                    informationSource: '',
                    customerType: '',
                    orgId: '',
                    orgName: '',
                    timeType: '',
                    startTime: util.getNowDate(),
                    endTime: util.getNowDate(),
                    houseMode: '',
                    wxNick:'',
                    houseId:'',
                    busiStatus:'1',
                    page:1,
                    size:10,
                    total:0
                },

                custId: '',
                customerInfo: [],
                checked: null,
                display: 'display:block',
                mobileNo:'',

                statusOptions: [{
                    value: '1',
                    label: '已咨询客户'
                }, {
                    value: '2',
                    label: '未咨询客户'
                }]
            }
        },

        methods: {
            queryCustomer: function () {
                let that = this;
                ajax.get('api/bss.customer/customer/queryCustomerInfo', this.custQueryCond, function (responseData) {
                    vm.customerInfo = responseData.records;
                    that.custQueryCond.page = responseData.current;
                    that.custQueryCond.total = responseData.total;
                });
            },


            getTemplateRow(index, row) {
                this.templateSelection = row;
                this.custId = row.custId;
                this.mobileNo=row.mobileNo;
            },


            handleSizeChange: function (size) {
                this.custQueryCond.size = size;
                this.custQueryCond.page = 1;
                this.queryCustomer();
            },

            handleCurrentChange: function(currentPage){
                this.custQueryCond.page = currentPage;
                this.queryCustomer();
            },

            toOrderDetail(custId, openId,partyId) {
                console.log(custId+"--"+openId+"---"+partyId)
                util.openPage('openUrl?url=modules/bss/cust/customer_info_detail&custId='+custId+'&openId='+openId+'&partyId='+partyId, '客户详情');
            }
        }
    });

    return vm;
})