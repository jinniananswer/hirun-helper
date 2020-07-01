require(['vue', 'ELEMENT', 'vxe-table', 'axios', 'echarts', 'org-orgtree', 'ajax','order-search-employee','util'], function (Vue, element, vxetable, axios, echarts, orgTree, ajax,orderSearchEmployee,util) {
    Vue.use(echarts)
    let vm = new Vue({
        el: '#app',
        data: function () {
            return {
                queryCond: {
                    orgId: '',
                    month : util.getNowMonth(),
                    employeeId:'',
                    queryType:'',
                    employeeName:'',
                    companyId:'',
                },
                queryTypes: [{
                    value: '1',
                    label: '员工'
                }, {
                    value: '2',
                    label: '门店'
                }, {
                    value: '3',
                    label: '分公司'
                },],
                options: [],
                companyOptions:[],
                shopSelectDisabled:false,
            }
        },

        methods: {
            query() {
                let that = this;
                //let orgIdSet = that.queryCond.orgIds;

/*                if (orgIdSet.length <= 0) {
                    this.$message.error('请选择门店');
                    return;
                }*/

/*                let orgId = '';
                for (let i = 0; i < orgIdSet.length; i++) {
                    orgId += orgIdSet[i] + ',';
                }
                that.queryCond.orgIds=orgId;*/
                ajax.get('api/bss.customer/customerServiceReport/queryAgentPlanAcutalReport',that.queryCond, function (data) {
                    that.drawEcharts(data);
                })

            },

            initShopData: function () {
                let that = this;
                ajax.get('api/bss.customer/customerServiceReport/initShopData', null, function (data) {
                    that.options = data;
                })
            },

            initCompanyData: function () {
                let that = this;
                ajax.get('api/bss.customer/customerServiceReport/initCompanyData', null, function (data) {
                    that.companyOptions = data;
                })
            },

            changeQueryType:function(newVal){
              if(newVal=='1'){
                  this.shopSelectDisabled=true;
              }
            },

            drawEcharts:function (data) {

                let xAxisData = [];
                let legendData = [];
                let planData=[];
                xAxisData = data.action;
                legendData=data.legendData;
                planData=data.planData;

                console.log(xAxisData);
                console.log(legendData);
                console.log(planData);


                let option = {
                    title: {
                        text: ''
                    },
                    tooltip: {
                        trigger: "axis",
                        axisPointer: {
                            type: "cross",
                            label: {
                                backgroundColor: "#76baf1"
                            }
                        }
                    },
                    legend: {
                        data: legendData
                    },
                    xAxis: {
                        data: xAxisData,
                        type: "category",
                        axisLabel: {
                            interval: 0,
                            rotate: 40
                        }
                    },
                    yAxis: {
                        type: "value"
                    },
                    series: [{
                        name: '计划',
                        type: 'bar',
                        data: planData
                    },
                        {
                            name: '实际',
                            type: 'bar',
                            data: [1, 20, 16, 20, 10, 40, 10, 2, 5]
                        }
                    ]
                };

                console.log(data.action);

                let myChart = echarts.init(document.getElementById('echarts_box'))

                myChart.setOption(option)
            }
        },

        mounted() {
            this.initShopData();
            this.initCompanyData();
        }

    });
})