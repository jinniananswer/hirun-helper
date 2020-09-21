require(['vue', 'ELEMENT', 'vxe-table', 'axios', 'echarts', 'org-orgtree', 'ajax','order-search-employee','util'], function (Vue, element, vxetable, axios, echarts, orgTree, ajax,orderSearchEmployee,util) {
    Vue.use(echarts)
    let vm = new Vue({
        el: '#app',
        data: function () {
            return {
                queryCond: {
                    shopId: '',
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
                }, {
                    value: '4',
                    label: '事业部'
                }],
                options: [],
                companyOptions:[],
                shopSelectDisabled:false,
                companySelectDisabled:false,
                employeeSelectDisabled:false,
                rules: {
                    queryType: [
                        {required: true, message: '请选择查询类型', trigger: 'change'}
                    ],
                },
            }
        },

        methods: {


            query() {

                if(this.verifyQueryCond()){
                    let that = this;
                    ajax.get('api/bss.customer/customerServiceReport/queryAgentPlanAcutalReport',that.queryCond, function (data) {
                        that.drawEcharts(data);
                    })
                };


            },

            verifyQueryCond:function(){
                if(this.queryCond.queryType==''){
                    this.$message.error('请选择查询方式');
                    return false;
                }
                if(this.queryCond.queryType=='1'){
                    if(this.queryCond.employeeId==''){
                        this.$message.error('查询方式为员工,请选择需要查询的员工');
                        return false;
                    }
                }
                if(this.queryCond.queryType=='2'){
                    if(this.queryCond.shopId==''){
                        this.$message.error('查询方式为门店,请选择需要查询的门店');
                        return false;
                    }
                }

                if(this.queryCond.queryType=='3'){
                    if(this.queryCond.companyId==''){
                        this.$message.error('查询方式为分公司,请选择需要查询的分公司');
                        return;
                    }
                }

                return true;
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
                  this.companySelectDisabled=true;
              }else if (newVal=='2'){
                  this.shopSelectDisabled=false;
                  this.companySelectDisabled=true;
              }else if(newVal=='3'){
                  this.shopSelectDisabled=true;
                  this.companySelectDisabled=false;
              }else{
                  this.shopSelectDisabled=true;
                  this.companySelectDisabled=true;
              }
            },

            drawEcharts:function (data) {

                let xAxisData = [];
                let legendData = [];
                let planData=[];
                let acutalData=[];
                xAxisData = data.action;
                legendData=data.legendData;
                planData=data.planData;
                acutalData=data.acutalData;

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
                        data: planData,
                        itemStyle: {
                            normal: {
                                color: "#1296db",
                                label: {
                                    show: true, //开启显示
                                    position: 'top', //在上方显示
                                    textStyle: { //数值样式
                                        color: 'black',
                                        fontSize: 16
                                    }
                                }                            }
                        },
                    },
                        {
                            name: '实际',
                            type: 'bar',
                            data: acutalData,
                            itemStyle: {
                                normal: {
                                    color: "#0b988f",
                                    label: {
                                        show: true, //开启显示
                                        position: 'top', //在上方显示
                                        textStyle: { //数值样式
                                            color: 'black',
                                            fontSize: 15
                                        }
                                    }
                                }
                            },
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